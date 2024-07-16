package joey.handler;

import com.alibaba.fastjson.JSON;
import joey.common.entity.RpcExceptionCode;
import joey.common.entity.msg.InvokeRequest;
import joey.common.entity.msg.InvokeResponse;
import joey.common.exception.FailToInvokeException;
import joey.common.exception.NoSuchServiceException;
import joey.common.util.mySocket;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerHandler implements Runnable {

    //socket 来自客户端
    private final mySocket clientSocket;

    //服务端维护的服务-类映射
    private final Map<String, Class<?>> serviceClassMap;

    //注入socket、服务-类映射
    public ServerHandler(Socket socket, Map<String, Class<?>> serviceClassMap) {
        this.clientSocket = new mySocket(socket);
        this.serviceClassMap = serviceClassMap;
    }

    //解析socket，处理方法调用
    public void run() {
        Map<String, Object> request = this.clientSocket.receiveMessage();

        if (request.get("type").equals("InvokeRequest")) {
            //方法调用
            String jsonString = JSON.toJSONString(request.get("payload"));
            InvokeRequest invokeRequest = JSON.parseObject(jsonString, InvokeRequest.class);

            String serviceName = invokeRequest.getServiceName();

            Map<String, Object> response = new HashMap<>();
            response.put("type", "InvokeResponse");

            String serviceClassName = serviceName.replaceAll("\\.[^.]*$", "");
            try {
                if (this.serviceClassMap.containsKey(serviceClassName)) {
                    //找到了服务对应的类，执行
                    String[] ss = serviceName.split("\\.");
                    Object result = invoke(this.serviceClassMap.get(serviceClassName), ss[ss.length - 1], invokeRequest.getParameter());
                    response.put("payload", new InvokeResponse(result, RpcExceptionCode.SUCCESS));
                } else {
                    //该服务未找到
                    response.put("payload", new InvokeResponse(null, RpcExceptionCode.NO_SUCH_SERVICE));
                }

            } catch (FailToInvokeException e) {
                response.put("payload", new InvokeResponse(null, RpcExceptionCode.INVOKE_FAILURE));
            } finally {
                String responseString = JSON.toJSONString(response);
                this.clientSocket.sendMessage(responseString);
            }

        } else {
            System.out.println("---服务中心接收到无法识别的消息，type为" + request.get("type") + "---");
        }
    }

    private Object invoke(Class<?> clazz, String methodName, List<Object> parameters) throws FailToInvokeException {
        //利用java的反射，执行本地函数
        try {
            Method[] ms = clazz.getDeclaredMethods();
            Method method = null;
            for (Method m : ms) {
                if (m.getName().equals(methodName)) {
                    method = m;
                    break;
                }
            }
            if (method == null) {
                throw new NoSuchMethodException();
            }

            method.setAccessible(true);

            Object[] params = new Object[parameters.size()];
            int i = 0;
            for (Object entry : parameters) {
                params[i++] = entry;
            }

            //创建实例
            Object instance = clazz.getDeclaredConstructor().newInstance();

            //执行方法
            Object result = method.invoke(instance, params);

            return result;

        } catch (Exception e) {
            throw new FailToInvokeException(e);
        }

    }
}
