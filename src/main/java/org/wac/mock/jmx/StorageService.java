package org.wac.mock.jmx;

import org.wac.mock.mockdb.Storage;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import java.util.ArrayList;

/**
 * User: Anne Marte Hjemås
 * Date: 18.05.11
 * Time: 09:46
 */
public class StorageService implements StorageServiceMBean {

    public StorageService() {
         MBeanServer server = getServer();

         ObjectName name = null;
         try {
           name = new ObjectName("Application:Name=StorageService,Type=Server");
           server.registerMBean(this, name);
         } catch (Exception e) {
           e.printStackTrace();
         }

       }


       private MBeanServer getServer() {
         MBeanServer mbserver = null;

         ArrayList mbservers = MBeanServerFactory.findMBeanServer(null);

         if (mbservers.size() > 0) {
           mbserver = (MBeanServer) mbservers.get(0);
         }

         if (mbserver != null) {
           System.out.println("Found our MBean server");
         } else {
           mbserver = MBeanServerFactory.createMBeanServer();
         }

         return mbserver;
       }

    @Override
    public int getNumberOfValidAccessTokens() {
       return Storage.getInstance().getNumberOfValidAccessTokens();
    }

    @Override
    public void revokeAllAccessTokens() {
        Storage.getInstance().revokeAllAccessTokens();
    }
}
