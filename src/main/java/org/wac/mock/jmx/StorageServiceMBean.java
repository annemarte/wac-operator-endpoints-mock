package org.wac.mock.jmx;

/**
 * User: Anne Marte Hjemås
 * Date: 18.05.11
 * Time: 09:43
 */
public interface StorageServiceMBean {

    public int getNumberOfValidAccessTokens();

    public void revokeAllAccessTokens();
}
