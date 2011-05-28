package net.wac.mock.networkapi;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * User: Anne Marte Hjemås
 * Date: 27.05.11
 * Time: 16:49
 */

@Path("/2/oauth")
public interface OAuth2_0 {

   @GET
   @Path("/authorize")
   public Response authorize();

}