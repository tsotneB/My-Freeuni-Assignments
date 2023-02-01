<%@ page import="Models.Order" %>
<%@ page import="Models.Dish" %>
<%@ page import="Models.DAO.DishDAO" %>
<%@ page import="Models.DAO.RestaurantDAO" %>
<%@ page import="Models.Restaurant" %>
<%@ page import="Models.Courier" %>
<%@ page import="Models.DAO.CourierDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel = "stylesheet" href = "../../Resources/style.css" />
    <title>Wolvo</title>
</head>
<body>
<div class = "reviewCont">
    <label>Write a review about the order</label> <br>
    <form action="reviewOrdConf" method="post">
        <%
            Order order = (Order) request.getAttribute("current_order");
            Dish dish = ((DishDAO) application.getAttribute("dishes")).getDishById(order.getDish());
            Restaurant rest = ((RestaurantDAO) application.getAttribute("restaurants")).getRestaurantById(dish.getRest_id());
            Courier cour = ((CourierDAO) application.getAttribute("couriers")).getCourierById(order.getCourier());
        %>
        <label for="reviewarea">Dish Identificator: </label>
        <input type="text" value="<%=order.getId()%>" name = "reviewID" id="reviewarea" readonly> <br>
        <label>Dish: <%=dish.getName()%></label> <br>
        <label>Category: <%=dish.getCategory()%></label> <br>
        <label>Price: <%=dish.getPrice()%></label> <br>
        <label>Restaurant: <%=rest.getName()%></label> <br>
        <label>Courier: <%=cour.getFirstName()%> <%=cour.getLastName()%></label> <br>
        <label>Courier rating: <%=cour.getRating()%></label> <br>
        <label>Rate a courier: </label>
        <input type = "radio" id = "reviewc1" name = "reviewCour" value="1">
        <label for = "reviewc1">1</label>
        <input type = "radio" id = "reviewc2" name = "reviewCour" value="2">
        <label for = "reviewc2">2</label>
        <input type = "radio" id = "reviewc3" name = "reviewCour" value="3">
        <label for = "reviewc3">3</label>
        <input type = "radio" id = "reviewc4" name = "reviewCour" value="4">
        <label for = "reviewc4">4</label>
        <input type = "radio" id = "reviewc5" name = "reviewCour" value="5">
        <label for = "reviewc5">5</label> <br>
        <label>Rate a Dish: </label>
        <input type = "radio" id = "reviewd1" name = "reviewDish" value="1">
        <label for = "reviewd1">1</label>
        <input type = "radio" id = "reviewd2" name = "reviewDish" value="2">
        <label for = "reviewd2">2</label>
        <input type = "radio" id = "reviewd3" name = "reviewDish" value="3">
        <label for = "reviewd3">3</label>
        <input type = "radio" id = "reviewd4" name = "reviewDish" value="4">
        <label for = "reviewd4">4</label>
        <input type = "radio" id = "reviewd5" name = "reviewDish" value="5">
        <label for = "reviewd5">5</label> <br>
        <label for = "courRev">About courier: </label>
        <input type="text" id = "courRev" name ="courtxt"> <br>
        <label for = "dishRev">About dish: </label>
        <input type="text" id = "dishRev" name ="dishtxt"> <br> <br>
        <input type="submit" value="Send Review" id = "reviewBut" name = "reviewBut"/>
    </form>
    <label></label>
</div>
<a href="login">Go Back to My Page</a>
<div class = "logoutR">
    <form action = "SignOut" method = "post">
        <input type="submit" value="Sign Out" id = "SignOut" name = "SignOut"/>
    </form>
</div>
</body>
</html>
