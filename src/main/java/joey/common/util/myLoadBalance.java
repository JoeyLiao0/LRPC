package joey.common.util;

import joey.common.entity.NetworkEndpoint;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class myLoadBalance {
    public static NetworkEndpoint getServerEndpoint(Set<NetworkEndpoint> s){
        //负载均衡策略
        if(s==null || s.isEmpty()){
            return null;
        }
        Random random = new Random();
        Iterator<NetworkEndpoint> iterator = s.iterator();
        int r =random.nextInt(s.size());
        for(int i=0;i<r;i++){
            if(iterator.hasNext())iterator.next();
        }
        return iterator.next();
    }
}
