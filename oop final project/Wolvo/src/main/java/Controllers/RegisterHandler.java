package Controllers;

import Models.*;
import Models.DAO.CourierDAO;
import Models.DAO.ManagerDAO;
import Models.DAO.RestaurantDAO;
import Models.DAO.UserDAO;

import static Models.Constants.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterHandler extends HttpServlet {

    private String hexToString(byte[] bytes) {
        StringBuffer buff = new StringBuffer();
        for (int aByte : bytes) {
            int val = aByte;
            val = val & 0xff;  // remove higher bits, sign
            if (val < 16) buff.append('0'); // leading 0
            buff.append(Integer.toString(val, 16));
        }
        return buff.toString();
    }

    private String hashedPassword(String password) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert messageDigest != null;
        messageDigest.update(password.getBytes());
        return hexToString(messageDigest.digest());
    }


    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (httpServletRequest.getParameter("custReg") != null) {
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/CustomerSignUp.jsp").forward(httpServletRequest,httpServletResponse);
        }
        if (httpServletRequest.getParameter("manReg") != null) {
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ManagerSignUp.jsp").forward(httpServletRequest,httpServletResponse);
        }
        if (httpServletRequest.getParameter("courReg") != null) {
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/CourierSignUp.jsp").forward(httpServletRequest,httpServletResponse);
        }
    }

    protected void addUser(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String email = httpServletRequest.getParameter("emailNewCust");
        UserDAO userDAO = (UserDAO) getServletContext().getAttribute("users");
        if (userDAO.getByEmail(email) != null) {
            httpServletResponse.setStatus(406);
            httpServletResponse.addHeader("registration error", "email");
            httpServletResponse.addHeader("url", "custReg");
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        String password = httpServletRequest.getParameter("passwordNewCust");
        String passwordConf = httpServletRequest.getParameter("passwordNewConfCust");
        if (!password.equals(passwordConf)) {
            httpServletResponse.setStatus(406);
            httpServletResponse.addHeader("registration error", "password");
            httpServletResponse.addHeader("url", "custReg");
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        String firstName = httpServletRequest.getParameter("fnameNewCust");
        String lastName = httpServletRequest.getParameter("lnameNewCust");
        String district = httpServletRequest.getParameter("districtsCust");
        String address = httpServletRequest.getParameter("AddressCust");
        String privacyType = httpServletRequest.getParameter("privacyTCust");
        String phonenum = httpServletRequest.getParameter("phoneCust");
        int rowsAffected = userDAO.addUser(email,firstName,lastName,hashedPassword(password),"Customer",privacyType,district,
                address,phonenum);
        if (rowsAffected == 1) {
            HttpSession session = httpServletRequest.getSession();
            session.setAttribute("userType","Customer");
            session.setAttribute("name",firstName);
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/successfulSignUp.jsp").forward(httpServletRequest,httpServletResponse);
        }   else {
            httpServletResponse.setStatus(417);
            httpServletResponse.addHeader("registration error", "email");
            httpServletResponse.addHeader("url", "custReg");
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
    }

    protected void addCourier(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String email = httpServletRequest.getParameter("emailNewCour");
        CourierDAO courierDAO = (CourierDAO) getServletContext().getAttribute("couriers");
        if (courierDAO.getCourierByEmail(email) != null) {
            httpServletResponse.setStatus(406);
            httpServletResponse.addHeader("registration error", "email");
            httpServletResponse.addHeader("url", "courReg");
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        String password = httpServletRequest.getParameter("passwordNewCour");
        String passwordConf = httpServletRequest.getParameter("passwordNewConfCour");
        if (!password.equals(passwordConf)) {
            httpServletResponse.setStatus(406);
            httpServletResponse.addHeader("registration error", "password");
            httpServletResponse.addHeader("url", "courReg");
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        String firstName = httpServletRequest.getParameter("fnameNewCour");
        String lastName = httpServletRequest.getParameter("lnameNewCour");
        String district = httpServletRequest.getParameter("districtsCour");
        String phonenum = httpServletRequest.getParameter("phoneCour");
        boolean rowsAffected = courierDAO.addCourier(email,firstName,lastName,district,hashedPassword(password),phonenum);
        if (rowsAffected) {
            HttpSession session = httpServletRequest.getSession();
            session.setAttribute("userType","Customer");
            session.setAttribute("name",firstName);
            session.setAttribute("surname", lastName);
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/successfulSignUpApproval.jsp").forward(httpServletRequest,httpServletResponse);
        }   else {
            httpServletResponse.setStatus(417);
            httpServletResponse.addHeader("registration error", "email");
            httpServletResponse.addHeader("url", "courReg");
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
    }

    protected void addManager(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String email = httpServletRequest.getParameter("emailNewMan");
        ManagerDAO managerDAO = (ManagerDAO) getServletContext().getAttribute("managers");
        if (managerDAO.getManagerByEmail(email) != null) {
            httpServletResponse.setStatus(406);
            httpServletResponse.addHeader("registration error", "email");
            httpServletResponse.addHeader("url", "manReg");
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        String password = httpServletRequest.getParameter("passwordNewMan");
        String passwordConf = httpServletRequest.getParameter("passwordNewConfMan");
        if (!password.equals(passwordConf)) {
            httpServletResponse.setStatus(406);
            httpServletResponse.addHeader("registration error", "password");
            httpServletResponse.addHeader("url", "manReg");
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        String firstName = httpServletRequest.getParameter("fnameNewMan");
        String lastName = httpServletRequest.getParameter("lnameNewMan");
        String district = httpServletRequest.getParameter("districtsMan");
        String address = httpServletRequest.getParameter("AddressMan");
        String nameRest = httpServletRequest.getParameter("nameRest");
        String phonenum = httpServletRequest.getParameter("phoneMan");
        RestaurantDAO restaurantDAO = (RestaurantDAO) getServletContext().getAttribute("restaurants");
        boolean k = managerDAO.addManager(email,firstName,lastName,hashedPassword(password),phonenum);
        Integer manK = null;
        if (k) manK = managerDAO.getManagerByEmail(email).getId();
        Integer resK = null;
        boolean rk = restaurantDAO.addRestaurant(nameRest, manK, district, address);
        if (rk) resK = restaurantDAO.getRestaurantByManager(manK).getId();
        managerDAO.changeRestaurant(resK, manK);
        if (manK != null && resK != null) {
            HttpSession session = httpServletRequest.getSession();
            session.setAttribute("userType","Manager");
            session.setAttribute("name",firstName);
            session.setAttribute("surname", lastName);
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/successfulSignUpApproval.jsp").forward(httpServletRequest,httpServletResponse);
        }   else {
            httpServletResponse.setStatus(417);
            httpServletResponse.addHeader("registration error", "other");
            httpServletResponse.addHeader("url", "manReg");
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (httpServletRequest.getParameter("signUpCustPressed") != null) {
            addUser(httpServletRequest,httpServletResponse);
        }
        if (httpServletRequest.getParameter("signUpCourPressed") != null) {
            addCourier(httpServletRequest,httpServletResponse);
        }
        if (httpServletRequest.getParameter("signUpManPressed") != null) {
            addManager(httpServletRequest,httpServletResponse);
        }
    }
}
