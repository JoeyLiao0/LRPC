package joey.api;

import joey.common.annotation.LRPCService;
import joey.common.entity.NetworkEndpoint;
import joey.common.exception.NoSuchServiceException;
import joey.handler.ServerHandler;
import joey.operator.ServerOperator;
import joey.threadPool.ServerThreadPool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Pattern;

public class LRPCServer {

    //线程池线程数
    private Integer threadNum;

    //注册中心地址
    private NetworkEndpoint registerEndpoint;

    //服务端地址
    private NetworkEndpoint serverEndpoint;

    //服务-类映射
    private Map<String, Class<?>> serviceClassMap;

    //扫描路径
    private String scanDir;

    //前缀路径
    private String rootPath;

    //初始化配置，设置注册中心地址、线程数
    public void init(String registerIp, Integer registerPort, Integer threadNum, String scanDir) {
        //注入
        this.registerEndpoint = new NetworkEndpoint(registerIp, registerPort);
        this.threadNum = threadNum;
        this.scanDir = scanDir;

        this.serviceClassMap = new HashMap<>();
        //判断地址是否有效
        if (registerEndpoint.isValid()) {
            System.out.println("---成功加载配置---");
        } else {
            throw new RuntimeException("---ip地址/端口无效---");
        }
    }

    //启动服务端，提供服务
    public void start() {
        try{
            //获取本地地址
            getServerEndpoint();

            //获取本地服务
            getServiceClassMap();

            //服务注册
            new ServerOperator().registerService(registerEndpoint, serverEndpoint, serviceClassMap);

            //开启监听
            new ServerThreadPool(threadNum, serviceClassMap, serverEndpoint).start();

        }catch (RuntimeException e){
            System.out.println(e.getMessage());
        }

    }

    //获取服务端地址（本地ip+可用的端口）
    private void getServerEndpoint() {
        int port = -1;
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            // 使用默认构造函数或传入0作为端口号
            port = serverSocket.getLocalPort();
        } catch (IOException e) {
            // 处理IOException异常
            System.out.println("---获取服务端地址失败---");
        }
        if(port!=-1) {
            this.serverEndpoint = new NetworkEndpoint("localhost", port);
        }else {
            this.serverEndpoint = null;
            System.out.println("---服务端找不到可用端口---");
        }
    }

    //获取所有提供的服务
    private void getServiceClassMap() {
        //注解 @LRPCService 来标识本地服务类
        //this.scanDir,扫描的根目录
        try {
            // 把 包路径之间的.换成文件分隔符
            String packageDirName = this.scanDir.replaceAll("\\.","/");
            //通过当前线程获取类加载器，进而获得包根目录的资源路径（运行时）。（因为是实际上从当前线程获取的类加载器，所以是运行时的资源路径）
            URL url =Thread.currentThread().getContextClassLoader().getResource(packageDirName);

            if (url != null) {
//                System.out.println(url);
                if ("jar".equals(url.getProtocol())) {
                    // 资源位于JAR中
                    loadBeanFromJar(url);
                } else {
                    // 资源位于文件系统
                    String filePath = URLDecoder.decode(url.getFile(), "utf-8");
                    this.rootPath = filePath.substring(0, filePath.length() - packageDirName.length());
                    File dir = new File(filePath);
                    if (dir.exists()) {
                        loadBean(dir);
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadBean(File fileParent){
        if (fileParent.isDirectory()) {
            //如果是目录
            File[] childrenFiles = fileParent.listFiles();
            if(childrenFiles == null || childrenFiles.length == 0){
                //目录为空
                return;
            }
            for (File child : childrenFiles) {
                //遍历全部子文件
                if (child.isDirectory()) {
                    //如果是个文件夹就继续调用该方法,使用了递归
                    loadBean(child);
                } else {
                    //通过文件路径转变成全类名,第一步把绝对路径部分去掉
                    String pathWithClass = child.getAbsolutePath().substring(this.rootPath.length() - 1);
                    //选中class文件
                    if (pathWithClass.contains(".class")) {
                        //去掉.class后缀，并且把 \ 替换成 .
                        String fullName = pathWithClass.replaceAll(Pattern.quote(File.separator), ".").replace(".class", "");
                        try {
                            Class<?> aClass = Class.forName(fullName);
                            //把非接口的类实例化放在map中
                            if(!aClass.isInterface()){
                                //判断类是否有被LRPCService所注释
                                LRPCService annotation = aClass.getAnnotation(LRPCService.class);
                                if(annotation != null){
                                        System.out.println("---扫描到服务类【"+ fullName +"】---");
                                        serviceClassMap.put(fullName,aClass);
                                }
                            }
                        } catch (ClassNotFoundException  e) {
                            throw new NoSuchServiceException(e);
                        }
                    }
                }
            }
        }
    }

    private void loadBeanFromJar(URL jarUrl) {
        try {
            String jarPath = jarUrl.getPath().substring(5, jarUrl.getPath().indexOf("!"));
            String internalPath = jarUrl.getPath().substring(jarUrl.getPath().indexOf("!") + 2);
//            System.out.println(internalPath);
            String i = internalPath.replace("/",".");
            try (JarInputStream jarStream = new JarInputStream(new FileInputStream(jarPath))) {
                JarEntry entry;
                while ((entry = jarStream.getNextJarEntry()) != null) {
                    if (!entry.isDirectory() && entry.getName().startsWith(internalPath) && entry.getName().endsWith(".class")) {
                        String className = entry.getName().substring(internalPath.length() + 1, entry.getName().length() - 6).replace('/', '.');
//                        System.out.println("className:"+i+"."+className);
                        loadClass(i+"."+className);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading beans from JAR", e);
        }
    }

    private void loadClass(String className) {
        try {
            Class<?> aClass = this.getClass().getClassLoader().loadClass(className);
            if (!aClass.isInterface()) {
                LRPCService annotation = aClass.getAnnotation(LRPCService.class);
                if (annotation != null) {
                    System.out.println("---扫描到服务类【" + className + "】---");
                    this.serviceClassMap.put(className, aClass);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new NoSuchServiceException(e);
        }
    }
}
