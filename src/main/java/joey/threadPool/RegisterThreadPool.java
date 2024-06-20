package joey.threadPool;

import joey.common.entity.NetworkEndpoint;
import joey.handler.RegisterHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterThreadPool {

    //执行器
    private ExecutorService executor;

    //线程池线程数
    private Integer threadNum;

    //服务-类映射
    //假设服务名是唯一的
    private Map<String,Set<NetworkEndpoint>> serviceEndpointMap;

    //注册中心地址
    private NetworkEndpoint registerEndpoint;

    //初始化配置
    public RegisterThreadPool(Integer threadNum,NetworkEndpoint registerEndpoint){
        try{
            this.registerEndpoint = registerEndpoint;
            this.threadNum = threadNum;
            this.executor = Executors.newFixedThreadPool(this.threadNum);
            this.serviceEndpointMap = new HashMap<String,Set<NetworkEndpoint>>();
            System.out.println("---注册中心初始化成功---");
        }catch (Exception e){
            System.out.println("---注册中心初始化失败---");
        }

    }

    //启动线程池
    public void start(){
        try(ServerSocket registerSocket = new ServerSocket(registerEndpoint.getPort())){
            System.out.println("---注册中心启动成功，正在监听" + registerEndpoint.getPort()+"端口---");
            while(true){
                Socket socket = registerSocket.accept();
                executor.submit(new RegisterHandler(socket,serviceEndpointMap));
            }
        }catch (Exception e){
            System.out.println("---注册中心启动失败---");
        }
    }

}
