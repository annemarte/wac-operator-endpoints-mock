package org.wac.mock.mockdb;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Anne Marte Hjemås
 * Date: 04.05.11
 * Time: 10:00
 */
public class Storage {
    private static Storage singletonObject;
    private static Map<String, Consumer> consumers = new HashMap<String, Consumer>();
    static {
        Consumer test = new Consumer();
        test.setClientId("123456");
        test.setName("MyConsumer");
        test.setDescription("Test consumer");
        test.setScope("all");
        test.setSecret("waccawacca1234");
        consumers.put("123456", test);
    }

    //keeps accesstokens on the go
    private static Map<String, AccessToken> unAuthorized = new HashMap<String, AccessToken>();

    //ready to use!
    private static Map<String, AccessToken> accessTokens = new HashMap<String, AccessToken>();

    /** A private Constructor prevents any other class from instantiating. */
    private Storage() {
        //	 Optional Code
    }
    public static synchronized Storage getInstance() {
        if (singletonObject == null) {
            singletonObject = new Storage();
        }
        return singletonObject;
    }

    public void putConsumer(String clientId, Consumer sessionInfo){
        consumers.put(clientId, sessionInfo);
    }

    public Consumer getConsumer(String clientId){
        return consumers.get(clientId);
    }

    public void deleteConsumer(String clientId){
        consumers.remove(clientId);
    }
    public void putAccessToken(String code, AccessToken sessionInfo){
        accessTokens.put(code, sessionInfo);
    }

    public AccessToken getAccessToken(String code){
        return accessTokens.get(code);
    }
    public void deleteAccessToken(String code){
        accessTokens.remove(code);
    }

    public void putTokenSession(String code, AccessToken sessionInfo){
        unAuthorized.put(code, sessionInfo);
    }

    public AccessToken getTokenSession(String code){
        return unAuthorized.get(code);
    }

    public void deleteTokenSession(String code){
        unAuthorized.remove(code);
    }


    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    //todo clean up tokens based on timestamp


}