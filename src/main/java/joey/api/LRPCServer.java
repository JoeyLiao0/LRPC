package joey.api;

import joey.common.entity.NetworkEndpoint;
import joey.handler.ServerHandler;
import joey.operator.ServerOperator;
import joey.threadPool.ServerThreadPool;

import java.util.Map;

public class LRPCServer {

    //线程池最大线程数
    private Integer threadNum;

    //注册中心地址
    private NetworkEndpoint registerEndpoint;

    //服务端地址
    private NetworkEndpoint serverEndpoint;

    //服务-类映射
    private Map<String,Class<?>> serviceClassMap;

    //初始化配置，设置注册中心地址、最大线程数
    public void init(String registerIp,Integer registerPort,Integer threadNum){
        //注入
        this.registerEndpoint = new NetworkEndpoint(registerIp,registerPort);
        this.threadNum = threadNum;
        //判断地址是否有效
        if(registerEndpoint.isValid()){
            System.out.println("---成功加载配置---");
        }else{
            throw new RuntimeException("---ip地址/端口无效---");
        }
    }

    //启动服务端，提供服务
    public void start(){
        //获取本地地址
        this.serverEndpoint = getServerEndpoint();

        //获取本地服务
        this.serviceClassMap = getServiceClassMap();

        //服务注册
        new ServerOperator().registerService(registerEndpoint,serverEndpoint,serviceClassMap.keySet());

        //开启监听
        new ServerThreadPool(threadNum,serviceClassMap,serverEndpoint).start();
    }

    //获取服务端地址（本地ip+可用的端口）
    private NetworkEndpoint getServerEndpoint(){
        return null;
    }

    //获取所有提供的服务
    private Map<String,Class<?>> getServiceClassMap(){
        return null;
    }

}
