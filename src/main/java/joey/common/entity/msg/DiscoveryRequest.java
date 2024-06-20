package joey.common.entity.msg;

public class DiscoveryRequest {
    private String serviceName;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public DiscoveryRequest(String serviceName) {
        this.serviceName = serviceName;
    }
}
