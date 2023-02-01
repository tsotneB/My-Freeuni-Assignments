package Controllers;

import Models.DAO.DishDAO;
import Models.DAO.RestaurantDAO;
import Models.Dish;
import Models.Restaurant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderFromRestaurantHandler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!"Customer".equals(httpServletRequest.getSession().getAttribute("userType"))) {
            httpServletResponse.setStatus(405);
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        Restaurant restaurant = ((RestaurantDAO) getServletContext().getAttribute("restaurants")).
                getRestaurantById(Integer.parseInt(httpServletRequest.getParameter("id")));
        httpServletRequest.setAttribute("orderFromRest",restaurant);
        httpServletRequest.getRequestDispatcher("WEB-INF/Views/OrderRestaurant.jsp").forward(httpServletRequest,httpServletResponse);
    }
}
