package Controllers;

import Models.DAO.DishDAO;
import Models.DAO.FriendsDAO;
import Models.DAO.FriendsRequestDAO;
import Models.DAO.UserDAO;
import Models.Dish;
import Models.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserFoundHandler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!"Admin".equals(httpServletRequest.getSession().getAttribute("userType")) &&
                !"Customer".equals(httpServletRequest.getSession().getAttribute("userType"))) {
            httpServletResponse.setStatus(405);
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        User user = ((UserDAO) getServletContext().getAttribute("users")).
                getByID(Integer.parseInt(httpServletRequest.getParameter("id")));
        httpServletRequest.setAttribute("foundUser",user);
        if ("Admin".equals(httpServletRequest.getSession().getAttribute("userType"))) {
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/UserPOVAdmin.jsp").forward(httpServletRequest, httpServletResponse);
        }   else {
            FriendsDAO friendsDAO = (FriendsDAO) ((getServletContext().getAttribute("friends")));
            FriendsRequestDAO friendsRequestDAO = (FriendsRequestDAO) (getServletContext().getAttribute("friend_requests"));
            if (friendsDAO.getFriends((User) httpServletRequest.getSession().getAttribute("customer")).contains(user)) {
                httpServletRequest.setAttribute("areFriends", 1);
            }   else {
                httpServletRequest.setAttribute("areFriends",0);
            }
            if (friendsRequestDAO.receivedRequets((User) httpServletRequest.getSession().getAttribute("customer")).contains(user)) {
                httpServletRequest.setAttribute("hasRequestReceived",1);
            }   else {
                httpServletRequest.setAttribute("hasRequestReceived",0);
            }
            if (friendsRequestDAO.sentRequests((User) httpServletRequest.getSession().getAttribute("customer")).contains(user)) {
                httpServletRequest.setAttribute("hasRequestSent",1);
            }   else {
                httpServletRequest.setAttribute("hasRequestSent",0);
            }
            if (((User) httpServletRequest.getSession().getAttribute("customer")).equals(user)) {
                httpServletRequest.setAttribute("searchSelf",1);
            }   else {
                httpServletRequest.setAttribute("searchSelf",0);
            }
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/UserPOVUser.jsp").forward(httpServletRequest,httpServletResponse);
        }
    }
}
