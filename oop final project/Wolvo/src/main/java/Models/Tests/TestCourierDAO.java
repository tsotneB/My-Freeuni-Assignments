package Models.Tests;

import Models.*;
import Models.DAO.CourierDAO;
import junit.framework.TestCase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Models.Constants.*;

public class TestCourierDAO extends TestCase {

    private Connection connection;
    private int[] id = {1, 2, 3, 4, 5};
    private String[] email = {"tbabu19@freeuni.edu.ge","tarus19@freeuni.edu.ge","achuk19@freeuni.edu.ge",
            "tbabu19(1)@freeuni.edu.ge", "tarus19(1)@freeuni.edu.ge"};
    private String[] first_name = {"Tsotne","Temur","Akaki", "Tsotne(1)", "Temur(1)"};
    private String[] last_name = {"Babunashvili","Arustashvili","Chukhua", "Babunashvili(1)","Arustashvili(1)"};
    private String[] password = {"c80adfeea5a0af6d3ab04a8dba3a8769064f0d90","5ed092a75b55d250d7cf19448ff66601d254d356",
            "db0d9ba0b474fc1a9ce19a389f4ed37df6350b3a", "c80adfeea5a0af6d3ab04a8dba3a8769064f0d90","5ed092a75b55d250d7cf19448ff66601d254d356"};
    private String[] districts = {"Didube","Saburtalo","Gldani","Didube","Saburtalo"};
    private String[] phoneNumbers = {"555-68-53-05","595-05-57-77","555-72-53-62","555-68-53-05","595-05-57-77"};
    private float[] rating = {0F, 5.0F, 5.0F, 3.4F, 2.4F};
    private int[] raters = {0, 10, 10, 375, 103};
    private int[] completedOrders = {100, 10, 9, 1078, 1999};
    private String[] isAdded = {APPROVED, PENDING, REJECTED, APPROVED, APPROVED};
    private String[] isFree = {FREE, OCCUPIED, OCCUPIED, FREE, OCCUPIED};
    private int[] currOrder = {103, 104, 105, 106, 107};
    private Courier[] couriers;

    /**
     * initializes connection and couriers.
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(
            "jdbc:mysql://localhost/wolvo_test_db?user=root&password=root");
        couriers = new Courier[5];
        for (int i = 0; i < 5; i++) {
            couriers[i] = new Courier();
            couriers[i].setId(id[i]);
            couriers[i].setEmail(email[i]);
            couriers[i].setFirstName(first_name[i]);
            couriers[i].setLastName(last_name[i]);
            couriers[i].setPassword(password[i]);
            couriers[i].setDistrict(districts[i]);
            couriers[i].setPhoneNumber(phoneNumbers[i]);
            couriers[i].setRating(rating[i]);
            couriers[i].setRaters(raters[i]);
            couriers[i].setCompletedOrders(completedOrders[i]);
            Status rs = new RequestStatus();
            rs.setStatus(isAdded[i]);
            couriers[i].setAdded(rs);
            Status cs = new CourierStatus();
            cs.setStatus(isFree[i]);
            couriers[i].setFree(cs);
            couriers[i].setCurrOrder(currOrder[i]);
        }
    }

    /**
     * tests getCouriers method.
     */
    public void testGetCouriers() {
        CourierDAO CDAO = new CourierDAO(connection);
        List<Courier> l = CDAO.getCouriers();
        List<Courier> actual = Arrays.asList(couriers);
        assertTrue(l.containsAll(actual));
        assertTrue(actual.containsAll(l));
    }

    /**
     * tests getCourierByEmail method
     */
    public void testGetCourierByEmail() {
        CourierDAO CDAO = new CourierDAO(connection);
        for (int i = 0; i < 5; i++) {
            Courier c = CDAO.getCourierByEmail(email[0]);
            assertTrue(c.equals(couriers[0]));
        }
    }

    /**
     * tests districtCouriers method.
     */
    public void testDistrictCouriers() {
        CourierDAO CDAO = new CourierDAO(connection);
        String[] ds = {"Saburtalo", "Didube", "Gldani"};
        for (int i = 0; i < 3; i++) {
            List<Courier> actual = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                if (districts[j].equals(ds[i]))
                    actual.add(couriers[j]);
            }
            List<Courier> l = CDAO.getDistrictCouriers(ds[i]);
            assertTrue(actual.containsAll(l));
            assertTrue(l.containsAll(actual));
        }
    }

    /**
     * tests addCourier method.
     */
    public void testAddCourier() throws SQLException {
        CourierDAO CDAO = new CourierDAO(connection);
        boolean b1 = CDAO.addCourier("b@d.com", "bla", "blu", "Saburtalo", "pass", "595");
        assertTrue(b1);
        boolean b2 = CDAO.addCourier("b@d.com", "bla", "blu", "Saburtalo", "pass", "595");
        assertFalse(b2);
        PreparedStatement statement = connection.prepareStatement("delete from couriers where email = \'b@d.com\'");
        int i = statement.executeUpdate();
        assertEquals(1, i);
        assertNull(CDAO.getCourierByEmail("b@d.com"));
    }

    /**
     * tests ApproveCourier.
     */
    public void testApproveCourier() throws SQLException {
        CourierDAO CDAO = new CourierDAO(connection);
        for (int i = 0; i < 5; i++) {
            CDAO.approveCourier(couriers[i]);
            String email = couriers[i].getEmail();
            Courier c = CDAO.getCourierByEmail(email);
            assertEquals(APPROVED, c.getAdded().getStatus());
            PreparedStatement statement = connection.prepareStatement("update couriers set add_status = ? where email = ?");
            statement.setString(1, isAdded[i]);
            statement.setString(2, email);
            statement.executeUpdate();
            c = CDAO.getCourierByEmail(couriers[i].getEmail());
            assertEquals(isAdded[i], c.getAdded().getStatus());
        }
    }

    /**
     * tests AcceptOrder.
     */
    public void testAcceptOrder() throws SQLException {
        CourierDAO CDAO = new CourierDAO(connection);
        for (int i = 0; i < 5; i++) {
            CDAO.acceptOrder(couriers[i]);
            Courier c = CDAO.getCourierByEmail(email[i]);
            assertEquals(OCCUPIED, c.getFree().getStatus());
            PreparedStatement statement = connection.prepareStatement("update couriers set curr_status = ? where email = ?");
            statement.setString(1, isFree[i]);
            statement.setString(2, email[i]);
            statement.executeUpdate();
            c = CDAO.getCourierByEmail(email[i]);
            assertEquals(isFree[i], c.getFree().getStatus());
        }
    }

    /**
     * tests UpdateCourier.
     */
    public void testUpdateCourier() throws SQLException {
        CourierDAO CDAO = new CourierDAO(connection);
        for (int i = 0; i < 5; i++) {
            CDAO.updateCourier(couriers[i], -1);
            Courier c = CDAO.getCourierById(id[i]);
            assertEquals(c.getRaters(), couriers[i].getRaters());
            assertEquals(c.getRating(), couriers[i].getRating());
            assertEquals(c.getFree().getStatus(), FREE);
            assertEquals(c.getCompletedOrders(), completedOrders[i] + 1);
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE couriers set completed_orders = ?, curr_status = ? where courier_id = ?;");
            statement.setInt(1, completedOrders[i]);
            statement.setString(2, isFree[i]);
            statement.setInt(3, id[i]);
            statement.executeUpdate();
        }

        CDAO.addCourier("b@d.com", "bla", "blu", "Saburtalo", "pass", "595");
        Courier c = CDAO.getCourierByEmail("b@d.com");
        assertEquals(c.getRaters(), 0);
        assertEquals(c.getRating(), 0F);
        CDAO.updateCourier(c, 3);
        c = CDAO.getCourierByEmail("b@d.com");
        assertEquals(c.getRaters(), 1);
        assertEquals(c.getRating(), 3F);
        PreparedStatement statement = connection.prepareStatement(
                "delete from couriers where email = \'b@d.com\';");
        statement.executeUpdate();
    }

    /**
     * tests pendingCouriers.
     */
    public void testPendingCouriers() {
        CourierDAO CDAO = new CourierDAO(connection);
        List<Courier> actual = new ArrayList<>();
        actual.add(couriers[1]);
        List<Courier> l = CDAO.getPendingCouriers();
        assertTrue(l.containsAll(actual));
        assertTrue(actual.containsAll(l));
    }

    /**
     * tests getCourierByID.
     */
    public void testGetCourierByID() {
        CourierDAO CDAO = new CourierDAO(connection);
        for (int i = 0; i < 5; i++) {
            Courier c = CDAO.getCourierById(id[i]);
            assertTrue(c.equals(couriers[i]));
        }
    }

    /**
     * tests markAsFree.
     */
    public void testMarkAsFree() {
        CourierDAO CDAO = new CourierDAO(connection);
        for (int i = 0; i < 5; i++) {
            CDAO.markAsFree(id[i]);
            assertEquals(CDAO.getCourierById(id[i]).getFree().getStatus(), FREE);
        }
        for (int i = 0; i < 5; i++) {
            if (isFree[i].equals(OCCUPIED))
                CDAO.markAsOccupied(id[i]);
        }
    }

    /**
     * tests markAsOccupied.
     */
    public void testMarkAsOccupied() {
        CourierDAO CDAO = new CourierDAO(connection);
        for (int i = 0; i < 5; i++) {
            CDAO.markAsOccupied(id[i]);
            assertEquals(CDAO.getCourierById(id[i]).getFree().getStatus(), OCCUPIED);
        }
        for (int i = 0; i < 5; i++) {
            if (isFree[i].equals(FREE))
                CDAO.markAsFree(id[i]);
        }
    }

    /**
     * tests removeCourier.
     */
    public void testRemoveCourier() throws SQLException {
        CourierDAO CDAO = new CourierDAO(connection);
        for (int i = 0; i < 5; i++) {
            assertNotNull(CDAO.getCourierById(id[i]));
            CDAO.removeCourier(id[i]);
            assertNull(CDAO.getCourierById(id[i]));
            PreparedStatement statement = connection.prepareStatement(
                    "insert into couriers (courier_id, email, first_name, last_name, district, password, phone_number, rating, raters, completed_orders, curr_status, add_status, curr_order) " +
                            "values (?,?,?,?,?,?,?,?,?,?,?,?,?);");
            statement.setInt(1, id[i]);
            statement.setString(2, email[i]);
            statement.setString(3,first_name[i]);
            statement.setString(4,last_name[i]);
            statement.setString(5,districts[i]);
            statement.setString(6,password[i]);
            statement.setString(7,phoneNumbers[i]);
            statement.setFloat(8, rating[i]);
            statement.setInt(9, raters[i]);
            statement.setInt(10, completedOrders[i]);
            statement.setString(11, isFree[i]);
            statement.setString(12, isAdded[i]);
            statement.setInt(13, currOrder[i]);
            int j = statement.executeUpdate();
            assertEquals(1, j);
        }
    }

    /**
     * tests getFreeCourier.
     */
    public void testGetFreeCourier() {
        CourierDAO CDAO = new CourierDAO(connection);
        for (String d : districts) {
            Courier c = CDAO.getFreeCourier(d);
            if ("Didube".equals(d)) {
                assertNotNull(c);
                assertEquals(c.getDistrict(), d);
            } else {
                assertNull(c);
            }
        }
    }
}
