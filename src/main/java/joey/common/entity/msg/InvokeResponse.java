package joey.common.entity.msg;

public class InvokeResponse {
    private Object result;


    private Boolean isInvoked;

    public Boolean getInvoked() {
        return isInvoked;
    }

    public void setInvoked(Boolean invoked) {
        isInvoked = invoked;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }


    public InvokeResponse(Object result, Boolean isInvoked) {
        this.result = result;
        this.isInvoked = isInvoked;
    }
}
