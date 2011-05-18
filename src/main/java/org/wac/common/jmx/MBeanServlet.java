package org.wac.common.jmx;

import org.wac.mock.jmx.StorageService;

import javax.management.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.lang.management.ManagementFactory;

/**
 * User: Anne Marte Hjemås
 * Date: 18.05.11
 * Time: 10:52
 */
public class MBeanServlet extends HttpServlet {
    StorageService storageService;
    public void init() throws ServletException {
        super.init();
        storageService = new StorageService();
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name;
        try {
            name = new ObjectName("org.wac.mock.jmx:type=StorageServiceMBean");
            mbs.registerMBean(storageService, name);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (NotCompliantMBeanException e) {
            e.printStackTrace();
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        } catch (MBeanRegistrationException e) {
            e.printStackTrace();
        }

    }


}
