package joey.common.entity.msg;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class InvokeRequest {

    private final String serviceName;

    private final List<Object> parameter;

    public String getServiceName() {
        return serviceName;
    }

    public List<Object> getParameter() {
        return parameter;
    }

    public InvokeRequest(String serviceName, List<Object> parameter) {
        this.serviceName = serviceName;
        this.parameter = parameter;
    }
}
