package Controllers;

import Models.Courier;
import Models.DAO.CourierDAO;
import Models.DAO.DishDAO;
import Models.Dish;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DishApprovalConfHandler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!"Admin".equals(httpServletRequest.getSession().getAttribute("userType"))) {
            httpServletResponse.setStatus(405);
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        Dish curr = ((DishDAO) getServletContext().getAttribute("dishes")).
                getDishById(Integer.parseInt(httpServletRequest.getParameter("id")));
        DishDAO dishDAO = (DishDAO) getServletContext().getAttribute("dishes");
        dishDAO.approveDish(curr.getDish_id());
        httpServletRequest.getRequestDispatcher("WEB-INF/Views/ConfirmedDishApproval.jsp").forward(httpServletRequest,httpServletResponse);
    }
}
