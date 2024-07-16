package joey.common.entity;

public enum RpcExceptionCode {
    SUCCESS(0, "成功"),
    INVOKE_FAILURE(1001, "函数执行失败"),
    NO_SUCH_SERVICE(1002, "服务未找到");
    private final int code;
    private final String description;

    RpcExceptionCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
