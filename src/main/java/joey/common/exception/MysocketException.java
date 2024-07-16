package joey.common.exception;

public class MysocketException extends RuntimeException{
    // 无参构造函数
    public MysocketException() {
        super();
    }

    // 接受错误消息的构造函数
    public MysocketException(String message) {
        super(message);
    }

    // 接受原始异常（原因）的构造函数
    public MysocketException(String message, Throwable cause) {
        super(message, cause);
    }

    // 接受原始异常的构造函数
    public MysocketException(Throwable cause) {
        super(cause);
    }
}
