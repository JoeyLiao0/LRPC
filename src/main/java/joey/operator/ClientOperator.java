package joey.operator;

import com.alibaba.fastjson.JSON;
import joey.common.entity.NetworkEndpoint;
import joey.common.entity.msg.DiscoveryRequest;
import joey.common.entity.msg.DiscoveryResponse;
import joey.common.entity.msg.InvokeRequest;
import joey.common.entity.msg.InvokeResponse;
import joey.common.util.myLoadBalance;
import joey.common.util.mySocket;


import java.io.IOException;

import java.net.Socket;
import java.util.*;

public class ClientOperator {

    //从注册中心获取服务端地址
    public NetworkEndpoint getServerEndpoint(NetworkEndpoint registerEndpoint,String serviceName){
        //创建请求消息 msg
        DiscoveryRequest discoveryRequest = new DiscoveryRequest(serviceName);
        Map<String,Object> msg = new HashMap<>();
        msg.put("type","DiscoveryRequest");
        msg.put("payload",discoveryRequest);

        //序列化Map
        String msgString = JSON.toJSONString(msg);

        try{
            Socket socket = new Socket(registerEndpoint.getIp(),registerEndpoint.getPort());
            //消息发送与接收
            mySocket ms = new mySocket(socket);
            //发送
            ms.sendMessage(msgString);

            //接收
            Map<String,Object> map2 = ms.receiveMessage();

            //由最初的发起者调用
            ms.close();

            if(map2.get("type").equals("DiscoveryResponse")){

                String jsonString = JSON.toJSONString(map2.get("payload"));
                DiscoveryResponse discoveryResponse = JSON.parseObject(jsonString,DiscoveryResponse.class);
                Set<NetworkEndpoint> s = discoveryResponse.getServerEndpointList();

                //负载均衡
                return myLoadBalance.getServerEndpoint(s);

            }else{
                System.out.println("---发现服务失败，注册中心返回有误---");
            }

        }catch(IOException e){
            System.out.println("---连接注册中心失败---");
        }

        return null;
    }

    //执行方法
    public Object invokeMethod(NetworkEndpoint serverEndpoint, String serviceName, Map<String,Object> parameterMap){
        //创建请求消息 msg
        InvokeRequest invokeRequest = new InvokeRequest(serviceName,parameterMap);
        Map<String,Object> msg = new HashMap<>();
        msg.put("type","InvokeRequest");
        msg.put("payload",invokeRequest);

        //序列化Map
        String msgString = JSON.toJSONString(msg);

        try{
            Socket socket = new Socket(serverEndpoint.getIp(),serverEndpoint.getPort());

            mySocket ms = new mySocket(socket);

            //发送
            ms.sendMessage(msgString);

            //接收
            Map<String,Object> map2 = ms.receiveMessage();

            //关闭
            ms.close();

            if(map2.get("type").equals("InvokeResponse")){

                String jsonString = JSON.toJSONString(map2.get("payload"));
                InvokeResponse invokeResponse = JSON.parseObject(jsonString, InvokeResponse.class);

                return invokeResponse.getResult();

            }else{
                System.out.println("---调用服务失败，服务端返回有误---");
                System.out.println(map2.get("type"));
            }

        }catch(IOException e){
            System.out.println("---连接服务端失败---");
        }

        return null;
    }

}
