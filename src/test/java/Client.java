import joey.api.LRPCClient;

import javax.management.ObjectName;
import java.util.*;

public class Client {
    public static void main(String[] args) {
        LRPCClient lrpcClient = new LRPCClient();
        lrpcClient.init("localhost",1234, 1232311123133L);
        List<Object> plist = new ArrayList<>();
        plist.add(2);
        plist.add(1);
        System.out.println(lrpcClient.invokeMethod("Name.Service.Calculation","add",plist));
    }
}
