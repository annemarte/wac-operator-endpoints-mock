package org.wac.mock.jmx;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import javax.management.NotificationListener;

/**
 * User: Anne Marte Hjemås
 * Date: 18.05.11
 * Time: 17:31
 */
public class PointlessNotificationListener implements NotificationListener{
    @Override
    public void handleNotification(Notification notification, Object handback) {
        System.out.println(notification.getMessage());
        if(notification instanceof AttributeChangeNotification){
            AttributeChangeNotification acn = (AttributeChangeNotification)notification;
            System.out.println(acn.getAttributeName()+ " changed from " + acn.getOldValue() + " to " + acn.getNewValue());
        }
    }
}
