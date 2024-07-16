package joey.common.exception;

public class TimeoutException extends RuntimeException{

    // 无参构造函数
    public TimeoutException() {
        super();
    }

    // 接受错误消息的构造函数
    public TimeoutException(String message) {
        super(message);
    }

    // 接受原始异常（原因）的构造函数
    public TimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    // 接受原始异常的构造函数
    public TimeoutException(Throwable cause) {
        super(cause);
    }
}
