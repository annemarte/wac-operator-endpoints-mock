package org.wac.mock.resources;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Anne Marte Hjemås
 * Date: 28.05.11
 * Time: 09:27
 */
public class OperatorEndpointApplication extends Application {

       public Set<Class<?>> getClasses() {
           Set<Class<?>> s = new HashSet<Class<?>>();
           s.add(OAuth2_0.class);
           s.add(OperatorLogin.class);
           return s;
       }

}
