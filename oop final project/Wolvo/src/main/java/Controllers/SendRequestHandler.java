package Controllers;

import Models.*;
import Models.DAO.FriendsRequestDAO;
import Models.DAO.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SendRequestHandler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!"Customer".equals(httpServletRequest.getSession().getAttribute("userType"))) {
            httpServletResponse.setStatus(405);
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        User user = ((UserDAO) getServletContext().getAttribute("users")).
                getByID(Integer.parseInt(httpServletRequest.getParameter("id")));
        User userFrom = (User) httpServletRequest.getSession().getAttribute("customer");
        FriendsRequestDAO friendsRequestDAO = (FriendsRequestDAO) (getServletContext().getAttribute("friend_requests"));
        Status rq = new RequestStatus();
        rq.setStatus(Constants.PENDING);
        friendsRequestDAO.insertFriendRequest(userFrom,user,rq);
        httpServletRequest.getRequestDispatcher("WEB-INF/Views/RequestSent.jsp").forward(httpServletRequest,httpServletResponse);
    }
}
