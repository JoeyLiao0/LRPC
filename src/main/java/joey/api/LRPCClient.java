package joey.api;

import joey.common.entity.NetworkEndpoint;
import joey.operator.ClientOperator;

import java.util.Map;

public class LRPCClient {

    //invokeMethod超时时间
    private Long requestTimeout;

    //注册中心地址
    private NetworkEndpoint registerEndpoint;

    //初始化配置，设置注册中心地址、超时时间
    public void init(String registerIp,Integer registerPort,Long timeout){
        //注入
        this.registerEndpoint = new NetworkEndpoint(registerIp,registerPort);
        this.requestTimeout = timeout;
        //判断地址是否有效
        if(registerEndpoint.isValid()){
            System.out.println("---成功加载配置---");
        }else{
            throw new RuntimeException("---ip地址/端口无效---");
        }
    }

    //调用方法，返回结果
    public Object invokeMethod(String className, String methodName, Map<String,Object> parameterMap){
        ClientOperator clientOperator = new ClientOperator();
        //查询到serverEndpoint
        // TODO：这里可以返回可用列表，让客户端自行选择
        NetworkEndpoint serverEndpoint = clientOperator.getServerEndpoint(registerEndpoint,className,methodName);

        if(serverEndpoint!=null){
            return clientOperator.invokeMethod(serverEndpoint,className,methodName,parameterMap);
        }else{
            throw new RuntimeException("---未找到该服务---");
        }
    }
}
