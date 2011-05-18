package org.wac.mock.resources;

import org.wac.mock.mockdb.AccessToken;
import org.wac.mock.mockdb.Consumer;
import org.wac.mock.mockdb.Storage;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;


/**
 * User: Anne Marte Hjemås
 * Date: 27.05.11
 * Time: 17:13
 */
@Path("/2/oauth")
public class OAuth2_0 {

    //operator specific pages
    static final String authenticatePage = "http://localhost:8080/jsp/loginPage.jsp";
    static final String authorizationPage = "http://localhost:8080/jsp/authorizationPage.jsp";

    /**
     * Here we receive a request from the client which is a redirect from the
     * WAC Gateway
     * @param clientId Required API Key associated with your application and must be the same as that you registered with WAC DWP.
     * @param redirectURI Required The URL on client application that will handle OAuth responses after the user takes an action on the authorization.
     * @param scope Required The scope of OAuth authentication.
     * WAC Network APIs platform uses scope parameter to
     * identify the resource information and authorization context.
     * Syntax of the scope parameter must follows:
     *{Operation}-{ResourceURI}?{item}
     * @param responseType "code" Required As beta scope is limited to the server-side flow
     * @return
     */
    @GET
    @Path("/authorize")
    @Produces({"text/plain"})
    public Response authorize( @QueryParam("client_id") String clientId,
                               @QueryParam("redirect_uri") String redirectURI,
                               @QueryParam("scope") String scope,
                               @QueryParam("response_type") String responseType){

        try {
            Consumer c = Storage.getInstance().getConsumer(clientId);
            if(c!=null)
                System.out.println("Validated client id " + clientId);



            if(responseType.equals("code")){
                //generate and store in mock db
                long code = Math.abs(r.nextLong());
                System.out.println("code=" + code);
                AccessToken token= new AccessToken();

                System.out.println("Redirect URI = " + redirectURI);
                token.setRedirectUri(redirectURI);
                token.setAccessToken(String.valueOf(code));
                System.out.println("Scope is = " + scope);
                token.setScope(scope);
                token.setResponseType(responseType);
                token.setConsumer(c);
                Storage.getInstance().putTokenSession(String.valueOf(code), token);
                return Response.status(302).header("location",new URI(authenticatePage+"?code="+code)).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("errorMessage:" + responseType + " not supported yet").build();
            }

        } catch (URISyntaxException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("errorMessage:Invalid URI").build();
        }
    }
    @POST
    @Path("/access-token")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response accessToken( @FormParam("client_id") String clientId,
                                 @FormParam("client_secret") String clientSecret,
                                 @FormParam("redirect_uri") String redirectURI,
                                 @FormParam("grant_type") String grantType,
                                 @FormParam("code") String code){


            AccessToken at = Storage.getInstance().getTokenSession(code);
            if(at!=null){
                System.out.println("Validated client id " + clientId);
                if(!at.getConsumer().getSecret().equals(clientSecret)){
                      return Response.status(Response.Status.BAD_REQUEST).entity("errorMessage: wrong client secret").build();
                }
            }

            if(grantType.equals("authorization_code")){
                //generate access token and store in mock db
                String accessToken = String.valueOf(Math.abs(r.nextLong()));
                at.setAccessToken(accessToken);
                Storage.getInstance().putAccessToken(accessToken,at);
                Storage.getInstance().deleteTokenSession(code);
                return Response.ok("access_token="+accessToken+"&expires_in="+at.getExpiresIn()+"&token_type=OAuth2.0").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("errorMessage:"+grantType+ " not supported yet").build();
            }

    }
    Random r = new Random(12345683);
    /**
     * This is really the wac endpoint.Proxy session token is wac-specific
     */
    @GET
    @Path("/back/{proxy_session_token}")
    public Response redirectToOperatorEndpoint( @PathParam("proxy_session_token") String sessionToken,
                                                @QueryParam("redirect_uri") String redirectURI,
                                                @QueryParam("code") String code
    ){
        //really does a lot of stuff, but we'll just say a-ok and return to the test-flow
        return Response.ok().build();
    }
}
