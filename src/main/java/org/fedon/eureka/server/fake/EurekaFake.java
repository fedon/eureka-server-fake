package org.fedon.eureka.server.fake;

import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fedon.eureka.server.api.Eureka;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;

/**
 * This is Eureka v2 fake for load/scale tests.
 * 
 * @author Dmytro Fedonin
 * 
 */
@Path("eureka/v2")
public class EurekaFake implements Eureka {
    private final Log log = LogFactory.getLog(getClass());
    static Applications apps = new Applications();

    @Override
    public Void addInstance(String appId, InstanceInfo instance) {
        log.info(appId + " -- " + instance.getAppName() + " -- " + instance.getVIPAddress() + " -- " + instance.getStatus().name());
        // TODO validate?
        // apps
        Application app = apps.getRegisteredApplications(instance.getAppName());
        if (app == null) {
            log.info("app == null");
            app = new Application(instance.getAppName());
            app.addInstance(instance);
            apps.addApplication(app);
        } else {
            app.addInstance(instance);
        }
        log.info("new size: " + apps.getRegisteredApplications().size());
        return null;
    }

    @Override
    public Void removeInstance(String appId, String instanceId) {
        log.info(appId + " -- " + instanceId);
        // apps
        apps.getRegisteredApplications(appId).removeInstance(apps.getRegisteredApplications(appId).getByInstanceId(instanceId));

        return null;
    }

    @Override
    public String heartbeat(String appId, String instanceId) {
        log.info(appId + " -- " + instanceId);
        // TODO implement
        Application app = apps.getRegisteredApplications(appId);
        if (app == null) {
            log.info(appId + " -- NOT_FOUND");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        InstanceInfo instance = app.getByInstanceId(instanceId);
        if (instance == null) {
            log.info(instanceId + " -- NOT_FOUND");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return "ok";
    }

    @Override
    public Applications listAll() {
        log.info("all");
        log.info("a size: " + apps.getRegisteredApplications().size());
        return apps;
    }

    @Override
    public Application listByApp(String appId) {
        log.info(appId + " -- ");
        log.info("i size: " + apps.getRegisteredApplications(appId).getInstances().size());
        return apps.getRegisteredApplications(appId);
    }

    @Override
    public InstanceInfo findInstance(String appId, String instanceId) {
        log.info(appId + " -- " + instanceId);
        return apps.getRegisteredApplications(appId).getByInstanceId(instanceId);
    }

    @Override
    public Void updateInstance(String appId, String instanceId, String operation) {
        // TODO update on base of operation
        if (Eureka.out.equals(operation)) {
            log.debug("operation: " + Eureka.out);
        }
        if (Eureka.back.equals(operation)) {
            log.debug("operation: " + Eureka.back);
        }
        return null;
    }

    @Override
    public Void updateMeta(String appId, String instanceId, String key) {
        // TODO implement
        return null;
    }

    @Override
    public List<InstanceInfo> listByVIP(String vip) {
        log.info(vip + " -- ");
        log.info("v size: " + apps.getInstancesByVirtualHostName(vip).size());
        return apps.getInstancesByVirtualHostName(vip);
    }

    @Override
    public List<InstanceInfo> listBySVIP(String svip) {
        log.info(svip + " -s- ");
        log.info("sv size: " + apps.getInstancesBySecureVirtualHostName(svip).size());
        return apps.getInstancesBySecureVirtualHostName(svip);
    }
}
