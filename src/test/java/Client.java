import joey.api.LRPCClient;

import javax.management.ObjectName;
import java.util.HashMap;
import java.util.Map;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        LRPCClient lrpcClient = new LRPCClient();
        lrpcClient.init("localhost",1234, 1232311123133L);
        Map<String,Object> pmap = new HashMap<>();
        pmap.put("a",1);
        pmap.put("b",2);
        System.out.println(lrpcClient.invokeMethod("Service.Calculation","add",pmap));
    }
}
