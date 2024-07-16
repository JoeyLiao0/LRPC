package joey.common.entity.msg;

public class DiscoveryRequest {
    private final String serviceName;

    public String getServiceName() {
        return serviceName;
    }

    public DiscoveryRequest(String serviceName) {
        this.serviceName = serviceName;
    }
}
