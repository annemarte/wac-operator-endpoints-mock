package itest.org.wac.client.oauth2;


import junit.framework.Assert;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.util.URIUtil;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * User: Anne Marte Hjemås
 * Date: 27.05.11
 * Time: 16:52
 *
 * Integration test. Server must be running
 */
public class Oauth2_0_IT {
    private static final String LOCALHOST = "http://localhost:8080/rest/";
    private static final String CLIENT_ID = "123456";
    private static final String CLIENT_SECRET = "waccawacca1234";
    private static final String CLIENT_REDIRECT_URL="http://localhost:8080/jsp/clientsPage.jsp";

    //expected operator results
    private static final String AUTHORIZATION_PAGE = "http://localhost:8080/jsp/authorizationPage.jsp";


    @Test
    public void testOauth2AuthorizationTokenFlow() throws Exception {
        String accessToken;
        //get oauth code
        String code = authorizeInit();
        //operator login
        postAuthentication(code);
        //operator authorize
        postAuthorization(code);
        //get access token
        accessToken = postAccessToken(code);

        String transactionId = postPayment(accessToken);

        String status = getStatus(accessToken, transactionId);

        assertEquals("Charged", status);

        String statusList = listStatus(accessToken);
        assertNotNull(statusList);
    }


    private String authorizeInit() throws Exception {
        HttpClient client = new HttpClient();
        HttpMethod authorizeMethod = new GetMethod(LOCALHOST+"2/oauth/authorize");
        try{

            String resourceUri = "(/payment/acr:Authorization/transactions/amount";
            String scope = "POST-"+resourceUri+"?code=wac-123";
            //add parameters
            NameValuePair param1 = new NameValuePair("client_id", URIUtil.encodeQuery(CLIENT_ID));
            NameValuePair param2 = new NameValuePair("redirect_uri", URIUtil.encodeQuery(CLIENT_REDIRECT_URL));
            NameValuePair param3 = new NameValuePair("scope", URIUtil.encodeQuery(scope));
            NameValuePair param4 = new NameValuePair("response_type", URIUtil.encodeQuery("code"));
            NameValuePair[] params = new NameValuePair[] {param1, param2, param3, param4};

            authorizeMethod.setQueryString(params);
            client.executeMethod(authorizeMethod);

            //should have been redirected to authentication page
            Assert.assertEquals(HttpStatus.SC_OK, authorizeMethod.getStatusCode());
            String response = authorizeMethod.getResponseBodyAsString();

            assertNotNull(response);
            //content of login page
            System.out.println("Response = " + response);

            String code = authorizeMethod.getQueryString();
            return code;
        }finally{
            authorizeMethod.releaseConnection();
        }
    }
    /**
     * Trigger operator authorization endpoint to initiate authentication and authorization
     * @throws Exception
     */

    private void postAuthentication(String code) throws IOException {
        HttpClient client = new HttpClient();
        //should be encoded or over https
        String data = code+"&username=bob&password=builder";
        PostMethod postAuthentication = new PostMethod(LOCALHOST+"operator/legacy/authenticate");
        postAuthentication.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");

        RequestEntity re = null;
        try {
            re = new StringRequestEntity(data, null, null);
            postAuthentication.setRequestEntity(re);
            postAuthentication.setContentChunked(true);

            client.executeMethod(postAuthentication);
            //redirect page
            //should get a redirect to authorization page
            //content of login page
            System.out.println("Response = " + postAuthentication.getResponseBodyAsString());
            Assert.assertEquals(HttpStatus.SC_MOVED_TEMPORARILY, postAuthentication.getStatusCode());
            String location = postAuthentication.getResponseHeader("location").getValue();
            Assert.assertEquals(AUTHORIZATION_PAGE, location);

        }finally {
            postAuthentication.releaseConnection();
        }
    }

    private void postAuthorization(String code) throws IOException {
        HttpClient client = new HttpClient();
        //should be encoded or over https
        String data = code+"&authorize=yes";
        PostMethod postAuthorize = new PostMethod(LOCALHOST+"operator/legacy/authorize");
        postAuthorize.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");

        RequestEntity re;
        try {
            re = new StringRequestEntity(data, null, null);
            postAuthorize.setRequestEntity(re);
            postAuthorize.setContentChunked(true);

            client.executeMethod(postAuthorize);
            //redirect page
            //should get a redirect to authorization page
            Assert.assertEquals(HttpStatus.SC_MOVED_TEMPORARILY, postAuthorize.getStatusCode());
            String location = postAuthorize.getResponseHeader("location").getValue();
            Assert.assertEquals(CLIENT_REDIRECT_URL+"?"+code, location);
        }finally {
            postAuthorize.releaseConnection();
        }
    }

    private String postAccessToken(String code) throws IOException {
        HttpClient client = new HttpClient();
        //should be encoded or over https
        String data = code+"&client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&redirect_uri="+CLIENT_REDIRECT_URL+"&grant_type=authorization_code";
        PostMethod postAccessToken= new PostMethod(LOCALHOST+"2/oauth/access-token");
        postAccessToken.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        postAccessToken.addRequestHeader("Connection", "keep-alive");
        RequestEntity re;
        try {
            re = new StringRequestEntity(data, null, null);
            postAccessToken.setRequestEntity(re);
            postAccessToken.setContentChunked(true);

            client.executeMethod(postAccessToken);

            Assert.assertEquals(HttpStatus.SC_OK, postAccessToken.getStatusCode());
            String resp=postAccessToken.getResponseBodyAsString();
            System.out.println("Response = " + resp);
            String[] params = resp.split("&");
            //making wild assumptions ;)
            return params[0].split("=")[1];
        }finally {
            postAccessToken.releaseConnection();
        }

    }

    private String postPayment(String accessToken) throws IOException {
        String data="endUserId= acr:Authorization&"+
                "transactionOperationStatus=charged&"+
                "description= Single%20general%20admission%20theater%20ticket&"+
                "code=wac-123&"+
                "referenceCode=REF-ASM600-239238&"+
                "onBehalfOf=WAC%20Cinemas%20Inc&"+
                "purchaseCategoryCode=Ticket&"+
                "channel=WAP";

        HttpClient client = new HttpClient();
        //should be encoded or over https
        PostMethod postPayment= new PostMethod(LOCALHOST+"1/payment/acr:Authorization/transactions/amount");
        postPayment.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        postPayment.addRequestHeader("Content-Length", String.valueOf(data.length()));
        postPayment.addRequestHeader("Authorization", "OAuth2 "+accessToken);
        RequestEntity re;

        re = new StringRequestEntity(data, null, null);
        postPayment.setRequestEntity(re);
        postPayment.setContentChunked(true);
        try{
            client.executeMethod(postPayment);

            Assert.assertEquals(HttpStatus.SC_OK, postPayment.getStatusCode());
            String resp=postPayment.getResponseBodyAsString();
            System.out.println("Response = " + resp);
            return resp.split("=")[1];//hairy
        }finally {
            postPayment.releaseConnection();
        }
    }

    private String getStatus(String accessToken, String transactionId) throws IOException {
        HttpClient client = new HttpClient();
        HttpMethod paymentStatusMethod = new GetMethod(LOCALHOST+"1/payment/acr:Authorization/transactions/amount/"+transactionId+"/");
        try{

            paymentStatusMethod.addRequestHeader("Authorization", "OAuth2 "+accessToken);
            client.executeMethod(paymentStatusMethod);

            //should have been redirected to authentication page
            Assert.assertEquals(HttpStatus.SC_OK, paymentStatusMethod.getStatusCode());
            String response = paymentStatusMethod.getResponseBodyAsString();

            assertNotNull(response);
            //content of login page
            System.out.println("Response = " + response);
            return response.split("=")[1].trim();
        }finally{
            paymentStatusMethod.releaseConnection();
        }
    }


    private String listStatus(String accessToken) throws IOException {
        HttpClient client = new HttpClient();
        HttpMethod paymentStatusListMethod = new GetMethod(LOCALHOST+"1/payment/acr:Authorization/transactions/amount/");
        try{

            paymentStatusListMethod.addRequestHeader("Authorization", "OAuth2 " + accessToken);
            client.executeMethod(paymentStatusListMethod);

            //should have been redirected to authentication page
            Assert.assertEquals(HttpStatus.SC_OK, paymentStatusListMethod.getStatusCode());
            String response = paymentStatusListMethod.getResponseBodyAsString();

            assertNotNull(response);
            //content of login page
            System.out.println("Response = " + response);
            return response;
        }finally{
            paymentStatusListMethod.releaseConnection();
        }
    }
}
