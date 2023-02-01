package Controllers;

import Models.Constants;
import Models.Courier;
import Models.CourierStatus;
import Models.DAO.OrderDAO;
import Models.Status;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderConfirmedHandler extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!"Courier".equals(httpServletRequest.getSession().getAttribute("userType"))) {
            httpServletResponse.setStatus(405);
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        OrderDAO orders = (OrderDAO) getServletContext().getAttribute("orders");
        orders.markAsDelivered(((Courier) httpServletRequest.getSession().getAttribute("courier")).getId());
        Courier courier = (Courier) httpServletRequest.getSession().getAttribute("courier");
        Status status = new CourierStatus();
        status.setStatus(Constants.FREE);
        courier.setFree(status);
        httpServletRequest.getRequestDispatcher("WEB-INF/Views/confirmedOrder.jsp").forward(httpServletRequest,httpServletResponse);
    }
}
