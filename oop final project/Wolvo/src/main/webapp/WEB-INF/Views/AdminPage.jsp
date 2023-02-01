<%@ page import="java.util.List" %>
<%@ page import="Models.DAO.CourierDAO" %>
<%@ page import="Models.DAO.ManagerDAO" %>
<%@ page import="Models.DAO.DishDAO" %>
<%@ page import="Models.*" %>
<%@ page import="Models.DAO.RestaurantDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel = "stylesheet" href = "../../Resources/style.css" />
    <title>Wolvo</title>
</head>
<body>
<div class = "adminInfo">
    <label>Hello, <%=session.getAttribute("name")%> <%=session.getAttribute("surname")%>!</label> <br>
    <label>Lets have a look at your profile!</label> <br>
    <label>User type: <%=session.getAttribute("userType")%></label> <br>
    <label>Your contact info: </label> <br>
    <label>District: <%=((User) session.getAttribute("customer")).getDistrict()%></label> <br>
    <label>Living adress: <%=((User) session.getAttribute("customer")).getAddress()%></label> <br>
    <label>Phone number: <%=((User) session.getAttribute("customer")).getPhoneNumber()%></label>
</div>
<div class = "pendingCouriers">
    <% List<Courier> pendingCours = ((CourierDAO) application.getAttribute("couriers")).
            getPendingCouriers();
    if (pendingCours.isEmpty()) {
    %>
    <label>There are currently no couriers  who  <br>  are waiting for your approval.</label>
    <% }
    else { %>
    <label>List of couriers waiting for your approval: </label> <br>
    <% for (Courier courier : pendingCours) { %>
    <label>Courier <%=courier.getFirstName()%> <%= courier.getLastName()%></label><br>
    <a href="approveCourier?id=<%=courier.getId()%>">See Details</a> <br>
    <% } %>
    <%
        }
    %>
</div>
<div class="pendingManagers">
    <% List<Manager> pendingManagers = ((ManagerDAO) application.getAttribute("managers")).
            getPendingManagers();
        if (pendingManagers.isEmpty()) {
    %>
    <label>There are currently no managers  who  <br>  are waiting for your approval.</label>
    <% }
    else { %>
    <label>List of Managers waiting for your approval: </label> <br>
    <% for (Manager manager : pendingManagers) { %>
    <label>Manager <%=manager.getFirstName()%> <%= manager.getLastName()%></label><br>
    <a href="approveManager?id=<%=manager.getId()%>">See Details</a> <br>
    <% } %>
    <%
        }
    %>
</div>
</div>
<div class="pendingDishes">
    <% List<Dish> pendingDishes = ((DishDAO) application.getAttribute("dishes")).
            getPendingDishes();
        if (pendingDishes.isEmpty()) {
    %>
    <label>There are currently no dishes <br> waiting for your approval.</label>
    <% }
    else { %>
    <label>List of Dishes waiting for your approval: </label> <br>
   <% for (Dish dish : pendingDishes) { %>
    <% Restaurant res = ((RestaurantDAO) application.getAttribute("restaurants")).
            getRestaurantById(dish.getRest_id());%>
    <label>Dish name: <%=dish.getName()%></label><br>
    <label>Restaurant: <%=res.getName()%></label><br>
    <a href="approveDish?id=<%=dish.getDish_id()%>">See Details</a> <br>
    <% } %>
    <%
        }
    %>
</div>

<div class = "searchUsers">
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
