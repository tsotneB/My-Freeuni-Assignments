<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel = "stylesheet" href = "../../Resources/style.css" />
    <title>Sign Up on Wolvo</title>
</head>
<body>
<div class = "courSignUp">
    <h1>Courier Registration</h1>
    <form action="register" method="post">
        <label for="emailRegCour">Email:                      </label>
        <input type = "email" id = "emailRegCour" required="required" name = "emailNewCour" /> <br> <br>
        <label for = "passwordRegCour">Password:                </label>
        <input type = "password" id = "passwordRegCour" required="required" name = "passwordNewCour" /><br> <br>
        <label for = "passwordRegConfCour">Confirm Password: </label>
        <input type = "password" id = "passwordRegConfCour" required="required" name = "passwordNewConfCour" /><br> <br>
        <label for = "fnameregCour">First Name:             </label>
        <input type = "text" id = "fnameregCour" required="required" name = "fnameNewCour"/> <br> <br>
        <label for = "lnameregCour">Last Name:              </label>
        <input type = "text" id = "lnameregCour" required="required" name = "lnameNewCour"/> <br> <br>
        <label for = "districtCour">Choose your working district: </label>
        <select id="districtCour" name="districtsCour">
            <option value="Didube">Didube</option>
            <option value="Gldani">Gldani</option>
            <option value="Saburtalo">Saburtalo</option>
            <option value="Vake">Vake</option>
            <option value="Isani">Isani</option>
            <option value="Varketili">Varketili</option>
            <option value="Digomi">Digomi</option>
        </select> <br> <br>
        <Label for = "phonenumberCour">Phone number:        </Label>
        <input type = "tel" id = "phonenumberCour" required="required" name = "phoneCour" placeholder="555-12-34-56"
               pattern="[0-9]{3}-[0-9]{2}-[0-9]{2}-[0-9]{2}"> <br> <br> <br>
        <input type = "submit" id = "signUpCourBut" name = "signUpCourPressed" value="Sign Up">
    </form>
</div>
<a href="/" >Go Back</a>
</body>
</html>
