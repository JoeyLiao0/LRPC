package joey.common.entity;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkEndpoint {

    //ip地址
    private String ip;

    //端口
    private Integer port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    //初始化
    public NetworkEndpoint(String ip,Integer port){
        this.ip = ip;
        this.port = port;
    }

    //判断是否有效
    public Boolean isValid() {
        // 检查端口号是否在有效范围内
        if (this.port <= 0 || this.port > 65535) {
            return false;
        }

        try {
            // 尝试将字符串IP地址转换为InetAddress对象
            InetAddress address = InetAddress.getByName(ip);
            // 如果IP地址是有效的，InetAddress.getByName不会抛出异常
            // 这里可以进一步检查是否是IPv4或IPv6，如果需要的话
            return true;
        } catch (UnknownHostException e) {
            // 如果IP地址无效，会抛出UnknownHostException异常
            return false;
        }
    }
}
