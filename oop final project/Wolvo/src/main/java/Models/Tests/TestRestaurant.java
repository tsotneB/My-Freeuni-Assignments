package Models.Tests;

import Models.RequestStatus;
import Models.Restaurant;
import Models.Status;
import junit.framework.TestCase;
import static Models.Constants.*;

public class TestRestaurant extends TestCase {

    private int[] rest_id = {1, 2, 3};
    private int[] manager_id = {11, 12, 13};
    private String[] name = {"Badagani", "Machakhala", "Paviliani"};
    private String[] district = {"Saburtalo", "Didube", "Gldani"};
    private String[] address = {"Dighmis Masivi V kvartali 1a","Fanjikidze str 22a/26","3 MD Naneishvili str 20/8"};
    private float[] rating = {5.5F, 2.3F, 10.0F};
    private int[] raters = {10, 100, 16};
    private String[] status = {APPROVED, PENDING, REJECTED};
    private Restaurant[] rest;

    /**
     * sets up res and tests constructor.
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        rest = new Restaurant[3];
        for (int i = 0; i < 3; i++) {
            rest[i] = new Restaurant();
            rest[i].setId(rest_id[i]);
            rest[i].setManager_id(manager_id[i]);
            rest[i].setName(name[i]);
            rest[i].setDistrict(district[i]);
            rest[i].setAddress(address[i]);
            rest[i].setRating(rating[i]);
            rest[i].setRaters(raters[i]);
            Status add = new RequestStatus();
            add.setStatus(status[i]);
            rest[i].setAdded(add);
        }
    }

    /**
     * tests basic methods of restaurants class.
     */
    public void testRestaurants() {
        for (int i = 0; i < 3; i++) {
            assertEquals(rest_id[i], rest[i].getId());
            assertEquals(manager_id[i], rest[i].getManager_id());
            assertEquals(name[i], rest[i].getName());
            assertEquals(district[i], rest[i].getDistrict());
            assertEquals(address[i], rest[i].getAddress());
            assertEquals(rating[i], rest[i].getRating());
            assertEquals(raters[i], rest[i].getRaters());
            assertEquals(status[i], rest[i].getAdded().getStatus());
        }
    }

    /**
     * tests equal method of restaurants class.
     */
    public void testEquals() {
        Restaurant[] EQ = new Restaurant[3];
        for (int i = 0; i < 3; i++) {
            EQ[i] = new Restaurant();
            EQ[i].setId(rest_id[i]);
            EQ[i].setManager_id(manager_id[i]);
            EQ[i].setName(name[i]);
            EQ[i].setDistrict(district[i]);
            EQ[i].setAddress(address[i]);
            EQ[i].setRating(rating[i]);
            EQ[i].setRaters(raters[i]);
            Status add = new RequestStatus();
            add.setStatus(status[i]);
            EQ[i].setAdded(add);
        }
        for (int i = 0; i < 3; i++) {
            assertTrue(rest[i].equals(rest[i]));
            assertTrue(rest[i].equals(EQ[i]));
            assertFalse(rest[i].equals(null));
            assertFalse(rest[i].equals(EQ[(i + 1) % 3]));
            assertFalse(rest[i].equals("rest[i]"));
        }
    }

    public void testToString() {
        for (int i = 0; i < 3; i++) {
            String s = "Id: "+ rest[i].getId() + "n/ name: "+rest[i].getName()+ "/n manager id: " +
                    rest[i].getManager_id() +"/n district: "+ rest[i].getDistrict()+
                    "/n address: " + rest[i].getAddress() + "n/ rating: " + rest[i].getRating();
        }
    }
}
