package org.wac.mock.jmx;

import org.wac.mock.mockdb.Consumer;

/**
 * User: Anne Marte Hjemås
 * Date: 18.05.11
 * Time: 15:06
 */
//@Author("Anne Marte Hjemås")
//@Version("1.0")
public interface ConsumerServiceMXBean {

    public String[] listConsumerKeys();
    public Consumer getConsumer(String clientKey);
    public void setConsumer(Consumer consumer);
    public int getNumberOfConsumers();
}
