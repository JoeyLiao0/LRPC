package joey.api;

import joey.common.entity.NetworkEndpoint;
import joey.threadPool.RegisterThreadPool;

public class LRPCRegister {

    //注册中心地址
    private NetworkEndpoint registerEndpoint;

    //线程池最大线程数
    private Integer threadNum;

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

    //启动注册中心
    public void start(){
        //线程池启动
        new RegisterThreadPool(threadNum,registerEndpoint).start();
    }
}
