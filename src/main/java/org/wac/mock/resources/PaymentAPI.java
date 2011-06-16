package org.wac.mock.resources;

import org.wac.mock.mockdb.AccessToken;
import org.wac.mock.mockdb.Storage;
import org.wac.mock.mockdb.TransactionStatus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Random;

/**
 * User: Anne Marte Hjemås
 * Date: 27.05.11
 * Time: 16:49
 */
@Path("/1/payment/acr:Authorization")
public class PaymentAPI {
    Random r = new Random();

    @POST
    @Path("/transactions/amount")
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response chargeAmount(@FormParam("code") String code,
                                 @HeaderParam("Authorization") String auth){
        String token = auth.split(" ")[1];
        AccessToken at = Storage.getInstance().getAccessToken(token);
        if(at==null){
            return Response.status(Response.Status.BAD_REQUEST).entity("errorMessage: invalid access token!").build();
        }
        if(at.getScope().contains(code)){
            if(Storage.getInstance().getAccessToken(at.getAccessToken()).isCharged()){
                return Response.status(Response.Status.BAD_REQUEST).entity("errorMessage: this has already been charged").build();
            }
            String id = "wac-"+String.valueOf(Math.abs(r.nextLong()));
            TransactionStatus status = new TransactionStatus();
            status.setId(id);
            status.setStatus("Charged");
            System.out.println("Charged!");
            Storage.getInstance().getAccessToken(at.getAccessToken()).putTransaction(id, status);
            //can only charge once.
            Storage.getInstance().getAccessToken(at.getAccessToken()).setCharged(true);
            return Response.ok("code =" + id).build();
        }else{
            //or redirect to get access token..
            return Response.status(Response.Status.BAD_REQUEST).entity("errorMessage: no authorization for this item").build();
        }
    }

    @GET
    @Path("/transactions/amount/{id}")
    @Produces({"text/plain"})
    public Response transactions(@HeaderParam("Authorization") String auth,
                                 @PathParam("id") String id){

        String token = auth.split(" ")[1];
        AccessToken at = Storage.getInstance().getAccessToken(token);
        if(at==null){
            return Response.status(Response.Status.BAD_REQUEST).entity("errorMessage: invalid access token!").build();
        }
        String status = at.getTransaction(id).getStatus();
        return Response.ok("Status = " + status).build();
    }

    @GET
    @Path("/transactions/amount")
    @Produces({"text/plain"})
    public Response transactions(@HeaderParam("Authorization") String auth){
        String token = auth.split(" ")[1];
        AccessToken at = Storage.getInstance().getAccessToken(token);
        Collection<TransactionStatus> transactions = at.getTransactions();
        String jsonReturn="";
        for(TransactionStatus ts : transactions){
            jsonReturn +="\"paymentAmount\": {"+
                    "\"chargingInformation\": {"+
                    "\"code\": \""+ts.getId()+"\""+
                    "},"+
                    "},"+
                    "\"transactionOperationStatus\": \""+ts.getStatus()+"\"";
        }
        return Response.ok(jsonReturn).build();
    }
}
