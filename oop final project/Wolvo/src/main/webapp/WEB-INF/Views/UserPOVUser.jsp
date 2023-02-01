<%@ page import="Models.User" %>
<%@ page import="java.util.List" %>
<%@ page import="Models.Order" %>
<%@ page import="Models.DAO.OrderDAO" %>
<%@ page import="Models.DAO.UserDAO" %>
<%@ page import="Models.DAO.DishDAO" %>
<%@ page import="Models.Dish" %>
<%@ page import="Models.DAO.RestaurantDAO" %>
<%@ page import="Models.Restaurant" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel = "stylesheet" href = "../../Resources/style.css" />
    <title>Wolvo</title>
</head>
<body>
<div class = "userFF">
    <label>First Name: <%= ((User) request.getAttribute("foundUser")).getFirstName()%></label> <br>
    <label>Last Name: <%= ((User) request.getAttribute("foundUser")).getLastName()%></label> <br>
    <label>Email: <%= ((User) request.getAttribute("foundUser")).getEmail()%></label> <br>
    <label>Phone Number: <%= ((User) request.getAttribute("foundUser")).getPhoneNumber()%> </label> <br>
    <label>District: <%= ((User) request.getAttribute("foundUser")).getDistrict()%></label> <br>
    <label>Address: <%= ((User) request.getAttribute("foundUser")).getAddress()%></label> <br>
    <%  if (((Integer) request.getAttribute("areFriends")).equals(0)) {
            if (((Integer) request.getAttribute("hasRequestReceived")).equals(1)) { %>
                <a href="confirmRequest?id=<%=((User) request.getAttribute("foundUser")).getId()%>">Confirm Request</a>
                <a href="rejectRequest?id=<%=((User) request.getAttribute("foundUser")).getId()%>">Reject Request</a>
            <% } else if (((Integer) request.getAttribute("hasRequestSent")).equals(0) && ((Integer) request.getAttribute("searchSelf")).equals(0)) { %>
                <a href = "sendFRequest?id=<%=((User) request.getAttribute("foundUser")).getId()%>">Send Friend Request</a>
            <% }
    }%>
</div>

<% if (((User) request.getAttribute("foundUser")).getPrivacyStatus().getStatus().equals("Public")
        || (((User) request.getAttribute("foundUser")).getPrivacyStatus().getStatus().equals("Friends") &&
        ((Integer) (request.getAttribute("areFriends"))).equals(1)) || ((Integer) request.getAttribute("searchSelf")).equals(1)) { %>
<div class = "userFOrders">
    <label>Order History: </label> <br>
    <% List<Order> courOrds = ((OrderDAO) application.getAttribute("orders")).
            getUserOrders(((User) request.getAttribute("foundUser")).getId());
        if (courOrds.isEmpty()) {
    %>
    <label>User doesn't have any orders.</label>
    <% }
    else { %>
    <% for (Order ord : courOrds) {
        Dish currDish = ((DishDAO) application.getAttribute("dishes")).getDishById(ord.getDish());
        if (currDish != null) {
        Restaurant currRest = ((RestaurantDAO) application.getAttribute("restaurants")).getRestaurantById(currDish.getRest_id());
    %>
    <li>
        <label>Dish: <%=currDish.getName()%> <br>
            <label>Price: <%=currDish.getPrice()%></label>
            <label>Restaurant: <%=currRest.getName()%></label> <br>
            <label>Restaurant address: <%=currRest.getDistrict()%>, <%=currRest.getAddress()%></label> <br>
    </li>
    <% }
     }%>
    <%
        }
    %>
</div>
<% } %>
<a href="login">Go Back to My Page</a>
<div class = "logoutR">
    <form action = "SignOut" method = "post">
        <input type="submit" value="Sign Out" id = "SignOut" name = "SignOut"/>
    </form>
</div>
</body>
</html>
