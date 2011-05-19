package org.wac.mock.jmx;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

/**
 * User: Anne Marte Hjemås
 * Date: 18.05.11
 * Time: 17:00
 */
public class NotificationService extends NotificationBroadcasterSupport implements NotificationServiceMXBean{
    private long sequenceNumber = 1;
    private static String oldEvent;
    private static String currentEvent ="Not much";
    @Override
    public synchronized void setCurrentEvent(String what) {
        oldEvent = currentEvent;
        currentEvent = what;
        Notification n =
                new AttributeChangeNotification(this,
                                sequenceNumber++,
                                System.currentTimeMillis(),
                                "Something happened!!",
                                "Current event",
                                "String",
                                oldEvent,
                                currentEvent);

            /* Now send the notification using the sendNotification method
               inherited from the parent class
               NotificationBroadcasterSupport.  */
            sendNotification(n);

    }

    @Override
    public String getCurrentEvent() {
        return currentEvent;
    }


    @Override
      public MBeanNotificationInfo[] getNotificationInfo() {
      String[] types = new String[] {
          AttributeChangeNotification.ATTRIBUTE_CHANGE
      };
      String name = AttributeChangeNotification.class.getName();
      String description = "Something happend in this MBean!";
      MBeanNotificationInfo info =
          new MBeanNotificationInfo(types, name, description);
      return new MBeanNotificationInfo[] {info};
      }

}
