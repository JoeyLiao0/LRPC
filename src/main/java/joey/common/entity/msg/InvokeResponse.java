package joey.common.entity.msg;

import joey.common.entity.RpcExceptionCode;

public class InvokeResponse {
    private final Object result;

    private final RpcExceptionCode rpcExceptionCode;

    public Object getResult() {
        return result;
    }

    public RpcExceptionCode getRpcExceptionCode() {
        return rpcExceptionCode;
    }

    public InvokeResponse(Object result, RpcExceptionCode rpcExceptionCode) {
        this.result = result;
        this.rpcExceptionCode = rpcExceptionCode;
    }
}
