package Controllers;

import Models.*;
import Models.DAO.DishDAO;
import Models.DAO.OrderDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DishDeletionHandler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!"Manager".equals(httpServletRequest.getSession().getAttribute("userType"))) {
            httpServletResponse.setStatus(405);
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        DishDAO dishDAO = (DishDAO) getServletContext().getAttribute("dishes");
        if (dishDAO.getDishById(Integer.valueOf(httpServletRequest.getParameter("id"))) == null) {
            httpServletResponse.setStatus(410);
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        dishDAO.removeDish(Integer.valueOf(httpServletRequest.getParameter("id")));
        httpServletRequest.getRequestDispatcher("WEB-INF/Views/RemoveDish.jsp").forward(httpServletRequest,httpServletResponse);
    }
}
