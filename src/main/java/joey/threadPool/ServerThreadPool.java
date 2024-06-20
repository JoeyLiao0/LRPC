package joey.threadPool;

import joey.common.entity.NetworkEndpoint;
import joey.handler.ServerHandler;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerThreadPool {

    //执行器
    private ExecutorService executor;

    //线程池线程数
    private Integer threadNum;

    //服务-类映射
    private Map<String,Class<?>> serviceClassMap;

    //服务端地址
    private NetworkEndpoint serverEndpoint;

    //初始化配置
    public ServerThreadPool(Integer threadNum,Map<String,Class<?>> serviceClassMap,NetworkEndpoint serverEndpoint){
        try{
            this.threadNum = threadNum;
            this.executor = Executors.newFixedThreadPool(this.threadNum);
            this.serviceClassMap = serviceClassMap;
            this.serverEndpoint = serverEndpoint;
            System.out.println("---服务端初始化成功---");
        }catch (Exception e){
            System.out.println("---服务端初始化失败---");
        }
    }

    //启动线程池
    public void start(){
        try(ServerSocket socket = new ServerSocket(serverEndpoint.getPort())){
            System.out.println("---服务端启动成功,正在监听" + serverEndpoint.getPort()+"端口---");
            while(true){
                Socket clientSocket = socket.accept();
                executor.submit(new ServerHandler(clientSocket,serviceClassMap));
            }
        }catch (Exception e){
            System.out.println("---服务端启动失败---");
        }
    }

}
