package Models.Tests;

import Models.Order;
import Models.OrderStatus;
import Models.Status;
import junit.framework.TestCase;

import java.sql.SQLData;
import java.sql.Timestamp;
import java.util.Date;

import static Models.Constants.*;

public class TestOrder extends TestCase {
    private Order[] orders;
    private int[] ids = {101, 102, 103};
    private int[] users = {5, 6, 7};
    private int[] dishes = {1000, 3000, 1};
    private Timestamp[] orderDate = {Timestamp.valueOf("2018-09-01 09:01:15"),
            Timestamp.valueOf("2021-08-10 04:31:10"), Timestamp.valueOf("2020-03-03 03:12:11")};
    private Timestamp[] receiveDate = {Timestamp.valueOf("2018-09-01 09:02:15"),
            Timestamp.valueOf("2021-08-10 04:32:10"), Timestamp.valueOf("2020-03-03 03:12:11")};
    private String[] district = {"Saburtalo", "Didube", "Gldani"};
    private String[] addresses = {"Dighmis Masivi V kvartali 1a","Fanjikidze str 22a/26","3 MD Naneishvili str 20/8"};
    private int[] courier = {2, 4, 10};
    private int[] quantity = {20, 100, 1};
    private String[] orderStatus = {DELIVERED, ONWAY, NOTRECEIVE};

    /**
     * test constructor and setup orders.
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        orders = new Order[3];
        for (int i = 0; i < 3; i++) {
            orders[i] = new Order();
            orders[i].setId(ids[i]);
            orders[i].setUser(users[i]);
            orders[i].setDish(dishes[i]);
            orders[i].setOrderDate(orderDate[i]);
            orders[i].setDistrict(district[i]);
            orders[i].setAddress(addresses[i]);
            orders[i].setCourier(courier[i]);
            orders[i].setReceiveDate(receiveDate[i]);
            orders[i].setQuantity(quantity[i]);
            Status os = new OrderStatus();
            os.setStatus(orderStatus[i]);
            orders[i].setOrderStatus(os);
        }
    }

    /**
     * test basic methods of order class.
     */
    public void testOrder() {
        for (int i = 0; i < 3; i++) {
            assertEquals(ids[i], orders[i].getId());
            assertEquals(users[i], orders[i].getUser());
            assertEquals(dishes[i], orders[i].getDish());
            assertEquals(orderDate[i], orders[i].getOrderDate());
            assertEquals(district[i], orders[i].getDistrict());
            assertEquals(addresses[i], orders[i].getAddress());
            assertEquals(courier[i], orders[i].getCourier());
            assertEquals(receiveDate[i], orders[i].getReceiveDate());
            assertEquals(quantity[i], orders[i].getQuantity());
            assertEquals(orderStatus[i], orders[i].getOrderStatus().getStatus());
        }
    }

    /**
     * test equal method of order class.
     */
    public void testEquals() {
        Order[] EQ = new Order[3];
        for (int i = 0; i < 3; i++) {
            EQ[i] = new Order();
            EQ[i].setId(ids[i]);
            EQ[i].setUser(users[i]);
            EQ[i].setDish(dishes[i]);
            EQ[i].setOrderDate(orderDate[i]);
            EQ[i].setDistrict(district[i]);
            EQ[i].setAddress(addresses[i]);
            EQ[i].setCourier(courier[i]);
            EQ[i].setReceiveDate(receiveDate[i]);
            EQ[i].setQuantity(quantity[i]);
            Status os = new OrderStatus();
            os.setStatus(orderStatus[i]);
            EQ[i].setOrderStatus(os);
        }
        for (int i = 0; i < 3; i++) {
            assertTrue(orders[i].equals(orders[i]));
            assertTrue(orders[i].equals(EQ[i]));
            assertFalse(orders[i].equals(EQ[(i + 1) % 3]));
            assertFalse(orders[i].equals(null));
            assertFalse(orders[i].equals("orders[i]"));
        }
    }

    /**
     * test toString method of order class.
     */
    public void testToString() {
        for (int i = 0; i < 3; i++) {
            String s = orders[i].getId() + " " + orders[i].getUser() + " " + orders[i].getDish() +
                    " " + orders[i].getCourier() + " "+ orders[i].getAddress() + " " + " "+
                    orders[i].getDistrict() + " "+ orders[i].getOrderDate().toString() + " " +
                     orders[i].getOrderStatus().getStatus() + " " +orders[i].getQuantity();
            assertEquals(s, orders[i].toString());
        }
    }
}
