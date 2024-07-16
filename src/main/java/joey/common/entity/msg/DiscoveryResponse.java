package joey.common.entity.msg;

import joey.common.entity.NetworkEndpoint;
import joey.common.entity.RpcExceptionCode;

import java.util.Set;

public class DiscoveryResponse {
    private final Set<NetworkEndpoint> serverEndpointList;
    private final RpcExceptionCode rpcExceptionCode;

    public Set<NetworkEndpoint> getServerEndpointList() {
        return serverEndpointList;
    }

    public RpcExceptionCode getRpcExceptionCode() {
        return rpcExceptionCode;
    }

    public DiscoveryResponse(Set<NetworkEndpoint> serverEndpointList, RpcExceptionCode rpcExceptionCode) {
        this.serverEndpointList = serverEndpointList;
        this.rpcExceptionCode = rpcExceptionCode;
    }
}
