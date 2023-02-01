package Controllers;

import Models.Courier;
import Models.DAO.CourierDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CourierRejectionHandler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!"Admin".equals(httpServletRequest.getSession().getAttribute("userType"))) {
            httpServletResponse.setStatus(405);
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        Courier curr = ((CourierDAO) getServletContext().getAttribute("couriers")).
                getCourierById(Integer.parseInt(httpServletRequest.getParameter("id")));
        CourierDAO courierDAO = (CourierDAO) getServletContext().getAttribute("couriers");
        courierDAO.removeCourier(curr.getId());
        httpServletRequest.getRequestDispatcher("WEB-INF/Views/CourierRejection.jsp").forward(httpServletRequest,httpServletResponse);
    }
}
