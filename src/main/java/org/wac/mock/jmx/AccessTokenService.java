package org.wac.mock.jmx;

import org.wac.mock.mockdb.AccessToken;
import org.wac.mock.mockdb.Storage;

import java.util.Map;

/**
 * User: Anne Marte Hjemås
 * Date: 18.05.11
 * Time: 09:46
 */
public class AccessTokenService implements AccessTokenServiceMXBean {

    @Override
    public int getNumberOfValidAccessTokens() {
       return Storage.getInstance().getNumberOfValidAccessTokens();
    }


    @Override
    public void revokeAllAccessTokens() {
        Storage.getInstance().revokeAllAccessTokens();
    }
}
