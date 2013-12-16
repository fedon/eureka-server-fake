package org.fedon.eureka.server.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;

/**
 * @author Dmytro Fedonin
 *
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface Eureka {
    String out = "OUT_OF_SERVICE";
    String back = "UP";

    @POST
    @Path("apps/{appId}")
    Void addInstance(@PathParam("appId") String appId, InstanceInfo instance);

    @DELETE
    @Path("apps/{appId}/{instanceId}")
    Void removeInstance(@PathParam("appId") String appId, @PathParam("instanceId") String instanceId);

    @PUT
    @Path("apps/{appId}/{instanceId}")
    String heartbeat(@PathParam("appId") String appId, @PathParam("instanceId") String instanceId);

    @GET
    @Path("apps")
    Applications listAll();

    @GET
    @Path("apps/{appId}")
    Application listByApp(@PathParam("appId") String appId);

    @GET
    @Path("apps/{appId}/{instanceId}")
    InstanceInfo findInstance(@PathParam("appId") String appId, @PathParam("instanceId") String instanceId);

    @PUT
    @Path("apps/{appId}/{instanceId}/status")
    Void updateInstance(@PathParam("appId") String appId, @PathParam("instanceId") String instanceId, @QueryParam("value") String operation);

    @PUT
    @Path("apps/{appId}/{instanceId}/metadata")
    Void updateMeta(@PathParam("appId") String appId, @PathParam("instanceId") String instanceId, @QueryParam("key") String key);

    @GET
    @Path("vips/{vip}")
    List<InstanceInfo> listByVIP(@PathParam("vip") String vip);

    @GET
    @Path("svips/{svip}")
    List<InstanceInfo> listBySVIP(@PathParam("svip") String vip);
}
