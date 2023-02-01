package Controllers;

import Models.Courier;
import Models.DAO.CourierDAO;
import Models.DAO.DishDAO;
import Models.DAO.OrderDAO;
import Models.DAO.UserDAO;
import Models.Dish;
import Models.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderDishConfHandler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!"Customer".equals(httpServletRequest.getSession().getAttribute("userType"))) {
            httpServletResponse.setStatus(405);
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        Dish dish = ((DishDAO) getServletContext().getAttribute("dishes")).
                getDishById((Integer.parseInt(httpServletRequest.getParameter("dish"))));
        User orderRecipient = ((UserDAO) getServletContext().getAttribute("users")).
                getByID(Integer.parseInt(httpServletRequest.getParameter("orderRecipient")));
        Courier courier = ((CourierDAO) getServletContext().getAttribute("couriers")).
                getFreeCourier(orderRecipient.getDistrict());
        if (courier == null) {
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/AllOccupied.jsp").forward(httpServletRequest,httpServletResponse);
        }   else {
            OrderDAO orderDAO = (OrderDAO) getServletContext().getAttribute("orders");
            orderDAO.addOrder(orderRecipient.getId(),dish.getDish_id(),orderRecipient.getDistrict(),orderRecipient.getAddress(),
                    courier.getId(),Integer.parseInt(httpServletRequest.getParameter("quantity")));
            ((CourierDAO) getServletContext().getAttribute("couriers")).markAsOccupied(courier.getId());
            httpServletRequest.setAttribute("delivering",courier);
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/OrderConfirmed.jsp").forward(httpServletRequest,httpServletResponse);
        }
    }
}
