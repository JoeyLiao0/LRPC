package joey.threadPool;

import joey.common.entity.NetworkEndpoint;

import java.util.Map;
import java.util.concurrent.ExecutorService;

public class ServerThreadPool {

    //执行器
    private ExecutorService executor;

    //线程池最大线程数
    private Integer threadNum;

    //服务-类映射
    private Map<String,Class<?>> serviceClassMap;

    //服务端地址
    private NetworkEndpoint serverEndpoint;

    //初始化配置
    public ServerThreadPool(Integer threadNum,Map<String,Class<?>> serviceClassMap,NetworkEndpoint serverEndpoint){

    }

    //启动线程池
    public void start(){

    }

}
