<%@ page import="Models.Courier" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel = "stylesheet" href = "../../Resources/style.css" />
    <title>Wolvo</title>
</head>
<body>
<div class="noCouriers">
    <label>Your order is confirmed.</label> <br>
    <label>Courier <%=((Courier) request.getAttribute("delivering")).getFirstName()%>
        <%=((Courier) request.getAttribute("delivering")).getLastName()%> with ratting
        <%=((Courier) request.getAttribute("delivering")).getRating()%> is delivering.</label> <br>
    <a href="login">Go Back to My Page</a>
</div>
</body>
</html>
