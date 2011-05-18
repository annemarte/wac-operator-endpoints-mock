<%--
  User: t521609
  Date: 11.05.11
  Time: 09:53
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Login Page</title></head>
<body>
<%
    String code = request.getParameter("code");
%>

<h1>Login Page</h1>

Enter your login details here:
<form action="http://localhost:8080/rest/operator/legacy/authenticate">
    <input type="text" name="code" value=<%=code%>/><br/>
    <input type="text" name="username"/> <br/>
    <input type="password" name="password"/> <br/>
    <input type="submit" name="ok" value="ok"/>
</form>


</body>
</html>