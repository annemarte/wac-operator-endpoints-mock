package org.wac.mock.jmx;

/**
 * User: Anne Marte Hjemås
 * Date: 15.06.11
 * Time: 11:29
 */
public interface SessionTokenServiceMXBean {
    public int getNumberOfSessionTokens();
    public void revokeAllSessionTokens();
}
