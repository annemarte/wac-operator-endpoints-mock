package itest.org.wac.client.oauth2;


import junit.framework.Assert;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.util.URIUtil;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

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

    // private static final String PROXY_HEADER = "http.route.default-proxy";
    private static final String LOCALHOST = "http://localhost:8080/rest/";
    private static final String CLIENT_ID = "782378hjgfjrertre92393";
    private static final String CLIENT_SECRET = "waccawacca1234";
    private static final String CLIENT_REDIRECT_URL="http://localhost:8080/jsp/clientsPage.jsp";

    //expected operator results
    private static final String AUTHORIZATION_PAGE = "http://localhost:8080/jsp/authorizationPage.jsp";


    @Test
    public void testOauth2AuthorizationTokenFlow() throws Exception{

        String accessToken;
        //get oauth code
        System.out.println("authorize");
        String code = authorize();
        //get access token
        System.out.println("get access token");
        accessToken = getAccessToken(code);
        // perform payment
        System.out.println("payment++");
        String transactionId = payment(accessToken);
        //check status
        String status = getStatus(accessToken, transactionId);
        assertEquals("Charged", status);
        //check status list
        String statusList = listStatus(accessToken);
        assertNotNull(statusList);
        System.out.println("success!");
    }

    private HttpClient getHttpClient(){
        HttpClient client = new HttpClient();
        HostConfiguration config = client.getHostConfiguration();
        config.setProxy("localhost", 9797);
        return client;
    }

    private String authorize () throws Exception {
        HtmlUnitDriver driver = new HtmlUnitDriver();//new FirefoxDriver(); ;
        System.out.println("got driver");
        // driver.setProxy("localhost", 9797);
        //driver.manage().setSpeed(Speed.MEDIUM);
        //parameters
        String resourceUri = "(/payment/acr:Authorization/transactions/amount";
        String client_id = URIUtil.encodeQuery(CLIENT_ID);
        String redirect_uri =URIUtil.encodeQuery(CLIENT_REDIRECT_URL);
        String scope= URIUtil.encodeQuery("POST-"+resourceUri+"?code=wac-123");
        String response_type = URIUtil.encodeQuery("code");
        String queryString = "client_id="+client_id+"&redirect_uri="+redirect_uri+"&scope="+scope+"&response_type="+response_type;
        String authorizeRequestString = LOCALHOST+"2/oauth/authorize?"+queryString;
        driver.get(authorizeRequestString);
        System.out.println("got login page");
        driver.findElement(By.id("j_username")).sendKeys("bob");
        WebElement element = driver.findElement(By.id("j_password"));
        element.sendKeys("builder");
        element.submit();
        boolean startsWith = driver.getCurrentUrl().startsWith(AUTHORIZATION_PAGE);
        assert(startsWith);
        // Sleep until the div we want is visible or 5 seconds is over
        Thread.sleep(10);

        //authorize
        driver.findElement(By.id("b_yes")).click();
        Thread.sleep(10);
        String clientUrl = driver.getCurrentUrl();
        String code=clientUrl.split("=")[1];
        assertNotNull(code);
        assertEquals(CLIENT_REDIRECT_URL + "?code=" + code, clientUrl);
        return code;
    }

    /*
     * Trigger operator authorization endpoint to initiate authentication and authorization
     * @param code authorization code
     * @throws Exception
     * @return  accesstoken
     */
    private String getAccessToken(String code) throws IOException {
        HttpClient client = getHttpClient();
        //should be encoded or over https
        String data = "code="+code+"&client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&redirect_uri="+CLIENT_REDIRECT_URL+"&grant_type=authorization_code";
        PostMethod postAccessToken= new PostMethod(LOCALHOST+"2/oauth/access-token");
        postAccessToken.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        postAccessToken.addRequestHeader("Connection", "keep-alive");
        RequestEntity re;
        try {
            re = new StringRequestEntity(data, null, null);
            postAccessToken.setRequestEntity(re);
            postAccessToken.setContentChunked(false);

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

    private String payment(String accessToken) throws IOException {
        HttpClient client = getHttpClient();
        String data="endUserId= acr:Authorization&"+
                "transactionOperationStatus=charged&"+
                "description= Single%20general%20admission%20theater%20ticket&"+
                "code=wac-123&"+
                "referenceCode=REF-ASM600-239238&"+
                "onBehalfOf=WAC%20Cinemas%20Inc&"+
                "purchaseCategoryCode=Ticket&"+
                "channel=WAP";


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
        HttpClient client = getHttpClient();
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
        HttpClient client = getHttpClient();
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
