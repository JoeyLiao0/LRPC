package joey.common.entity.msg;

import java.util.Map;

public class InvokeRequest {

    private String serviceName;

    private Map<String,Object> parameter;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Map<String, Object> getParameter() {
        return parameter;
    }

    public void setParameter(Map<String, Object> parameter) {
        this.parameter = parameter;
    }

    public InvokeRequest(String serviceName, Map<String, Object> parameter) {
        this.serviceName = serviceName;
        this.parameter = parameter;
    }
}
