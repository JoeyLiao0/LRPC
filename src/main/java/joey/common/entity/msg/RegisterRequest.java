package joey.common.entity.msg;

import joey.common.entity.NetworkEndpoint;

import java.util.Set;

public class RegisterRequest {

    private Set<String> serviceName;

    private NetworkEndpoint serverEndpoint;

    public RegisterRequest(Set<String> serviceName, NetworkEndpoint serverEndpoint) {
        this.serviceName = serviceName;
        this.serverEndpoint = serverEndpoint;
    }

    public Set<String> getServiceName() {
        return serviceName;
    }

    public void setServiceName(Set<String> serviceName) {
        this.serviceName = serviceName;
    }

    public NetworkEndpoint getServerEndpoint() {
        return serverEndpoint;
    }

    public void setServerEndpoint(NetworkEndpoint serverEndpoint) {
        this.serverEndpoint = serverEndpoint;
    }
}
