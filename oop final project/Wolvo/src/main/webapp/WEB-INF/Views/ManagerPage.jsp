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
<div class = "managerInfo">
    <label>Hello, <%= session.getAttribute("name")%>!</label> <br>
    <label>Lets have a look at your profile!</label> <br>
    <label>Your contact info: </label> <br>
    <label>First Name: <%= session.getAttribute("name")%></label> <br>
    <label>Last Name: <%= session.getAttribute("surname")%></label> <br>
    <label>User type: <%=session.getAttribute("userType")%></label> <br>
    <label>Email: <%= ((Manager) session.getAttribute("manager")).getEmail()%></label> <br>
    <label>Phone Number: <%= ((Manager) session.getAttribute("manager")).getPhoneNumber()%> </label>
</div>
<div class = "managerRests">
    <label>Let's see your restaurant basic info!</label> <br>
    <% Restaurant restaurant = ((RestaurantDAO) application.getAttribute("restaurants")).
            getRestaurantById(((Manager) session.getAttribute("manager")).getRest_id());
        if (restaurant == null) {
    %>
    <label>You don't have any restaurants.</label>
    <% }
    else { %>
    <label>Restaurant Name: <%= ((Restaurant) restaurant).getName() %> <br> </label>
    <label>Restaurant District: <%= ((Restaurant) restaurant).getDistrict() %> <br> </label>
    <label>Restaurant Address: <%= ((Restaurant) restaurant).getAddress() %> <br> </label>
    <label>Restaurant Rating: <%= ((Restaurant) restaurant).getRating() %> <br> </label>
    <label>Restaurant Number of Raters: <%= ((Restaurant) restaurant).getRaters() %> <br> </label>
    <%}%>
</div>
<div class = "managerRestDishes">
    <label>Let's see your restaurant Dishes!</label> <br>
    <% List<Dish> dishes = ((DishDAO) application.getAttribute("dishes")).
            getRestaurantDishes(((RestaurantDAO) application.getAttribute("restaurants")).getRestaurantById(((Manager) session.getAttribute("manager")).getRest_id()));
        if (dishes.isEmpty()) {
    %>
            <label>Your restaurant doesn't have any dish.</label>
        <% }
    else { %>
    <label> Your Dishes: <br> </label>
            <% for (Dish d : dishes) { %>
            <label> Dish Name: <%= ((Dish) d).getName() %></label>
            <label><a href="removeDish?id=<%= ((Dish) d).getDish_id()%>">Remove</a> <br> </label>
            <label> Dish Price: <%= ((Dish) d).getPrice() %> <br> </label>
            <label> Dish Category: <%= ((Dish) d).getCategory() %> <br> </label>
            <label> Dish Rating: <%= ((Dish) d).getRating() %> <br> </label>
            <label> Dish Number of Raters: <%= ((Dish) d).getRaters() %> <br> </label>
            <label> Dish Status: <%= ((Status) ((Dish) d).getAdded()).getStatus() %> <br> </label>
        <% } %>
    <% } %>
</div>
<div class = "addNewDish">
    <form action="dishRequest" method="get">
        <input type="hidden" id="id" name="restaurant" value=<%=((Manager) session.getAttribute("manager")).getRest_id()%>>
        <p1> Send Request For New Dish </p1> <br> <br>
        <label for="dishName"> Dish Name:        </label>
        <input type = "text" id = "dishName" required="required" name = "name"/> <br> <br>
        <label for="dishCategory"> Dish Category:  </label>
        <input type = "text" id = "dishCategory" required="required" name = "category"/> <br> <br>
        <label for="dishPrice"> Dish Price:         </label>
        <input type = "number" id = "dishPrice" required="required" name="price" placeholder="5.99" step="0.01" min="0"> <br> <br>
        <input type = "submit" value = "Submit Dish" id = "dishSubmit" name="dishSubmit"> <br> <br>
    </form>
</div>
<div class = "logout">
    <form action = "SignOut" method = "post">
        <input type="submit" value="Sign Out" id = "SignOut" name = "SignOut"/>
    </form>
</div>
</body>
</html>
