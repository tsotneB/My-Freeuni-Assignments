package Models.Tests;

import Models.CourierStatus;
import Models.Courier;
import Models.RequestStatus;
import Models.Status;
import junit.framework.TestCase;
import static Models.Constants.*;

public class TestCourier extends TestCase {
    private int[] courier_id = {1, 2, 3};
    private String[] email = {"tarus19@freeuni.edu.ge", "tbabu19@freeuni.edu.ge", "achuk19@freeuni.edu.ge"};
    private String[] first_name = {"temur", "tsotne", "akaki"};
    private String[] last_name = {"arustashvili", "babunashivili", "chukhua"};
    private String[] passwords = {"c80adfeea5a0af6d3ab04a8dba3a8769064f0d90","5ed092a75b55d250d7cf19448ff66601d254d356",
            "db0d9ba0b474fc1a9ce19a389f4ed37df6350b3a", "c80adfeea5a0af6d3ab04a8dba3a8769064f0d90","5ed092a75b55d250d7cf19448ff66601d254d356"};
    private String[] phoneNumber = {"595-05-57-77", "555-68-53-05", "555-72-53-62"};
    private String[] district = {"Saburtalo", "Didube", "Gldani"};
    private float[] rating = {0F, 5.0F, 5.0F};
    private int[] raters = {0, 10, 10};
    private int[] completedOrders = {100, 10, 9};
    private String[] isAdded = {APPROVED, PENDING, REJECTED};
    private String[] isFree = {FREE, OCCUPIED, FREE};
    private int[] currOrder = {10, 11, 12};

    public void testConstructor() {
        Courier courier = new Courier();
        courier = new Courier();
    }

    public void testCourier() {
        Courier[] couriers = new Courier[3];
        for (int i = 0; i < 3; i++) {
            couriers[i] = new Courier();
            couriers[i].setId(courier_id[i]);
            couriers[i].setEmail(email[i]);
            couriers[i].setFirstName(first_name[i]);
            couriers[i].setLastName(last_name[i]);
            couriers[i].setPassword(passwords[i]);
            couriers[i].setPhoneNumber(phoneNumber[i]);
            couriers[i].setDistrict(district[i]);
            couriers[i].setRating(rating[i]);
            couriers[i].setRaters(raters[i]);
            couriers[i].setCompletedOrders(completedOrders[i]);
            Status add = new RequestStatus();
            add.setStatus(isAdded[i]);
            couriers[i].setAdded(add);
            Status free = new CourierStatus();
            free.setStatus(isFree[i]);
            couriers[i].setFree(free);
            couriers[i].setCurrOrder(currOrder[i]);
        }
        for (int i = 0; i < 3; i++) {
            assertEquals(courier_id[i], couriers[i].getId());
            assertEquals(email[i], couriers[i].getEmail());
            assertEquals(first_name[i], couriers[i].getFirstName());
            assertEquals(last_name[i], couriers[i].getLastName());
            assertEquals(passwords[i], couriers[i].getPassword());
            assertEquals(phoneNumber[i], couriers[i].getPhoneNumber());
            assertEquals(district[i], couriers[i].getDistrict());
            assertEquals(rating[i], couriers[i].getRating());
            assertEquals(raters[i], couriers[i].getRaters());
            assertEquals(completedOrders[i], couriers[i].getCompletedOrders());
            Status add = new RequestStatus();
            add.setStatus(isAdded[i]);
            assertEquals(add, couriers[i].getAdded());
            Status free = new CourierStatus();
            free.setStatus(isFree[i]);
            assertEquals(free, couriers[i].getFree());
            assertEquals(currOrder[i], couriers[i].getCurrOrder());
        }
    }

    public void testEquals() {
        Courier[] couriers = new Courier[3];
        for (int i = 0; i < 3; i++) {
            couriers[i] = new Courier();
            couriers[i].setId(courier_id[i]);
            couriers[i].setEmail(email[i]);
            couriers[i].setFirstName(first_name[i]);
            couriers[i].setLastName(last_name[i]);
            couriers[i].setPassword(passwords[i]);
            couriers[i].setPhoneNumber(phoneNumber[i]);
            couriers[i].setDistrict(district[i]);
            couriers[i].setRating(rating[i]);
            couriers[i].setRaters(raters[i]);
            couriers[i].setCompletedOrders(completedOrders[i]);
            Status add = new RequestStatus();
            add.setStatus(isAdded[i]);
            couriers[i].setAdded(add);
            Status free = new CourierStatus();
            free.setStatus(isFree[i]);
            couriers[i].setFree(free);
            couriers[i].setCurrOrder(currOrder[i]);
        }
        Courier[] EQ = new Courier[3];
        for (int i = 0; i < 3; i++) {
            EQ[i] = new Courier();
            EQ[i].setId(courier_id[i]);
            EQ[i].setEmail(email[i]);
            EQ[i].setFirstName(first_name[i]);
            EQ[i].setLastName(last_name[i]);
            EQ[i].setPassword(passwords[i]);
            EQ[i].setPhoneNumber(phoneNumber[i]);
            EQ[i].setDistrict(district[i]);
            EQ[i].setRating(rating[i]);
            EQ[i].setRaters(raters[i]);
            EQ[i].setCompletedOrders(completedOrders[i]);
            Status add = new RequestStatus();
            add.setStatus(isAdded[i]);
            EQ[i].setAdded(add);
            Status free = new CourierStatus();
            free.setStatus(isFree[i]);
            EQ[i].setFree(free);
            EQ[i].setCurrOrder(currOrder[i]);
        }
        for (int i = 0; i < 3; i++) {
            assertTrue(couriers[i].equals(couriers[i]));
            assertTrue(couriers[i].equals(EQ[i]));
            assertFalse(couriers[i].equals(null));
            assertFalse(couriers[i].equals(EQ[(i + 1) % 3]));
            assertFalse(couriers[i].equals("true"));
        }
    }

    public void testToString() {
        Courier[] couriers = new Courier[3];
        for (int i = 0; i < 3; i++) {
            couriers[i] = new Courier();
            couriers[i].setId(courier_id[i]);
            couriers[i].setEmail(email[i]);
            couriers[i].setFirstName(first_name[i]);
            couriers[i].setLastName(last_name[i]);
            couriers[i].setPassword(passwords[i]);
            couriers[i].setPhoneNumber(phoneNumber[i]);
            couriers[i].setDistrict(district[i]);
            couriers[i].setRating(rating[i]);
            couriers[i].setRaters(raters[i]);
            couriers[i].setCompletedOrders(completedOrders[i]);
            Status add = new RequestStatus();
            add.setStatus(isAdded[i]);
            couriers[i].setAdded(add);
            Status free = new CourierStatus();
            free.setStatus(isFree[i]);
            couriers[i].setFree(free);
            couriers[i].setCurrOrder(currOrder[i]);
        }
        for (int i = 0; i < 3; i++) {
            String s = couriers[i].getId() + " " + couriers[i].getEmail() + " " + couriers[i].getFirstName() + " " +
                    couriers[i].getLastName() + " "+ couriers[i].getPassword() + " " + " "+ couriers[i].getDistrict()
                    + " "+ couriers[i].getPhoneNumber() + " " + couriers[i].getRating() + " " +
                    couriers[i].getRaters() + " " +couriers[i].getCompletedOrders() + " " + couriers[i].getCurrOrder() + " " +
                    couriers[i].getAdded().getStatus() + " " + couriers[i].getFree().getStatus();
            assertEquals(s, couriers[i].toString());
        }
    }
}
