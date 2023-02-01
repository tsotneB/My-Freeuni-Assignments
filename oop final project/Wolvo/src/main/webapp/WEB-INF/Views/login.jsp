<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel = "stylesheet" href = "../../Resources/style.css" />
    <title>Welcome</title>
</head>
<body>
<div class = "introduction">
    <h1>Welcome To Wolvo</h1>

    <p>Please Log in</p>

    <form action = "login" method = "post">
        <label for="emailField">Email:      </label>
        <input type = "email" id = "emailField" required="required" name = "email" /> <br> <br>
        <label for = "passwordField">Password: </label>
        <input type = "password" id = "passwordField" required="required" name = "password" /> <br> <br>
        <label>Sign in as a: </label>
        <input type = "radio" id = "userTCust" name = "userTLog" value="Customer" required>
        <label for = "userTCust">Customer</label>
        <input type = "radio" id = "userTMan" name = "userTLog" value="Manager">
        <label for = "userTMan">Manager</label>
        <input type = "radio" id = "userTCour" name = "userTLog" value="Courier">
        <label for = "userTCour">Courier</label>
        <br> <br> <br>
        <button id = "loginBut">Login</button>
    </form>

    <div class = "buttonHolder">
    <form action="register" method="get">
        <input type="submit" value="Sign Up as a Customer" id = "signUpCust" name = "custReg"/> <br> <br>
        <input type="submit" value="Sign Up as a Manager" id = "signUpMan" name = "manReg"> <br> <br>
        <input type="submit" value="Sign Up as a Courier" id = "signUpCour" name = "courReg"> <br> <br>
    </form>
    </div>
</div>
</body>
</html>
