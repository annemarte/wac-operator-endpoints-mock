package org.wac.mock.jmx;

import org.wac.mock.mockdb.Storage;

/**
 * User: Anne Marte Hjemås
 * Date: 15.06.11
 * Time: 11:30
 */
public class SessionTokenService implements SessionTokenServiceMXBean {
    @Override
    public int getNumberOfSessionTokens() {
        return Storage.getInstance().getNumberOfSessionTokens();
    }

    @Override
    public void revokeAllSessionTokens() {
        Storage.getInstance().revokeAllSessionTokens();
    }
}
