package Models.Tests;

import Models.DAO.OrderDAO;
import Models.Order;
import Models.OrderStatus;
import Models.Status;
import junit.framework.TestCase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Models.Constants.*;

public class TestOrderDAO extends TestCase {

    Connection connection;
    private int[] id = {103, 104, 105, 106, 107};
    private int[] user = {1, 2, 3, 4, 5};
    private int[] dish = {210, 211, 212, 213, 214};
    private Timestamp[] orderDate = {Timestamp.valueOf("2008-11-11 13:23:44"), Timestamp.valueOf("2009-11-11 13:23:44"),
            Timestamp.valueOf("2010-11-11 13:23:44"), Timestamp.valueOf("2011-11-11 13:23:44"),
            Timestamp.valueOf("2012-11-11 13:23:44")};
    private Timestamp[] receiveDate = {Timestamp.valueOf("2008-11-11 14:23:44"), Timestamp.valueOf("2009-11-11 14:23:44"),
            null, null, Timestamp.valueOf("2013-11-11 13:23:44")};
    private String[] orderStatus = {DELIVERED, DELIVERED, ONWAY, NOTRECEIVE, DELIVERED};
    private String[] district = {"Didube", "Saburtalo", "Gldani", "Didube", "Saburtalo"};
    private String[] address = {"Dighmis Masivi V kvartali 1a", "Fanjikidze str 22a/26", "3 MD Naneishvili str 20/8",
            "Dighmis Masivi V kvartali 1a", "Fanjikidze str 22a/26"};
    private int[] courier = {1, 2, 3, 4, 5};
    private int[] quantity = {1, 100, 3, 1, 1};
    private Order[] orders;

    /**
     * sets up orders.
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost/wolvo_test_db?user=root&password=root");
        orders = new Order[5];
        for (int i = 0; i < 5; i++) {
            orders[i] = new Order();
            orders[i].setId(id[i]);
            orders[i].setUser(user[i]);
            orders[i].setDish(dish[i]);
            orders[i].setOrderDate(orderDate[i]);
            orders[i].setReceiveDate(receiveDate[i]);
            Status os = new OrderStatus();
            os.setStatus(orderStatus[i]);
            orders[i].setOrderStatus(os);
            orders[i].setDistrict(district[i]);
            orders[i].setAddress(address[i]);
            orders[i].setCourier(courier[i]);
            orders[i].setQuantity(quantity[i]);
        }
    }

    /**
     * tests getOrders.
     */
    public void testGetOrders() {
        OrderDAO ODAO = new OrderDAO(connection);
        List<Order> l = ODAO.getOrders();
        List<Order> actual = Arrays.asList(orders);
        assertTrue(l.containsAll(actual));
        assertTrue(actual.containsAll(l));
    }

    /**
     * tests getUserOrders.
     */
    public void testGetUserOrders() {
        OrderDAO ODAO = new OrderDAO(connection);
        for (int i : user) {
            List<Order> l = ODAO.getUserOrders(i);
            List<Order> actual = new ArrayList<>();
            for (Order o : orders) {
                if (o.getUser() == i) actual.add(o);
            }
            assertTrue(l.containsAll(actual));
            assertTrue(actual.containsAll(l));
        }
    }

    /**
     * tests addOrder.
     */
    public void testAddOrder() throws SQLException {
        OrderDAO ODAO = new OrderDAO(connection);
        boolean b = ODAO.addOrder(1, 212, "Didube", "some address", 5, 120);
        assertTrue(b);
        List<Order> l = ODAO.getUserOrders(1);
        assertEquals(2, l.size());
        PreparedStatement statement = connection.prepareStatement(
                "delete from orders where user_id = 1 and dish_id = 212 and location = \"some address\"");
        statement.executeUpdate();
        l = ODAO.getUserOrders(1);
        assertEquals(1, l.size());
    }

    /**
     * tests getCouriersCurrentOrder.
     */
    public void testGetCouriersCurrentOrder() {
        OrderDAO ODAO = new OrderDAO(connection);
        for (int i = 0; i < 5; i++) {
            Order order = ODAO.getCouriersCurrentOrder(courier[i]);
            if (!orderStatus[i].equals("OnWay")) {
                assertEquals(null, order);
            } else {
                assertEquals(orders[i], order);
            }
        }
    }

    /**
     * tests markAsDelivered.
     */
    public void testMarkAsDelivered() throws SQLException {
        OrderDAO ODAO = new OrderDAO(connection);
        for (int i = 0; i < 5; i++) {
            ODAO.markAsDelivered(courier[i]);
            if (orderStatus[i] != NOTRECEIVE)
                assertEquals(ODAO.getByID(id[i]).getOrderStatus().getStatus(), DELIVERED);
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE orders set order_status = ?, receive_date = ? where courier_id= ?;");
            statement.setString(1, orderStatus[i]);
            statement.setTimestamp(2, receiveDate[i]);
            statement.setInt(3, courier[i]);
            statement.executeUpdate();
        }
    }

    /**
     * tests getByID.
     */
    public void testGetByID() {
        OrderDAO ODAO = new OrderDAO(connection);
        for (int i = 0; i < 5; i++) {
            Order o = ODAO.getByID(id[i]);
            assertEquals(o, orders[i]);
        }
        assertEquals(null, ODAO.getByID(-1));
    }
}
