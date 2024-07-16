package joey.api;

import joey.common.entity.NetworkEndpoint;
import joey.threadPool.RegisterThreadPool;

public class LRPCRegister {

    //注册中心地址
    private NetworkEndpoint registerEndpoint;

    //线程池线程数
    private Integer threadNum;

    //初始化配置，设置注册中心地址、最大线程数
    public void init(String registerIp,Integer registerPort,Integer threadNum){
        //注入
        this.registerEndpoint = new NetworkEndpoint(registerIp,registerPort);
        this.threadNum = threadNum;
        //判断
        if(!registerEndpoint.isValid()){
            throw new RuntimeException("---ip地址/端口无效---");
        }
        if(threadNum<=0){
            throw  new IllegalArgumentException("---线程数非法---");
        }
        System.out.println("---成功加载配置---");
    }

    //启动注册中心
    public void start(){
        //线程池启动
        new RegisterThreadPool(threadNum,registerEndpoint).start();
    }
}
