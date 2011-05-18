<%--
  Created by IntelliJ IDEA.
  User: t521609
  Date: 11.05.11
  Time: 10:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Authorization Page</title></head>
<body>
<h1>Authorization Page</h1>
<%
    String code = request.getParameter("code");
%>

Accept access to scope <%=request.getParameter("code")%>?
<form action="http://localhost:8080/rest/operator/legacy/authorize">
    <br/>
    <input type="text" name="code" value=<%=code%>/><br/>
    <input type="submit" name="authorize" value="Yes"/>
    <input type="submit" name="authorize" value="No"/>
</form>

</body>
</html>