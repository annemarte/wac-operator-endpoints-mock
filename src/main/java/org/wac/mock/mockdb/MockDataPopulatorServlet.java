package org.wac.mock.mockdb;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * User: Anne Marte Hjemås
 * Date: 18.05.11
 * Time: 16:19
 */
public class MockDataPopulatorServlet  extends HttpServlet {

    @Override
    public void init() throws ServletException {

        super.init();
        Consumer test1 = new Consumer();
        test1.setClientId("782378hjgfjrertre92393");
        test1.setName("Starbucks App");
        test1.setDescription("Pay for Starbucks coffe with your phone ");
        test1.setScope("POST-/payment/acr:Authorization/transactions/amount?code=wac-123");
        test1.setSecret("waccawacca1234");
        test1.setUrl("http://localhost:8080/jsp/clientsPage.jsp");

        Consumer test2 = new Consumer();
        test2.setClientId("25kjirdi8935rei949dfkl");
        test2.setName("MyFinances");
        test2.setDescription("MyFinances app. Checks your balance.");
        test2.setScope("account:read");
        test2.setSecret("waccawacca4311");
        test2.setUrl("http://localhost:8080/jsp/clientsPage.jsp");

        Storage.getInstance().putConsumer("782378hjgfjrertre92393", test1);
        Storage.getInstance().putConsumer("25kjirdi8935rei949dfkl", test2);
    }
}
