package Controllers;

import Models.DAO.DishDAO;
import Models.Dish;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DishRejectionHandler extends HttpServlet {
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
        dishDAO.removeDish(curr.getDish_id());
        httpServletRequest.getRequestDispatcher("WEB-INF/Views/DishRejection.jsp").forward(httpServletRequest,httpServletResponse);
    }
}
