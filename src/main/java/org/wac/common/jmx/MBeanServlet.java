package org.wac.common.jmx;

import org.wac.mock.jmx.*;

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
    public void init() throws ServletException {
        super.init();

        //Services we want to manage
        AccessTokenService accessTokenService = new AccessTokenService();
        ConsumerService consumerService = new ConsumerService();
        SessionTokenService sessionTokenService = new SessionTokenService();
        NotificationService notificationService = new NotificationService();

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName name1 = new ObjectName("WAC_Application:Name=AccessTokenService,type=MockStorage");
            mbs.registerMBean(accessTokenService, name1);
            ObjectName name2 = new ObjectName("WAC_Application:Name=ConsumerService,type=MockStorage");
            mbs.registerMBean(consumerService, name2);
            ObjectName name3 = new ObjectName("WAC_Application:Name=SessionTokenService,type=MockStorage");
            mbs.registerMBean(sessionTokenService, name3);
            ObjectName name4 = new ObjectName("WAC_Application:Name=NotificationService,type=Notifications");
            mbs.registerMBean(notificationService, name4);
            mbs.addNotificationListener(name4,new PointlessNotificationListener(),null, null);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (NotCompliantMBeanException e) {
            e.printStackTrace();
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        } catch (MBeanRegistrationException e) {
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        }

    }


}
