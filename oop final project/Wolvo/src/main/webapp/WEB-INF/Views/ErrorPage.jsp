<%@ page import="java.util.List" %>
<%@ page import="Models.DAO.*" %>
<%@ page import="Models.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel = "stylesheet" href = "../../Resources/style.css" />
    <title>Error</title>
</head>
<body>
<div class="Error">
    <errp>Error</errp> <br>
    <errh>      <%= response.getStatus() %> </errh> <br>
    <% String s = "/";
    String name = (String) session.getAttribute("name");
    String surname = (String) session.getAttribute("surname");
    String userType = (String) session.getAttribute("userType");
    Manager m = null; User u = null; Courier c = null;
    if ("Manager".equals(userType)) m = (Manager) session.getAttribute(userType);
    if ("Customer".equals(userType) || "Admin".equals(userType)) u = (User) session.getAttribute(userType);
    if ("Courier".equals(userType)) c = (Courier) session.getAttribute(userType);
    if (name != null && surname != null && userType != null) {
          if (userType.equals("Manager") && m != null && name.equals(m.getFirstName()) && surname.equals(m.getLastName())) s += "login";
          else if ((userType.equals("Customer") || userType.equals("Admin")) && u != null && name.equals(u.getFirstName()) && surname.equals(u.getLastName())) s += "login";
          else if (userType.equals("Courier") && c != null && name.equals(c.getFirstName()) && surname.equals(c.getLastName())) s += "login";
    } %>
    <% if ("email".equals(response.getHeader("registration error"))) { %>
        <p1>                  Email you entered is already in use </p1> <br>
    <% s = "/register?" + response.getHeader("url") + "=Sign+Up+as+a+Customer"; } %>
    <% if ("password".equals(response.getHeader("registration error"))) { %>
        <p1>                         Passwords don't match </p1> <br>
    <% s = "/register?" + response.getHeader("url") + "=Sign+Up+as+a+Customer"; } %>
    <% if ("other".equals(response.getHeader("registration error"))) { %>
        <p1>                  Registration failed </p1> <br>
    <% s = "/register?" + response.getHeader("url") + "=Sign+Up+as+a+Customer"; } %>
    <% if ("email".equals(response.getHeader("login error"))) { %>
        <p1>                               Email not correct</p1> <br>
    <% } %>
    <% if ("password".equals(response.getHeader("login error"))) { %>
        <p1>                          Password is not correct</p1> <br>
    <% } %>
    <a href=<%=s%> >Go Back</a>
</div>
</body>
</html>