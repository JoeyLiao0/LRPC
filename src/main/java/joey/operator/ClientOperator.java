package joey.operator;

import com.alibaba.fastjson.JSON;
import joey.common.entity.NetworkEndpoint;
import joey.common.entity.RpcExceptionCode;
import joey.common.entity.msg.DiscoveryRequest;
import joey.common.entity.msg.DiscoveryResponse;
import joey.common.entity.msg.InvokeRequest;
import joey.common.entity.msg.InvokeResponse;
import joey.common.exception.FailToConnectException;
import joey.common.exception.FailToInvokeException;
import joey.common.exception.MysocketException;
import joey.common.exception.NoSuchServiceException;
import joey.common.util.myLoadBalance;
import joey.common.util.mySocket;


import java.io.IOException;

import java.net.Socket;
import java.util.*;

public class ClientOperator {

    //从注册中心获取服务端地址
    public NetworkEndpoint getServerEndpoint(NetworkEndpoint registerEndpoint, String serviceName) throws FailToConnectException, MysocketException {
        //创建请求消息 msg
        DiscoveryRequest discoveryRequest = new DiscoveryRequest(serviceName);
        Map<String, Object> msg = new HashMap<>();
        msg.put("type", "DiscoveryRequest");
        msg.put("payload", discoveryRequest);

        //序列化Map
        String msgString = JSON.toJSONString(msg);

        mySocket ms = null;

        try {
            Socket socket = new Socket(registerEndpoint.getIp(), registerEndpoint.getPort());

            //消息发送与接收
            ms = new mySocket(socket);

            //发送
            ms.sendMessage(msgString);

            //接收
            Map<String, Object> map2 = ms.receiveMessage();

            if (map2.get("type").equals("DiscoveryResponse")) {

                String jsonString = JSON.toJSONString(map2.get("payload"));
                DiscoveryResponse discoveryResponse = JSON.parseObject(jsonString, DiscoveryResponse.class);
                Set<NetworkEndpoint> s = discoveryResponse.getServerEndpointList();

                //负载均衡
                return myLoadBalance.getServerEndpoint(s);

            }

        } catch (IOException e) {
            throw new FailToConnectException("---连接注册中心失败---", e);
        } finally {
            if (ms != null) {
                //关闭
                ms.close();
            }
        }

        return null;
    }

    //执行方法
    public Object invokeMethod(NetworkEndpoint serverEndpoint, String serviceName, List<Object> parameterList) throws FailToConnectException, MysocketException,NoSuchServiceException,FailToInvokeException {
        //创建请求消息 msg
        InvokeRequest invokeRequest = new InvokeRequest(serviceName, parameterList);
        Map<String, Object> msg = new HashMap<>();
        msg.put("type", "InvokeRequest");
        msg.put("payload", invokeRequest);

        //序列化Map
        String msgString = JSON.toJSONString(msg);

        mySocket ms = null;

        try {

            Socket socket = new Socket(serverEndpoint.getIp(), serverEndpoint.getPort());

            ms = new mySocket(socket);

            //发送
            ms.sendMessage(msgString);

            //接收
            Map<String, Object> map2 = ms.receiveMessage();


            if (map2.get("type").equals("InvokeResponse")) {

                String jsonString = JSON.toJSONString(map2.get("payload"));
                InvokeResponse invokeResponse = JSON.parseObject(jsonString, InvokeResponse.class);

                switch (invokeResponse.getRpcExceptionCode()){
                    case SUCCESS:
                        return invokeResponse.getResult();
                    case INVOKE_FAILURE:
                        throw new FailToInvokeException("---调用服务失败---");
                    case NO_SUCH_SERVICE:
                        throw new NoSuchServiceException("---服务不存在---");
                }
            }

        } catch (IOException e) {
            throw new FailToConnectException("---连接服务器失败---", e);
        } finally {
            if (ms != null) {
                //关闭
                ms.close();
            }
        }

        return null;
    }

}
