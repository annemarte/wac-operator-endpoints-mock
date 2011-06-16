<%@ page import="org.wac.mock.mockdb.Storage" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Authorization Page</title></head>
<body>
<h1>Authorization Page</h1>
<%
    String code = request.getParameter("code");
    String scope = Storage.getInstance().getTokenSession(code).getScope();
    System.out.println("code="+code+", scope="+scope);
%>

Accept access to scope <br/>
<%=scope%>?
<form action="http://localhost:8080/rest/operator/legacy/authorize" method="post">
    <br/>
    <input type="hidden" name="code" value="<%=code%>"/><br/>
    <input id="b_yes" type="submit" name="authorize" value="Yes"/>
    <input id="b_no" type="submit" name="authorize" value="No"/>
</form>

</body>
</html>