package joey.operator;

import com.alibaba.fastjson.JSON;
import joey.common.entity.NetworkEndpoint;
import joey.common.entity.msg.RegisterRequest;
import joey.common.entity.msg.RegisterResponse;
import joey.common.util.mySocket;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.*;

public class ServerOperator {

    //服务注册
    public void registerService(NetworkEndpoint registerEndpoint, NetworkEndpoint serverEndpoint, Map<String,Class<?>>serviceClass){
        //创建请求消息 msg
        //类.方法

        Set<String> serviceNames = new HashSet<>();
        for(String className : serviceClass.keySet()){
            Class<?> clazz = serviceClass.get(className);
            Method[] methods = clazz.getMethods();
            for(Method method : methods){
                serviceNames.add(className+"."+method.getName());
            }
        }

        RegisterRequest registerRequest = new RegisterRequest(serviceNames,serverEndpoint);
        Map<String,Object> msg = new HashMap<>();
        msg.put("type","RegisterRequest");
        msg.put("payload",registerRequest);

        //序列化Map
        String msgString = JSON.toJSONString(msg);

        try{
            Socket socket = new Socket(registerEndpoint.getIp(),registerEndpoint.getPort());

            mySocket ms = new mySocket(socket);

            //发送
            ms.sendMessage(msgString);

            //接收
            Map<String,Object> map2 = ms.receiveMessage();

            ms.close();

            if(map2.get("type").equals("RegisterResponse")){

                String jsonString = JSON.toJSONString(map2.get("payload"));
                RegisterResponse registerResponse = JSON.parseObject(jsonString, RegisterResponse.class);

                System.out.println(registerResponse.getMsg());

            }else{
                System.out.println("---注册服务失败，服务端返回有误---");
            }
        }catch(IOException e){
            System.out.println("---连接注册中心失败---");
            e.printStackTrace();
        }

    }
}