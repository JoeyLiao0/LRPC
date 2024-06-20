package joey.api;

import joey.common.entity.NetworkEndpoint;
import joey.operator.ClientOperator;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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
    public Object invokeMethod(String className, String methodName, Map<String, Object> parameterMap) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Object> result = new AtomicReference<>(); // 用于存储返回结果
        AtomicBoolean isTimedOut = new AtomicBoolean(false); // 用于标记是否超时

        Thread worker = new Thread(() -> {
            try {
                ClientOperator clientOperator = new ClientOperator();
                NetworkEndpoint serverEndpoint = clientOperator.getServerEndpoint(registerEndpoint, className+"."+methodName);

                if (serverEndpoint != null) {
                    result.set(clientOperator.invokeMethod(serverEndpoint, className +"."+ methodName, parameterMap));
                } else {
                    throw new RuntimeException("---未找到该服务---");
                }
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            } finally {
                latch.countDown(); // 确保在所有路径中都递减计数器
            }
        });

        worker.start();

        try {
            if (!latch.await(this.requestTimeout, TimeUnit.MILLISECONDS)) {
                isTimedOut.set(true); // 设置超时标记
                worker.interrupt(); // 请求中断线程
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 重新设置中断状态
        }

        if (isTimedOut.get()) {
            // 处理超时情况，例如抛出异常或返回特定值
            throw new InterruptedException("---请求超时---");
        }

        return result.get(); // 返回结果
    }
}
