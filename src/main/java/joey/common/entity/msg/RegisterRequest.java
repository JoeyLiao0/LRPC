package joey.common.entity.msg;

import joey.common.entity.NetworkEndpoint;

import java.util.Set;

public class RegisterRequest {

    private final Set<String> serviceName;

    private final NetworkEndpoint serverEndpoint;

    public RegisterRequest(Set<String> serviceName, NetworkEndpoint serverEndpoint) {
        this.serviceName = serviceName;
        this.serverEndpoint = serverEndpoint;
    }

    public Set<String> getServiceName() {
        return serviceName;
    }

    public NetworkEndpoint getServerEndpoint() {
        return serverEndpoint;
    }


}
