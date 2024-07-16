package joey.common.entity.msg;

import joey.common.entity.RpcExceptionCode;

public class RegisterResponse {

    private final RpcExceptionCode rpcExceptionCode;

    public RpcExceptionCode getRpcExceptionCode() {
        return rpcExceptionCode;
    }

    public RegisterResponse(RpcExceptionCode rpcExceptionCode) {
        this.rpcExceptionCode = rpcExceptionCode;
    }

}
