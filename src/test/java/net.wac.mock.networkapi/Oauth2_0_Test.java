package net.wac.mock.networkapi;

import net.wac.mock.networkapi.impl.OAuth2_0_Impl;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static junit.framework.Assert.assertNotNull;

/**
 * User: Anne Marte Hjemås
 * Date: 27.05.11
 * Time: 16:52
 */
public class Oauth2_0_Test {

    @Test
    public void testAuthorize(){
       OAuth2_0 operatorOauth = new OAuth2_0_Impl();
       Response response = operatorOauth.authorize();
       assertNotNull("Should not be null", response);

    }
}
