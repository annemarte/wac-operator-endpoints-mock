package org.wac.mock.resources;

import org.wac.mock.mockdb.AccessToken;
import org.wac.mock.mockdb.Storage;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * User: Anne Marte Hjemås
 * Date: 03.05.11
 * Time: 13:38
 */
@Path("/operator/legacy")
public class OperatorLogin {
    // Allows to insert contextual objects into the class,
    // e.g. ServletContext, Request, Response, UriInfo
    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    //operator specific authorization page
    String authoriziationPage = "http://localhost:8080/jsp/authorizationPage.jsp";

    @POST
    @Path("/authenticate")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticate(@FormParam("code") String code,
                                 @FormParam("username") String username,
                                 @FormParam("password") String password ) throws IOException, URISyntaxException {
        //mock remote call to some internal service
        System.out.println("Logging in");
        if(validateLogin(username, password)){
            return Response.status(302).header("location",new URI(authoriziationPage+"?code="+code)).build();

        }else{
            return Response.status(Response.Status.BAD_REQUEST).entity("errorMessage:Username or password is incorrect").build();
        }
    }

    @POST
    @Path("/authorize")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authorize(@FormParam("code") String code,
                              @FormParam("authorize") String authorize) throws URISyntaxException {
        //mock remote call to some internal service
        System.out.println("Authorizing");
        if(authorize.equals("Yes")){
            AccessToken token = Storage.getInstance().getTokenSession(code);
            token.setAuthorizedScope(token.getScope());
            token.setTimestamp(System.currentTimeMillis());
            token.setExpiresIn(600000);
             return Response.status(302).header("location", new URI(token.getRedirectUri()+"?code="+code)).build();
        }else{
            return Response.status(Response.Status.UNAUTHORIZED).build();  //check if this error code is correct
        }
    }

    private boolean validateLogin(String username, String password) {
        if(username!=null && password!=null)
            return true;//)
        else
            return false;
    }
}
