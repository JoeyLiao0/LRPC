package joey.threadPool;

import joey.common.entity.NetworkEndpoint;
import joey.handler.RegisterHandler;

import java.util.Map;
import java.util.concurrent.ExecutorService;

public class RegisterThreadPool {

    //执行器
    private ExecutorService executor;

    //线程池最大线程数
    private Integer threadNum;

    //服务-类映射
    private Map<String,NetworkEndpoint> serviceEndpointMap;

    //注册中心地址
    private NetworkEndpoint registerEndpoint;

    //初始化配置
    public RegisterThreadPool(Integer threadNum,NetworkEndpoint registerEndpoint){

    }

    //启动线程池
    public void start(){

    }

}
