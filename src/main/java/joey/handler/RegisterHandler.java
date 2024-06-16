package joey.handler;

import joey.common.entity.NetworkEndpoint;

import java.net.Socket;
import java.util.Map;

public class RegisterHandler implements Runnable {

    //socket 来自服务端/客户端
    private Socket Socket;

    //上一级维护的服务-地址映射
    private Map<String, NetworkEndpoint> serviceEndpointMap;

    //注入socket 和 服务-地址映射
    public RegisterHandler(Socket socket,Map<String,NetworkEndpoint> serviceEndpointMap){

    }

    //解析socket，处理服务端的注册/客户端的发现
    public void run(){

    }
}
