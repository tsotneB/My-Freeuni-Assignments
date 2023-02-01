package Controllers;

import Models.Courier;
import Models.DAO.*;
import Models.Dish;
import Models.Order;
import Models.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderReviewConfHandler extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (!"Customer".equals(httpServletRequest.getSession().getAttribute("userType"))) {
            httpServletResponse.setStatus(405);
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/ErrorPage.jsp").forward(httpServletRequest, httpServletResponse);
            return;
        }
        Order ord = ((OrderDAO) getServletContext().getAttribute("orders")).
                getByID(Integer.parseInt(httpServletRequest.getParameter("reviewID")));
        int courrev = -1;
        if (httpServletRequest.getParameter("reviewCour") != null) {
            courrev = Integer.parseInt(httpServletRequest.getParameter("reviewCour"));
        }

        int dishrev = -1;
        if (httpServletRequest.getParameter("reviewDish") != null) {
            dishrev = Integer.parseInt(httpServletRequest.getParameter("reviewDish"));
        }

        String courstr = "";
        if (httpServletRequest.getParameter("courtxt") != null) {
            courstr = httpServletRequest.getParameter("courtxt");
        }

        String dishstr = "";
        if (httpServletRequest.getParameter("dishtxt") != null) {
            dishstr = httpServletRequest.getParameter("dishtxt");
        }

        ReviewDAO reviewDAO = ((ReviewDAO) getServletContext().getAttribute("reviews"));
        User usr = ((UserDAO) getServletContext().getAttribute("users")).getByID(ord.getUser());
        Dish dish = ((DishDAO) getServletContext().getAttribute("dishes")).getDishById(ord.getDish());
        Courier courier = ((CourierDAO) getServletContext().getAttribute("couriers")).getCourierById(ord.getCourier());
        reviewDAO.addReview(ord.getId(),usr,dish,courier,dishrev,courrev,courstr,dishstr);
        httpServletRequest.getRequestDispatcher("WEB-INF/Views/ReviewDone.jsp").forward(httpServletRequest,httpServletResponse);
    }
}
