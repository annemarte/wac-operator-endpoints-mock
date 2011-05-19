package org.wac.mock.jmx;

/**
 * User: Anne Marte Hjemås
 * Date: 18.05.11
 * Time: 16:59
 */
public interface NotificationServiceMXBean {

    public void setCurrentEvent(String what);
    public String getCurrentEvent();

}
