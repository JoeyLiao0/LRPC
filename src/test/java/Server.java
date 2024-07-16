import joey.api.LRPCServer;

public class Server {
    public static void main(String[] args) {
        LRPCServer lrpcServer = new LRPCServer();
        String registerIp = "localhost";
        Integer registerPort = 1234;
        Integer threadNum = 10;
        String scanDir = "Name.Service";
        lrpcServer.init(registerIp,registerPort,threadNum,scanDir);
        lrpcServer.start();
    }
}
