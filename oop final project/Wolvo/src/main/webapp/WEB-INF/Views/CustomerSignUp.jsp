<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel = "stylesheet" href = "../../Resources/style.css" />
    <title>Sign Up on Wolvo</title>
</head>
<body>
<div class = "custSignUp">
<h1>Customer Registration</h1>
<form action="register" method="post">
    <label for="emailRegCust">Email:                      </label>
    <input type = "email" id = "emailRegCust" required="required" name = "emailNewCust" /> <br> <br>
    <label for = "passwordRegCust">Password:                </label>
    <input type = "password" id = "passwordRegCust" required="required" name = "passwordNewCust" /><br> <br>
    <label for = "passwordRegConfCust">Confirm Password: </label>
    <input type = "password" id = "passwordRegConfCust" required="required" name = "passwordNewConfCust" /><br> <br>
    <label for = "fnameregCust">First Name:             </label>
    <input type = "text" id = "fnameregCust" required="required" name = "fnameNewCust"/> <br> <br>
    <label for = "lnameregCust">Last Name:              </label>
    <input type = "text" id = "lnameregCust" required="required" name = "lnameNewCust"/> <br> <br>
    <label for = "districtCust">Choose your district: </label>
    <select id="districtCust" name="districtsCust">
        <option value="Didube">Didube</option>
        <option value="Gldani">Gldani</option>
        <option value="Saburtalo">Saburtalo</option>
        <option value="Vake">Vake</option>
        <option value="Isani">Isani</option>
        <option value="Varketili">Varketili</option>
        <option value="Digomi">Digomi</option>
    </select> <br> <br>
    <label for = "addressCust">Enter your address: </label>
    <input type = "text" id = "addressCust" required="required" name = "AddressCust"/> <br> <br>
    <label>Please specify your privacy settings: </label> <br>
    <input type = "radio" id = "firstTCust" name = "privacyTCust" value="Private" required>
    <label for = "firstTCust">No one can see my order history</label> <br>
    <input type = "radio" id = "secondTCust" name = "privacyTCust" value="Friends">
    <label for = "secondTCust">Only my friends can see my order history</label> <br>
    <input type = "radio" id = "thirdTCust" name = "privacyTCust" value="Public">
    <label for = "thirdTCust">Anyone can see my order history</label> <br> <br>
    <Label for = "phonenumberCust">Phone number:        </Label>
    <input type = "tel" id = "phonenumberCust" required="required" name = "phoneCust" placeholder="555-12-34-56"
           pattern="[0-9]{3}-[0-9]{2}-[0-9]{2}-[0-9]{2}"> <br> <br> <br>
    <input type = "submit" id = "signUpCustBut" name = "signUpCustPressed" value = "Sign Up">
</form>
</div>
<a href="/" >Go Back</a>
</body>
</html>
