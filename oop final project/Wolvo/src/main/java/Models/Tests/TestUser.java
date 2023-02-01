package Models.Tests;

import Models.PrivacyStatus;
import Models.Status;
import Models.User;
import Models.UserStatus;
import junit.framework.TestCase;

import static Models.Constants.*;

public class TestUser extends TestCase {

    private int[] id = {1,2,3,4,5};
    private String[] email = {"tbabu19@freeuni.edu.ge","tarus19@freeuni.edu.ge","achuk19@freeuni.edu.ge"};
    private String[] firstNames = {"Tsotne","Temur","Akaki"};
    private String[] lastNames = {"Babunashvili","Arustashvili","Chukhua",};
    private String[] passwords = {"c80adfeea5a0af6d3ab04a8dba3a8769064f0d90","5ed092a75b55d250d7cf19448ff66601d254d356",
            "db0d9ba0b474fc1a9ce19a389f4ed37df6350b3a"};
    private String[] types = {ADMIN,CUSTOMER,ADMIN};
    private String[] privacyTypes = {PRIVATE, FRIENDS, PRIVATE};
    private String[] districts = {"Didube","Saburtalo","Gldani"};
    private String[] addresses = {"Dighmis Masivi V kvartali 1a","Fanjikidze str 22a/26","3 MD Naneishvili str 20/8"};
    private String[] phoneNumbers = {"555685305","595055777","555725362"};

    /**
     * tests constructor.
     */
    public void testConstructor() {
        User user = new User();
        user = new User();
    }

    /**
     * tests main setter and getter methods of user class.
     */
    public void testUser() {
        User[] users = new User[3];
        for (int i = 0; i < 3; i++) {
            users[i] = new User();
            users[i].setId(id[i]);
            users[i].setEmail(email[i]);
            users[i].setFirstName(firstNames[i]);
            users[i].setLastName(lastNames[i]);
            users[i].setPassword(passwords[i]);
            Status us = new UserStatus();
            us.setStatus(types[i]);
            users[i].setUserStatus(us);
            Status ps = new PrivacyStatus();
            ps.setStatus(privacyTypes[i]);
            users[i].setPrivacyStatus(ps);
            users[i].setDistrict(districts[i]);
            users[i].setAddress(addresses[i]);
            users[i].setPhoneNumber(phoneNumbers[i]);
        }
        for (int i = 0; i < 3; i++) {
            assertEquals(users[i].getId(), id[i]);
            assertEquals(users[i].getEmail(), email[i]);
            assertEquals(users[i].getFirstName(), firstNames[i]);
            assertEquals(users[i].getLastName(), lastNames[i]);
            assertEquals(users[i].getPassword(), passwords[i]);
            assertEquals(users[i].getUserStatus().getStatus(), types[i]);
            assertEquals(users[i].getPrivacyStatus().getStatus(), privacyTypes[i]);
            assertEquals(users[i].getDistrict(), districts[i]);
            assertEquals(users[i].getAddress(), addresses[i]);
            assertEquals(users[i].getPhoneNumber(), phoneNumbers[i]);
        }
    }

    public void testEquals() {
        User[] users = new User[3];
        for (int i = 0; i < 3; i++) {
            users[i] = new User();
            users[i].setId(id[i]);
            users[i].setEmail(email[i]);
            users[i].setFirstName(firstNames[i]);
            users[i].setLastName(lastNames[i]);
            users[i].setPassword(passwords[i]);
            Status us = new UserStatus();
            us.setStatus(types[i]);
            users[i].setUserStatus(us);
            Status ps = new PrivacyStatus();
            ps.setStatus(privacyTypes[i]);
            users[i].setPrivacyStatus(ps);
            users[i].setDistrict(districts[i]);
            users[i].setAddress(addresses[i]);
            users[i].setPhoneNumber(phoneNumbers[i]);
        }
        User[] EQ = new User[3];
        for (int i = 0; i < 3; i++) {
            EQ[i] = new User();
            EQ[i].setId(id[i]);
            EQ[i].setEmail(email[i]);
            EQ[i].setFirstName(firstNames[i]);
            EQ[i].setLastName(lastNames[i]);
            EQ[i].setPassword(passwords[i]);
            Status us = new UserStatus();
            us.setStatus(types[i]);
            EQ[i].setUserStatus(us);
            Status ps = new PrivacyStatus();
            ps.setStatus(privacyTypes[i]);
            EQ[i].setPrivacyStatus(ps);
            EQ[i].setDistrict(districts[i]);
            EQ[i].setAddress(addresses[i]);
            EQ[i].setPhoneNumber(phoneNumbers[i]);
        }
        for (int i = 0; i < 3; i++) {
            assertTrue(users[i].equals(users[i]));
            assertTrue(users[i].equals(EQ[i]));
            assertFalse(users[i].equals(EQ[(i + 1) % 3]));
            assertFalse(users[i].equals(null));
            assertFalse(users[i].equals("true"));
        }
    }

    public void testToString() {
        User[] users = new User[3];
        for (int i = 0; i < 3; i++) {
            users[i] = new User();
            users[i].setId(id[i]);
            users[i].setEmail(email[i]);
            users[i].setFirstName(firstNames[i]);
            users[i].setLastName(lastNames[i]);
            users[i].setPassword(passwords[i]);
            Status us = new UserStatus();
            us.setStatus(types[i]);
            users[i].setUserStatus(us);
            Status ps = new PrivacyStatus();
            ps.setStatus(privacyTypes[i]);
            users[i].setPrivacyStatus(ps);
            users[i].setDistrict(districts[i]);
            users[i].setAddress(addresses[i]);
            users[i].setPhoneNumber(phoneNumbers[i]);
        }
        for (int i = 0; i < 3; i++) {
            String s = users[i].getFirstName() + " " + users[i].getLastName() + ", " + users[i].getId() + ", " +
                    users[i].getEmail() + ", " + users[i].getPhoneNumber() + ", "
                    + users[i].getDistrict() + " " + users[i].getAddress() + " " +
                    users[i].getUserStatus().getStatus() + " " + users[i].getPrivacyStatus().getStatus();
            assertEquals(s, users[i].toString());
        }
    }
}
