package org.wac.mock.mockdb;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Anne Marte Hjemås
 * Date: 11.05.11
 * Time: 10:19
 */
public class AccessToken implements Serializable{

    private static final long serialVersionUID = -882877653402984139L;

    Consumer consumer;
    String scope;
    String authorizedScope;
    String accessToken;
    String responseType;
    String redirectUri;
    long timestamp;
    long expiresIn;
    boolean charged;


    public boolean isCharged() {
        return charged;
    }

    public void setCharged(boolean charged) {
        this.charged = charged;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getAuthorizedScope() {
        return authorizedScope;
    }

    public void setAuthorizedScope(String authorizedScope) {
        this.authorizedScope = authorizedScope;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

     public Map<String, TransactionStatus > transactions = new HashMap<String, TransactionStatus>();

    public void putTransaction(String code, TransactionStatus transactionStatus){
        transactions.put(code, transactionStatus);
    }

    public TransactionStatus getTransaction(String code){
        return transactions.get(code);
    }
    public void deleteTransaction(String clientId){
        transactions.remove(clientId);
    }
     public Collection<TransactionStatus> getTransactions(){
        return transactions.values();
    }
}
