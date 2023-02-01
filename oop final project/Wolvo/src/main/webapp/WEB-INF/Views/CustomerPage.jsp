<%@ page import="java.util.List" %>
<%@ page import="Models.DAO.*" %>
<%@ page import="Models.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel = "stylesheet" href = "../../Resources/style.css" />
    <title>Wolvo</title>
</head>
<body>
<div class = "customerInfo">
    <label>Hello, <%=((User) session.getAttribute("customer")).getFirstName()%>!</label> <br>
    <label>Let's have a look at your profile!</label> <br>
    <label>First Name: <%=((User) session.getAttribute("customer")).getFirstName()%></label> <br>
    <label>Last Name: <%=((User) session.getAttribute("customer")).getLastName()%></label> <br>
    <label>Email: <%=((User) session.getAttribute("customer")).getEmail()%></label> <br>
    <label>Phone Number: <%=((User) session.getAttribute("customer")).getPhoneNumber()%></label> <br>
    <label>District: <%=((User) session.getAttribute("customer")).getDistrict()%></label> <br>
    <label>Living address: <%=((User) session.getAttribute("customer")).getAddress()%></label> <br>
    <label>User Status: <%=((User) session.getAttribute("customer")).getUserStatus().getStatus()%></label> <br>
    <label>Privacy Status: <%=((User) session.getAttribute("customer")).getPrivacyStatus().getStatus()%></label> <br>
</div>
<div class = "customerFriends">
    <label>List of your friends: </label> <br>
    <% List<User> userFriends = ((FriendsDAO) application.getAttribute("friends")).getFriends((User) session.getAttribute("customer"));
        if (userFriends.isEmpty()) { %>
    <label>You don't have any friends yet.</label>
    <% }
    else {
        for (User user : userFriends) { %>
    <a href="userFound?id=<%=user.getId()%>"> <%=user.getFirstName()%> <%=user.getLastName()%> </a> <br>
    <% }
    }  %>
</div>
<div class = "customerFriendRequests">
    <label>Your friend Requests</label> <br>
    <% List<User> userRequests = ((FriendsRequestDAO) application.getAttribute("friend_requests")).
            receivedRequets((User) session.getAttribute("customer"));
        if (userRequests.isEmpty()) { %>
    <label>You don't have any friend requests yet.</label>
    <% }
    else {
        for (User user : userRequests) { %>
    <label><%=user.getFirstName()%> <%=user.getLastName()%></label> <br>
    <a href="userFound?id=<%=user.getId()%>">See Profile</a>
    <a href="confirmRequest?id=<%=user.getId()%>"> Accept </a>
    <a href="rejectRequest?id=<%=user.getId()%>"> Reject </a> <br>
    <% }
    }  %>
</div>
<div class="OrderHistory">
    <label>Your order History:</label> <br>
    <% List<Order> userOrds = ((OrderDAO) application.getAttribute("orders")).
            getUserOrders(((User) session.getAttribute("customer")).getId());
        if (userOrds.isEmpty()) { %>
    <label>You don't have any orders yet.</label> <br>
    <%   }
    else {
        for (Order ord : userOrds) {
            Dish dish = ((DishDAO) application.getAttribute("dishes")).getDishById(ord.getDish());
            if (dish != null) {
            Restaurant rest = ((RestaurantDAO) application.getAttribute("restaurants")).getRestaurantById(dish.getRest_id());
            Courier courier = ((CourierDAO) application.getAttribute("couriers")).getCourierById(ord.getCourier()); %>
    <label>Dish name: <%=dish.getName()%></label> <br>
    <label>Category: <%=dish.getCategory()%></label><br>
    <label>Restaurant name: <%=rest.getName()%></label><br>
    <label>Price: <%=dish.getPrice()%></label><br>
    <label>Courier: <%=courier.getFirstName()%> <%=courier.getLastName()%></label><br>
    <label>Order Date: <%=ord.getOrderDate()%></label><br>
    <label>Order status: <%=ord.getOrderStatus().getStatus()%></label><br>
    <%
        if (ord.getOrderStatus().getStatus().equals(Constants.DELIVERED)) {
    %>
    <label>Receive date: <%=ord.getReceiveDate()%></label> <br>
    <%
        Review currRev = ((ReviewDAO) application.getAttribute("reviews")).getByID(ord.getId());
        if (currRev == null) {%>
    <a href="reviewOrd?id=<%=ord.getId()%>">Rate order</a><br>
    <% } else {
        if (currRev.getCourierRating() != -1) { %>
    <label>You gave a courier <%=currRev.getCourierRating()%> out of 5!</label> <br>
    <%    }
        if (currRev.getDishRating() != -1) { %>
    <label>You gave a dish <%=currRev.getDishRating()%> out of 5!</label> <br>
    <%    } %>
    <% if (!currRev.getCourierText().equals("")) {  %>
    <label>You made a comment about courier: <%=currRev.getCourierText()%></label>
    <% }
        if (!currRev.getDishText().equals("")) { %>
    <label>You also made a comment about dish: <%=currRev.getDishText()%> </label> <br>
    <%
         }
        }%>
    <% } %>
    <% }%>
    <%   } %>

    <% } %>
</div>
<div class="newOrder">
    <label>Make new Order!</label> <br>
    <label>You can order from these restaurants: </label> <br>
    <%
        List<Restaurant> restaurants = ((RestaurantDAO) application.getAttribute("restaurants")).getRestaurants();
        if (restaurants.isEmpty()) { %>
    <label>Unfortunately, there are no restaurants working right now</label>
    <% }
    else {
        for (Restaurant res : restaurants) { %>
    <a href = "orderFromRestaurant?id=<%=res.getId()%>"><%=res.getName()%></a> <br>
    <%   }
    }%>
</div>
<div class = "userSearchBar">
    <form action = "userSearch" method = "get">
        <input type = "text" id = "searchUser" name = "search" placeholder="Look for users by full name"/> <br> <br>
    </form>
</div>
<div class = "logout">
    <form action = "SignOut" method = "post">
        <input type="submit" value="Sign Out" id = "SignOut" name = "SignOut"/>
    </form>
</div>
</body>
</html>
