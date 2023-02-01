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
<div class = "courierInfo">
    <label>Hello, <%= ((Courier) session.getAttribute("courier")).getFirstName() %>!</label> <br>
    <label>Lets have a look at your profile!</label> <br>
    <label>First Name: <%= ((Courier) session.getAttribute("courier")).getFirstName()%></label> <br>
    <label>Last Name: <%= ((Courier) session.getAttribute("courier")).getLastName()%></label> <br>
    <label>Email: <%= ((Courier) session.getAttribute("courier")).getEmail()%></label> <br>
    <label>Phone Number: <%= ((Courier) session.getAttribute("courier")).getPhoneNumber()%> </label> <br>
    <label>Your working district: <%= ((Courier) session.getAttribute("courier")).getDistrict()%></label> <br>
    <label>Number of completed orders: <%= ((Courier) session.getAttribute("courier")).getCompletedOrders()%></label> <br>
    <label>Rating: <%= ((Courier) session.getAttribute("courier")).getRating()%></label>
</div>
<div class = "courierReviews">
    <label>Let's see your reviews!</label> <br>
    <% List<Review> courRevs = ((ReviewDAO) application.getAttribute("reviews")).
            getCourierReviews((Courier) session.getAttribute("courier"));
        if (courRevs.isEmpty()) {
    %>
    <label>You don't have any reviews.</label>
    <% }
    else { boolean b = false;%>
    <% for (Review review : courRevs) {
        if (review.getCourierRating() != -1 || !"".equals(review.getCourierText())) { b = true;
    %>
    <label>User <%= ((UserDAO) application.getAttribute("users")).
            getByID(review.getUser()).getFirstName()%> 
        <%= ((UserDAO) application.getAttribute("users")).
                getByID(review.getUser()).getLastName()%>  </label>
                <% if (review.getCourierRating() != -1) %>
                    <label>gave you a <%=review.getCourierRating()%>  out of 5!</label> <br>
                <% if (!"".equals(review.getCourierText())) %>
                    <label>User made a comment: <%=review.getCourierText()%></label>
            <%}%>
        <%}
          if (b == false) { %>
                <label>You don't have any reviews.</label>
          <% }
        %>
    <%}%>
</div>
<div class = "currentOrder">
    <% if (((Courier) session.getAttribute("courier")).getFree().getStatus().equals("Free")) { %>
    <label>You don't have any orders right now.</label> <br>
    <% }
    else { %>
    <label>           Your current order:</label> <br>
    <% Order currentOrder = ((OrderDAO) application.getAttribute("orders")).
            getCouriersCurrentOrder(((Courier) session.getAttribute("courier")).getId());

       Dish currDish = ((DishDAO) application.getAttribute("dishes")).getDishById(currentOrder.getDish());
       if (currDish != null) {
       Restaurant currentRestaurant = ((RestaurantDAO) application.getAttribute("restaurants")).getRestaurantById(
               ((DishDAO) application.getAttribute("dishes")).getDishById(currentOrder.getDish()).getRest_id());
       Dish currentDish = ((DishDAO) application.getAttribute("dishes")).getDishById(currentOrder.getDish());
    %>
    <label>Restaurant: <%= currentRestaurant.getName()%> </label> <br>
    <label>Dish: <%=currentDish.getName()%></label> <br>
    <label>Price: <%=currentDish.getPrice()%></label> <br>
    <label>Address: <%=currentOrder.getAddress()%></label> <br>
    <a href="confirmOrder">Mark order as delivered</a>
    <% }
     }%>

</div>
<div class = "logout">
    <form action = "SignOut" method = "post">
        <input type="submit" value="Sign Out" id = "SignOut" name = "SignOut"/>
    </form>
</div>
</body>
</html>
