<%@ page import="Models.Dish" %>
<%@ page import="java.util.List" %>
<%@ page import="Models.Restaurant" %>
<%@ page import="Models.DAO.DishDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel = "stylesheet" href = "../../Resources/style.css" />
    <title>Wolvo</title>
</head>
<body>
<div class="restaurantDishes">
    <%List<Dish> dishes = ((DishDAO) application.getAttribute("dishes")).
            getRestaurantDishes((Restaurant) request.getAttribute("orderFromRest"));
        if (dishes.isEmpty()) { %>
    <label>This restaurant doesn't serve any dishes yet.</label> <br>
    <% }
    else {
        for (Dish dish:dishes) {   %>
    <label>Dish: <%=dish.getName()%></label> <br>
    <label>Category: <%=dish.getCategory()%></label> <br>
    <label>Price: <%=dish.getPrice()%></label> <br>
    <a href = "orderDish?id=<%=dish.getDish_id()%>">Order</a> <br>
    <% } %>
    <% }%>
</div>
<a href="login">Go Back to My Page</a>
<div class = "logoutR">
    <form action = "SignOut" method = "post">
        <input type="submit" value="Sign Out" id = "SignOut" name = "SignOut"/>
    </form>
</div>
</body>
</html>
