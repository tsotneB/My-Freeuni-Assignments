<%@ page import="Models.Dish" %>
<%@ page import="Models.Restaurant" %>
<%@ page import="Models.DAO.RestaurantDAO" %>
<%@ page import="Models.User" %>
<%@ page import="Models.DAO.FriendsDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel = "stylesheet" href = "../../Resources/style.css" />
    <title>Wolvo</title>
</head>
<body>
<div class = "orderContainer">
    <form action = "orderDishConf" method="get">
        <% Dish currentDish = (Dish) request.getAttribute("current_dish");
            Restaurant currentRest = ((RestaurantDAO) application.getAttribute("restaurants")).
                    getRestaurantById(currentDish.getRest_id()); %>
        <label> You're ordering from <%=currentRest.getName()%></label> <br>
        <label for="disharea">Dish Identificator: </label>
        <input type="text" value="<%=currentDish.getDish_id()%>" name = "dish" id="disharea" readonly> <br>
        <label for = "chooseRecipient">Order for: </label>
        <select id="chooseRecipient" name="orderRecipient">
            <option value="<%=((User) session.getAttribute("customer")).getId()%>">You</option>
            <% for (User usr : ((FriendsDAO) application.getAttribute("friends")).
                    getFriends((User) session.getAttribute("customer"))) { %>
            <option value="<%=usr.getId()%>"><%=usr.getFirstName()%> <%=usr.getLastName()%></option>
            <%  } %>
        </select> <br>
        <label for="quantityField">Enter quantity: </label>
        <input type="number" id = "quantityField" name="quantity" placeholder="Quantity" required="required"/> <br> <br>
        <input type="submit" value="Order" id = "orderBut" name = "custReg"/>
    </form>
</div>
<a href="login">Go Back to My Page</a>
<div class = "logoutR">
    <form action = "SignOut" method = "post">
        <input type="submit" value="Sign Out" id = "SignOut" name = "SignOut"/>
    </form>
</div>
</body>
</html>
