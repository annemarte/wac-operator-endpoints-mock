package org.wac.jmx;

import org.junit.BeforeClass;
import org.junit.Test;
import org.wac.mock.jmx.StorageService;

import javax.management.*;
import java.lang.management.ManagementFactory;

import static junit.framework.Assert.assertEquals;

/**
 * User: Anne Marte Hjemås
 * Date: 18.05.11
 * Time: 10:45
 */
public class StorageMBeanTest {
    static StorageService storageService;

    @BeforeClass
    public static void setup() throws MBeanRegistrationException, InstanceAlreadyExistsException, NotCompliantMBeanException, MalformedObjectNameException {
        storageService = new StorageService();
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("org.wac.mock.jmx:type=StorageServiceMBean");
        mbs.registerMBean(storageService, name);
        imitateActivity();
    }

    @Test
    public void getNumberOfActiveAccessTokens(){

        int num = storageService.getNumberOfValidAccessTokens();
        assertEquals(0, num);
    }

    private static void imitateActivity() {
        while(true) {
            try {

                Thread.sleep(1000);
            }
            catch(InterruptedException e) { }
        }
    }

}
