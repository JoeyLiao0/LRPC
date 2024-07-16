import joey.api.LRPCRegister;

public class Register {
    public static void main(String[] args) {
        LRPCRegister lrpcRegister = new LRPCRegister();
        lrpcRegister.init("172.27.115.7",1234,10);
        lrpcRegister.start();
    }
}
