package Controllers;

import Models.Courier;
import Models.DAO.CourierDAO;
import Models.DAO.ManagerDAO;
import Models.Manager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ManagerApprovalHandler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!"Admin".equals(httpServletRequest.getSession().getAttribute("userType"))) {
            httpServletResponse.setStatus(405);
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        Manager curr = ((ManagerDAO) getServletContext().getAttribute("managers")).
                getManagerByID(Integer.parseInt(httpServletRequest.getParameter("id")));
        httpServletRequest.setAttribute("managerToApprove",curr);
        httpServletRequest.getRequestDispatcher("WEB-INF/Views/ApproveManager.jsp").forward(httpServletRequest,httpServletResponse);
    }
}
