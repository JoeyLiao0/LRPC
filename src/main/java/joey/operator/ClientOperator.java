package joey.operator;

import joey.common.entity.NetworkEndpoint;

import java.util.Map;

public class ClientOperator {

    //从注册中心获取服务端地址
    public NetworkEndpoint getServerEndpoint(NetworkEndpoint registerEndpoint,String className,String methodName){
        return null;
    }

    //执行方法
    public Object invokeMethod(NetworkEndpoint serverEndpoint, String className, String methodName, Map<String,Object> parameterMap){
        return null;
    }

}
