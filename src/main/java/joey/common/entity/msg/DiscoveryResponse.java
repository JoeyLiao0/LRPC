package joey.common.entity.msg;

import joey.common.entity.NetworkEndpoint;

import java.util.Set;

public class DiscoveryResponse {
    private Set<NetworkEndpoint> serverEndpointList;

    public Set<NetworkEndpoint> getServerEndpointList() {
        return serverEndpointList;
    }

    public void setServerEndpointList(Set<NetworkEndpoint> serverEndpointList) {
        this.serverEndpointList = serverEndpointList;
    }

    public DiscoveryResponse(Set<NetworkEndpoint> serverEndpointList) {
        this.serverEndpointList = serverEndpointList;
    }
}
