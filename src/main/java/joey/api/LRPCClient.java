package joey.api;

import joey.common.entity.NetworkEndpoint;
import joey.common.exception.MysocketException;
import joey.common.exception.NoSuchServiceException;
import joey.common.exception.TimeoutException;
import joey.operator.ClientOperator;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public void init(String registerIp,Integer registerPort,Long timeout)throws IllegalArgumentException{
        //注入
        this.registerEndpoint = new NetworkEndpoint(registerIp,registerPort);
        this.requestTimeout = timeout;

        //判断地址是否有效
        if(!this.registerEndpoint.isValid()){
            throw new IllegalArgumentException("---注册中心ip地址/端口无效");
        }else if(this.requestTimeout<0){
            throw new IllegalArgumentException("---超时时间设置无效---");
        }else{
            System.out.println("---成功加载配置---");
        }

    }

    //调用方法，返回结果
    public Object invokeMethod(String className, String methodName, List<Object> parameterList)throws NoSuchServiceException,TimeoutException,IllegalArgumentException, MysocketException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Object> result = new AtomicReference<>(); // 用于存储返回结果
        AtomicBoolean isTimedOut = new AtomicBoolean(false); // 用于标记是否超时

        Thread worker = new Thread(() -> {
            try{
                ClientOperator clientOperator = new ClientOperator();
                NetworkEndpoint serverEndpoint = clientOperator.getServerEndpoint(registerEndpoint, className+"."+methodName);

                if (serverEndpoint != null) {
                    //此处可能抛出参数错误异常
                    result.set(clientOperator.invokeMethod(serverEndpoint, className +"."+ methodName, parameterList));
                } else {
                    throw new NoSuchServiceException("---未找到该服务---");
                }
            }finally {
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
            throw new TimeoutException("---请求超时---");
        }

        return result.get(); // 返回结果
    }
}
