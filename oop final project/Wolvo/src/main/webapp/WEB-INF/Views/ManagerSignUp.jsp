<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel = "stylesheet" href = "../../Resources/style.css" />
    <title>Sign Up on Wolvo</title>
</head>
<body>
<div class = "manSignUp">
    <h1>Manager Registration</h1>
    <form action="register" method="post">
        <label for="emailRegMan">Email:                       </label>
        <input type = "email" id = "emailRegMan" required="required" name = "emailNewMan" /> <br> <br>
        <label for = "passwordRegMan">Password:                 </label>
        <input type = "password" id = "passwordRegMan" required="required" name = "passwordNewMan" /><br> <br>
        <label for = "passwordRegConfMan">Confirm Password:  </label>
        <input type = "password" id = "passwordRegConfMan" required="required" name = "passwordNewConfMan" /><br> <br>
        <label for = "fnameregMan">First Name:              </label>
        <input type = "text" id = "fnameregMan" required="required" name = "fnameNewMan"/> <br> <br>
        <label for = "lnameregMan">Last Name:               </label>
        <input type = "text" id = "lnameregMan" required="required" name = "lnameNewMan"/> <br> <br>
        <label for = "districtMan">Choose your working district: </label>
        <select id="districtMan" name="districtsMan">
            <option value="Didube">Didube</option>
            <option value="Gldani">Gldani</option>
            <option value="Saburtalo">Saburtalo</option>
            <option value="Vake">Vake</option>
            <option value="Isani">Isani</option>
            <option value="Varketili">Varketili</option>
            <option value="Digomi">Digomi</option>
        </select> <br> <br>
        <label for = "nameRest">Restaurant name:    </label>
        <input type = "text" id = "nameRest" required="required" name = "nameRest"/> <br> <br>
        <label for = "addressMan">Restaurant address:</label>
        <input type = "text" id = "addressMan" required="required" name = "AddressMan"/> <br> <br>
        <Label for = "phonenumberMan">Phone number:        </Label>
        <input type = "tel" id = "phonenumberMan" required="required" name = "phoneMan" placeholder="555-12-34-56"
               pattern="[0-9]{3}-[0-9]{2}-[0-9]{2}-[0-9]{2}"> <br> <br> <br>
        <input type = "submit" id = "signUpManBut" name = "signUpManPressed" value="Sign Up">
    </form>
</div>
<a href="/" >Go Back</a>
</body>
</html>
