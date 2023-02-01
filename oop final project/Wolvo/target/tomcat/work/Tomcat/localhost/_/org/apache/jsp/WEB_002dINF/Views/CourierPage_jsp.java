/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.47
 * Generated at: 2021-08-18 20:46:37 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.WEB_002dINF.Views;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.List;
import Models.DAO.*;
import Models.*;

public final class CourierPage_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("    <link rel = \"stylesheet\" href = \"../../Resources/style.css\" />\r\n");
      out.write("    <title>Wolvo</title>\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("<div class = \"courierInfo\">\r\n");
      out.write("    <label>Hello, ");
      out.print( ((Courier) session.getAttribute("courier")).getFirstName() );
      out.write("!</label> <br>\r\n");
      out.write("    <label>Lets have a look at your profile!</label> <br>\r\n");
      out.write("    <label>First Name: ");
      out.print( ((Courier) session.getAttribute("courier")).getFirstName());
      out.write("</label> <br>\r\n");
      out.write("    <label>Last Name: ");
      out.print( ((Courier) session.getAttribute("courier")).getLastName());
      out.write("</label> <br>\r\n");
      out.write("    <label>Email: ");
      out.print( ((Courier) session.getAttribute("courier")).getEmail());
      out.write("</label> <br>\r\n");
      out.write("    <label>Phone Number: ");
      out.print( ((Courier) session.getAttribute("courier")).getPhoneNumber());
      out.write(" </label> <br>\r\n");
      out.write("    <label>Your working district: ");
      out.print( ((Courier) session.getAttribute("courier")).getDistrict());
      out.write("</label> <br>\r\n");
      out.write("    <label>Number of completed orders: ");
      out.print( ((Courier) session.getAttribute("courier")).getCompletedOrders());
      out.write("</label> <br>\r\n");
      out.write("    <label>Rating: ");
      out.print( ((Courier) session.getAttribute("courier")).getRating());
      out.write("</label>\r\n");
      out.write("</div>\r\n");
      out.write("<div class = \"courierReviews\">\r\n");
      out.write("    <label>Let's see your reviews!</label> <br>\r\n");
      out.write("    ");
 List<Review> courRevs = ((ReviewDAO) application.getAttribute("reviews")).
            getCourierReviews((Courier) session.getAttribute("courier"));
        if (courRevs.isEmpty()) {
    
      out.write("\r\n");
      out.write("    <label>You don't have any reviews.</label>\r\n");
      out.write("    ");
 }
    else { boolean b = false;
      out.write("\r\n");
      out.write("    ");
 for (Review review : courRevs) {
        if (review.getCourierRating() != -1 || !"".equals(review.getCourierText())) { b = true;
    
      out.write("\r\n");
      out.write("    <label>User ");
      out.print( ((UserDAO) application.getAttribute("users")).
            getByID(review.getUser()).getFirstName());
      out.write(" \r\n");
      out.write("        ");
      out.print( ((UserDAO) application.getAttribute("users")).
                getByID(review.getUser()).getLastName());
      out.write("  </label>\r\n");
      out.write("                ");
 if (review.getCourierRating() != -1) 
      out.write("\r\n");
      out.write("                    <label>gave you a ");
      out.print(review.getCourierRating());
      out.write("  out of 5!</label> <br>\r\n");
      out.write("                ");
 if (!"".equals(review.getCourierText())) 
      out.write("\r\n");
      out.write("                    <label>User made a comment: ");
      out.print(review.getCourierText());
      out.write("</label>\r\n");
      out.write("            ");
}
      out.write("\r\n");
      out.write("        ");
}
          if (b == false) { 
      out.write("\r\n");
      out.write("                <label>You don't have any reviews.</label>\r\n");
      out.write("          ");
 }
        
      out.write("\r\n");
      out.write("    ");
}
      out.write("\r\n");
      out.write("</div>\r\n");
      out.write("<div class = \"currentOrder\">\r\n");
      out.write("    ");
 if (((Courier) session.getAttribute("courier")).getFree().getStatus().equals("Free")) { 
      out.write("\r\n");
      out.write("    <label>You don't have any orders right now.</label> <br>\r\n");
      out.write("    ");
 }
    else { 
      out.write("\r\n");
      out.write("    <label>           Your current order:</label> <br>\r\n");
      out.write("    ");
 Order currentOrder = ((OrderDAO) application.getAttribute("orders")).
            getCouriersCurrentOrder(((Courier) session.getAttribute("courier")).getId());

       Dish currDish = ((DishDAO) application.getAttribute("dishes")).getDishById(currentOrder.getDish());
       if (currDish != null) {
       Restaurant currentRestaurant = ((RestaurantDAO) application.getAttribute("restaurants")).getRestaurantById(
               ((DishDAO) application.getAttribute("dishes")).getDishById(currentOrder.getDish()).getRest_id());
       Dish currentDish = ((DishDAO) application.getAttribute("dishes")).getDishById(currentOrder.getDish());
    
      out.write("\r\n");
      out.write("    <label>Restaurant: ");
      out.print( currentRestaurant.getName());
      out.write(" </label> <br>\r\n");
      out.write("    <label>Dish: ");
      out.print(currentDish.getName());
      out.write("</label> <br>\r\n");
      out.write("    <label>Price: ");
      out.print(currentDish.getPrice());
      out.write("</label> <br>\r\n");
      out.write("    <label>Address: ");
      out.print(currentOrder.getAddress());
      out.write("</label> <br>\r\n");
      out.write("    <a href=\"confirmOrder\">Mark order as delivered</a>\r\n");
      out.write("    ");
 }
     }
      out.write("\r\n");
      out.write("\r\n");
      out.write("</div>\r\n");
      out.write("<div class = \"logout\">\r\n");
      out.write("    <form action = \"SignOut\" method = \"post\">\r\n");
      out.write("        <input type=\"submit\" value=\"Sign Out\" id = \"SignOut\" name = \"SignOut\"/>\r\n");
      out.write("    </form>\r\n");
      out.write("</div>\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}