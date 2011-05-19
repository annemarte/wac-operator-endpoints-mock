package org.wac.mock.mockdb;

import java.io.Serializable;

/**
 * User: Anne Marte Hjemås
 * Date: 11.05.11
 * Time: 17:28
 */
public class TransactionStatus implements Serializable{
    private static final long serialVersionUID = 357562534497348251L;
    String id;
    String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
