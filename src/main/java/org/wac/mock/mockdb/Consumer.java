package org.wac.mock.mockdb;

import java.beans.ConstructorProperties;
import java.io.Serializable;

/**
 * User: Anne Marte Hjemås
 * Date: 04.05.11
 * Time: 10:00
 */
public class Consumer implements Serializable{

    private static final long serialVersionUID = 7757374640139470240L;

    String clientId;
    String name;
    String description;

    String secret;
    String scope;

    String url;

    @ConstructorProperties({"clientId", "name", "description", "secret", "scope", "url"})
    public Consumer(String clientId, String name, String description, String secret, String scope, String url) {
        this.clientId = clientId;
        this.name = name;
        this.description = description;
        this.secret = secret;
        this.scope = scope;
        this.url = url;
    }

    public Consumer() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }


    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
