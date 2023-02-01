package Controllers;

import Models.DAO.FriendsDAO;
import Models.DAO.FriendsRequestDAO;
import Models.DAO.UserDAO;
import Models.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ConfirmRequestHandler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!"Customer".equals(httpServletRequest.getSession().getAttribute("userType"))) {
            httpServletResponse.setStatus(405);
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        User usrTo = (User) httpServletRequest.getSession().getAttribute("customer");
        User userFrom = ((UserDAO) getServletContext().getAttribute("users")).
                getByID(Integer.parseInt(httpServletRequest.getParameter("id")));
        FriendsRequestDAO friendsRequestDAO = (FriendsRequestDAO) getServletContext().getAttribute("friend_requests");
        if (!friendsRequestDAO.receivedRequets(usrTo).contains(userFrom)) {
            httpServletResponse.setStatus(410);
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        FriendsDAO friendsDAO = (FriendsDAO) getServletContext().getAttribute("friends");
        friendsDAO.insertFriends(userFrom,usrTo);
        friendsRequestDAO.removeFriendsRequest(userFrom,usrTo);
        httpServletRequest.getRequestDispatcher("WEB-INF/Views/RequestApproved.jsp").forward(httpServletRequest,httpServletResponse);
    }
}
