package joey.common.entity;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class NetworkEndpoint {

    //ip地址
    private final String ip;

    //端口
    private final Integer port;

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NetworkEndpoint that = (NetworkEndpoint) o;
        return Objects.equals(ip, that.ip) && Objects.equals(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }

    @Override
    public String toString() {
        return "NetworkEndpoint[addr="+ip+",port"+port.toString()+"]";
    }
}
