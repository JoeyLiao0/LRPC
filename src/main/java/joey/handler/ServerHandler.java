package joey.handler;

import com.alibaba.fastjson.JSON;
import joey.common.entity.msg.InvokeRequest;
import joey.common.entity.msg.InvokeResponse;
import joey.common.util.mySocket;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerHandler implements Runnable {

    //socket 来自客户端
    private mySocket clientSocket;

    //服务端维护的服务-类映射
    private Map<String,Class<?>> serviceClassMap;

    //注入socket、服务-类映射
    public ServerHandler(Socket socket,Map<String,Class<?>> serviceClassMap){
        this.clientSocket = new mySocket(socket);
        this.serviceClassMap = serviceClassMap;
    }

    //解析socket，处理方法调用
    public void run(){
        Map<String,Object> request = this.clientSocket.receiveMessage();

        if(request.get("type").equals("InvokeRequest")){
            //方法调用
            String jsonString = JSON.toJSONString(request.get("payload"));
            InvokeRequest invokeRequest = JSON.parseObject(jsonString,InvokeRequest.class);

            String serviceName = invokeRequest.getServiceName();

            Map<String,Object> response = new HashMap<>();
            response.put("type","InvokeResponse");

            String serviceClassName = serviceName.replaceAll("\\.[^.]*$", "");

            if(this.serviceClassMap.containsKey(serviceClassName)){
                //找到了服务对应的类，执行
                String[] ss =serviceName.split("\\.");

                Object result = invoke(this.serviceClassMap.get(serviceClassName),ss[ss.length-1],invokeRequest.getParameter());
                response.put("payload",new InvokeResponse(result,true));
            }else{
                //该服务未找到
                response.put("payload",new InvokeResponse(null,false));
            }

            String responseString = JSON.toJSONString(response);

            this.clientSocket.sendMessage(responseString);

        }else{
            System.out.println("---注册中心接收到无法识别的消息，type为"+request.get("type")+"---");
        }
    }

    private Object invoke(Class<?> clazz,String methodName,Map<String,Object> parameters)  {
        //利用java的反射，执行本地函数
        try{
            Method[] ms = clazz.getDeclaredMethods();
            Method method = null;
            for(Method m : ms){
                if(m.getName().equals(methodName)){
                    method = m;
                    break;
                }
            }
            if(method==null){
                throw new NoSuchMethodException();
            }

            method.setAccessible(true);

            Object[] params = new Object[parameters.size()];
            int i = 0;
            for(Map.Entry<String,Object> entry : parameters.entrySet()){
                params[i++] = entry.getValue();
            }

            //创建实例
            Object instance = clazz.getDeclaredConstructor().newInstance();

            //执行方法
            Object result = method.invoke(instance, params);

            return result;

        }catch (NoSuchMethodException e){
            System.out.println("---服务端找不到对应方法---");
        } catch (Exception e){
            System.out.println("---服务端调用方法失败---");
        }
        return null;
    }
}