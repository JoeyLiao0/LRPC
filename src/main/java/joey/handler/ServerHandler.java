package joey.handler;

import joey.common.entity.NetworkEndpoint;

import java.net.Socket;
import java.util.Map;

public class ServerHandler implements Runnable {

    //socket 来自客户端
    private Socket clientSocket;

    //服务端维护的服务-类映射
    private Map<String,Class<?>> serviceClassMap;

    //注入socket、服务-类映射
    public ServerHandler(Socket socket,Map<String,Class<?>> serviceClassMap){

    }

    //解析socket，处理方法调用
    public void run(){

    }

}
