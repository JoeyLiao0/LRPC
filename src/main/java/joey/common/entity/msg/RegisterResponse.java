package joey.common.entity.msg;

public class RegisterResponse {
    String msg;

    public RegisterResponse(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
