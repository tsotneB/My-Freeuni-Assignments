package Controllers;

import Models.Courier;
import Models.DAO.CourierDAO;
import Models.DAO.ManagerDAO;
import Models.DAO.OrderDAO;
import Models.Manager;
import Models.User;
import Models.DAO.UserDAO;
import static Models.Constants.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class LoginHandler extends HttpServlet {
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

    private void logInCustomer(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String email = httpServletRequest.getParameter("email");
        String password = httpServletRequest.getParameter("password");
        UserDAO userDAO = (UserDAO) getServletContext().getAttribute("users");

        User currentUser = userDAO.getByEmail(email);
        if (currentUser == null) {
            httpServletResponse.setStatus(400);
            httpServletResponse.addHeader("login error", "email");
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        if (!currentUser.getPassword().equals(hashedPassword(password))) {
            httpServletResponse.setStatus(400);
            httpServletResponse.addHeader("login error", "password");
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        httpServletRequest.getSession().setAttribute("name",currentUser.getFirstName());
        httpServletRequest.getSession().setAttribute("surname",currentUser.getLastName());
        httpServletRequest.getSession().setAttribute("customer",currentUser);
        httpServletRequest.getSession().setAttribute("userType",currentUser.getUserStatus().getStatus());
        httpServletRequest.getRequestDispatcher("WEB-INF/Views/" + currentUser.getUserStatus().getStatus() + "Page.jsp")
                .forward(httpServletRequest,httpServletResponse);

    }

    private void logInCourier(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String email = httpServletRequest.getParameter("email");
        String password = httpServletRequest.getParameter("password");
        CourierDAO courierDAO = (CourierDAO) getServletContext().getAttribute("couriers");

        Courier currentCourier = courierDAO.getCourierByEmail(email);
        if (currentCourier == null) {
            httpServletResponse.setStatus(400);
            httpServletResponse.addHeader("login error", "email");
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        if (!currentCourier.getPassword().equals(hashedPassword(password))) {
            httpServletResponse.setStatus(400);
            httpServletResponse.addHeader("login error", "password");
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        if (!currentCourier.getAdded().getStatus().equals(APPROVED)) {
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/PendingPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        httpServletRequest.getSession().setAttribute("name",currentCourier.getFirstName());
        httpServletRequest.getSession().setAttribute("surname",currentCourier.getLastName());
        httpServletRequest.getSession().setAttribute("courier",currentCourier);
        httpServletRequest.getSession().setAttribute("userType","Courier");
        httpServletRequest.getRequestDispatcher("WEB-INF/Views/CourierPage.jsp")
                .forward(httpServletRequest,httpServletResponse);
    }


    private void logInManager(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String email = httpServletRequest.getParameter("email");
        String password = httpServletRequest.getParameter("password");
        ManagerDAO managerDAO = (ManagerDAO) getServletContext().getAttribute("managers");
        Manager currentManager = managerDAO.getManagerByEmail(email);
        if (currentManager == null) {
            httpServletResponse.setStatus(400);
            httpServletResponse.addHeader("login error", "email");
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        if (!currentManager.getPassword().equals(hashedPassword(password))) {
            httpServletResponse.setStatus(400);
            httpServletResponse.addHeader("login error", "password");
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        if (!currentManager.getAddStatus().getStatus().equals(APPROVED)) {
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/PendingPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        httpServletRequest.getSession().setAttribute("name", currentManager.getFirstName());
        httpServletRequest.getSession().setAttribute("surname", currentManager.getLastName());
        httpServletRequest.getSession().setAttribute("manager", currentManager);
        httpServletRequest.getSession().setAttribute("userType", "Manager");
        httpServletRequest.getRequestDispatcher("WEB-INF/Views/ManagerPage.jsp")
                .forward(httpServletRequest, httpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (httpServletRequest.getParameter("userTLog").equals("Customer")) {
            logInCustomer(httpServletRequest,httpServletResponse);
        }
        if (httpServletRequest.getParameter("userTLog").equals("Courier")) {
            logInCourier(httpServletRequest,httpServletResponse);
        }
        if (httpServletRequest.getParameter("userTLog").equals("Manager")) {
            logInManager(httpServletRequest,httpServletResponse);
        }
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (httpServletRequest.getSession().getAttribute("userType") == null) {
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/login.jsp")
                    .forward(httpServletRequest, httpServletResponse);
        }
        if (httpServletRequest.getSession().getAttribute("userType").equals("Courier")) {
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/CourierPage.jsp")
                    .forward(httpServletRequest,httpServletResponse);
        }
        if (httpServletRequest.getSession().getAttribute("userType").equals("Admin")) {
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/AdminPage.jsp")
                    .forward(httpServletRequest,httpServletResponse);
        }
        if (httpServletRequest.getSession().getAttribute("userType").equals("Customer")) {
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/CustomerPage.jsp")
                    .forward(httpServletRequest,httpServletResponse);
        }
        if (httpServletRequest.getSession().getAttribute("userType").equals("Manager")) {
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ManagerPage.jsp")
                    .forward(httpServletRequest,httpServletResponse);
        }
    }
}
