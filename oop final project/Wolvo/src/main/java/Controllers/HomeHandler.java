package Controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import static Models.Constants.*;

public class HomeHandler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String userType = (String) httpServletRequest.getSession().getAttribute("userType");
        if (userType == null) {
            httpServletRequest.getRequestDispatcher("WEB-INF/Views/login.jsp").forward(httpServletRequest, httpServletResponse);
        } else {
            if (userType.equals(ADMIN)) {
                httpServletRequest.getRequestDispatcher("WEB-INF/Views/AdminPage.jsp").forward(httpServletRequest, httpServletResponse);
            } else if (userType.equals(CUSTOMER)) {
                httpServletRequest.getRequestDispatcher("WEB-INF/Views/CustomerPage.jsp").forward(httpServletRequest, httpServletResponse);
            } else if (userType.equals(MANAGER)) {
                httpServletRequest.getRequestDispatcher("WEB-INF/Views/ManagerPage.jsp").forward(httpServletRequest, httpServletResponse);
            } else {
                httpServletRequest.getRequestDispatcher("WEB-INF/Views/CourierPage.jsp").forward(httpServletRequest, httpServletResponse);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        doGet(httpServletRequest, httpServletResponse);
    }
}
