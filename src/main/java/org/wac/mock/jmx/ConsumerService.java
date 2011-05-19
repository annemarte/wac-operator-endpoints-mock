package org.wac.mock.jmx;

import org.wac.mock.mockdb.Consumer;
import org.wac.mock.mockdb.Storage;

import java.util.Set;

/**
 * User: Anne Marte Hjemås
 * Date: 18.05.11
 * Time: 15:08
 */
public class ConsumerService implements ConsumerServiceMXBean {

    @Override
    public String[] listConsumerKeys() {
        Set<String> consumerKeys = Storage.getInstance().getConsumerKeys();
        String[] result = new String[consumerKeys.size()];
        consumerKeys.toArray(result);
        return result;
    }

    @Override
    public Consumer getConsumer(String clientKey) {
        return Storage.getInstance().getConsumer(clientKey);
    }

    @Override
    public void setConsumer(Consumer consumer) {
        Storage.getInstance().putConsumer(consumer.getClientId(), consumer);
    }


}
