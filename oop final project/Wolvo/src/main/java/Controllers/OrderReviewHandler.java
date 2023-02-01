package Controllers;

import Models.DAO.DishDAO;
import Models.DAO.OrderDAO;
import Models.Dish;
import Models.Order;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderReviewHandler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!"Customer".equals(httpServletRequest.getSession().getAttribute("userType"))) {
            httpServletResponse.setStatus(405);
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        Order order = ((OrderDAO) getServletContext().getAttribute("orders")).
                getByID((Integer.parseInt(httpServletRequest.getParameter("id"))));
        httpServletRequest.setAttribute("current_order",order);
        httpServletRequest.getRequestDispatcher("WEB-INF/Views/ReviewOrder.jsp").forward(httpServletRequest,httpServletResponse);

    }
}
