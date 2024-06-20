package joey.handler;

import com.alibaba.fastjson.JSON;
import joey.common.entity.NetworkEndpoint;
import joey.common.entity.msg.DiscoveryRequest;
import joey.common.entity.msg.DiscoveryResponse;
import joey.common.entity.msg.RegisterRequest;
import joey.common.entity.msg.RegisterResponse;
import joey.common.util.mySocket;

import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RegisterHandler implements Runnable {

    //socket 来自服务端/客户端
    private mySocket socket;

    //上一级维护的服务-地址映射
    private Map<String,Set<NetworkEndpoint>> serviceEndpointMap;

    //注入socket 和 服务-地址映射
    public RegisterHandler(Socket socket,Map<String, Set<NetworkEndpoint>> serviceEndpointMap){
        this.socket =new mySocket(socket);
        this.serviceEndpointMap = serviceEndpointMap;
    }

    //解析socket，处理服务端的注册/客户端的发现
    public void run(){

        //接收
        Map<String,Object> request = this.socket.receiveMessage();
        String type = (String) request.get("type");
        if(type.equals("DiscoveryRequest")){
            //服务发现
            String jsonString = JSON.toJSONString(request.get("payload"));
            DiscoveryRequest discoveryRequest = JSON.parseObject(jsonString,DiscoveryRequest.class);

            String serviceName = discoveryRequest.getServiceName();

            Map<String,Object> response = new HashMap<>();
            response.put("type","DiscoveryResponse");

            DiscoveryResponse discoveryResponse = new DiscoveryResponse(this.serviceEndpointMap.getOrDefault(serviceName, null));

            response.put("payload",discoveryResponse);

            //返回
            String responseString = JSON.toJSONString(response);
            this.socket.sendMessage(responseString);

        }else if(type.equals("RegisterRequest")){
            //服务注册
            String jsonString = JSON.toJSONString(request.get("payload"));
            RegisterRequest registerRequest = JSON.parseObject(jsonString,RegisterRequest.class);

            Set<String> services = registerRequest.getServiceName();
            NetworkEndpoint serverEndpoint = registerRequest.getServerEndpoint();

            for(String s : services){
                Set<NetworkEndpoint> endpoints = this.serviceEndpointMap.getOrDefault(s,new HashSet<>());
                endpoints.add(serverEndpoint);
                this.serviceEndpointMap.put(s,endpoints);
            }

            for (String s : this.serviceEndpointMap.keySet()){
                System.out.println(s);
            }

            Map<String,Object> response = new HashMap<>();

            RegisterResponse registerResponse = new RegisterResponse("服务成功注册");

            response.put("type","RegisterResponse");
            response.put("payload", registerResponse);


            //返回
            String responseString = JSON.toJSONString(response);
            System.out.println("register 返回 " + responseString);
            this.socket.sendMessage(responseString);

        }else{
            System.out.println("---注册中心接收到无法识别的消息，type为"+type+"---");
        }

    }
}
