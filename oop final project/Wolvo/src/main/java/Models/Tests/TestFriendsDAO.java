package Models.Tests;

import Models.*;
import Models.DAO.FriendsDAO;
import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestFriendsDAO extends TestCase {

    private Connection connection;
    private int[] id = {1, 2, 3, 4, 5};
    private String[] email = {"tbabu19@freeuni.edu.ge","tarus19@freeuni.edu.ge","achuk19@freeuni.edu.ge",
                            "tbabu19(1)@freeuni.edu.ge", "tarus19(1)@freeuni.edu.ge"};
    private String[] first_name = {"Tsotne","Temur","Akaki", "Tsotne(1)", "Temur(1)"};
    private String[] last_name = {"Babunashvili","Arustashvili","Chukhua", "Babunashvili(1)","Arustashvili(1)"};
    private String[] password = {"c80adfeea5a0af6d3ab04a8dba3a8769064f0d90","5ed092a75b55d250d7cf19448ff66601d254d356",
            "db0d9ba0b474fc1a9ce19a389f4ed37df6350b3a", "c80adfeea5a0af6d3ab04a8dba3a8769064f0d90","5ed092a75b55d250d7cf19448ff66601d254d356"};
    private String[] UserType = {"Admin","Customer","Admin", "Customer", "Admin"};
    private String[] privacyTypes = {"Private","Friends","Private", "Public", "Private"};
    private String[] districts = {"Didube","Saburtalo","Gldani","Didube","Saburtalo"};
    private String[] addresses = {"Dighmis Masivi V kvartali 1a","Fanjikidze str 22a/26","3 MD Naneishvili str 20/8","Dighmis Masivi V kvartali 1a","Fanjikidze str 22a/26"};
    private String[] phoneNumbers = {"555685305","595055777","555725362","555685305","595055777"};
    private User[] users;

    /**
     * sets up connection and users.
     * @throws Exception in case connection was wrong.
     */
    @Override
    protected void setUp() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost/wolvo_test_db?user=root&password=root");
        users = new User[5];
        for (int i = 0; i < 5; i++) {
            User usr = new User();
            usr.setId(id[i]);
            usr.setFirstName(first_name[i]);
            usr.setLastName(last_name[i]);
            usr.setEmail(email[i]);
            usr.setPassword(password[i]);
            usr.setDistrict(districts[i]);
            usr.setAddress(addresses[i]);
            usr.setPhoneNumber(phoneNumbers[i]);
            Status us = new UserStatus();
            us.setStatus(UserType[i]);
            usr.setUserStatus(us);
            Status ps = new PrivacyStatus();
            ps.setStatus(privacyTypes[i]);
            usr.setPrivacyStatus(ps);
            users[i] = usr;
        }
    }

    /**
     * tests constructor
     */
 //   @Test
    public void testConstructor() {
        FriendsDAO FDAO = new FriendsDAO(connection);
        FDAO = new FriendsDAO(connection);
    }

    /**
     * tests get friends method.
     */
  //  @Test
    public void testGetFriends() {
        List<User> f1 = new ArrayList<>();
        for (int i = 1; i <= 3; i++) f1.add(users[i]);
        List<User> f2 = new ArrayList<>();
        f2.add(users[0]);
        f2.add(users[4]);
        List<User> f3 = new ArrayList<>();
        f3.add(users[0]);
        f3.add(users[3]);
        List<User> f4 = new ArrayList<>();
        f4.add(users[0]);
        f4.add(users[2]);
        List<User> f5 = new ArrayList<>();
        f5.add(users[1]);
        FriendsDAO FDAO = new FriendsDAO(connection);
        List<User> s1 = FDAO.getFriends(users[0]);
        for (User usr : f1) {
            assertTrue(s1.contains(usr));
            s1.remove(usr);
        }
        assertTrue(s1.isEmpty());
        List<User> s2 = FDAO.getFriends(users[1]);
        for (User usr : f2) {
            assertTrue(s2.contains(usr));
            s2.remove(usr);
        }
        assertTrue(s2.isEmpty());
        List<User> s3 = FDAO.getFriends(users[2]);
        for (User usr : f3) {
            assertTrue(s3.contains(usr));
            s3.remove(usr);
        }
        assertTrue(s3.isEmpty());
        List<User> s4 = FDAO.getFriends(users[3]);
        for (User usr : f4) {
            assertTrue(s4.contains(usr));
            s4.remove(usr);
        }
        assertTrue(s4.isEmpty());
        List<User> s5 = FDAO.getFriends(users[4]);
        for (User usr : f5) {
            assertTrue(s5.contains(usr));
            s5.remove(usr);
        }
        assertTrue(s5.isEmpty());
    }

    /**
     * tests friendship method.
     */
    public void testFriendship() {
        FriendsDAO FDAO = new FriendsDAO(connection);
        assertTrue(FDAO.friendship(users[0], users[1]));
        assertTrue(FDAO.friendship(users[0], users[2]));
        assertTrue(FDAO.friendship(users[3], users[0]));
        assertFalse(FDAO.friendship(users[4], users[0]));
        assertFalse(FDAO.friendship(users[0], users[0]));
        assertFalse(FDAO.friendship(users[1], users[2]));
        assertTrue(FDAO.friendship(users[4], users[1]));
        assertTrue(FDAO.friendship(users[2], users[3]));
        assertFalse(FDAO.friendship(users[2], users[4]));
    }

    /**
     * tests friendship removal.
     */
    public void testUpdateFriendship() throws SQLException {
        FriendsDAO FDAO = new FriendsDAO(connection);
        boolean b1 = FDAO.removeFriendship(users[0], users[1]);
        assertTrue(b1);
        assertFalse(FDAO.friendship(users[0], users[1]));
        PreparedStatement statement1 = connection.prepareStatement("insert into Friends values(?, ?)");
        statement1.setInt(1, users[0].getId());
        statement1.setInt(2, users[1].getId());
        boolean b2 = FDAO.removeFriendship(users[3], users[0]);
        assertTrue((b2));
        assertFalse(FDAO.friendship(users[3], users[0]));
        PreparedStatement statement2 = connection.prepareStatement("insert into Friends values(?, ?)");
        statement2.setInt(1, users[0].getId());
        statement2.setInt(2, users[3].getId());
        boolean b3 = FDAO.removeFriendship(users[0], users[1]);
        assertFalse(b3);
        boolean b4 = FDAO.removeFriendship(users[2], users[2]);
        assertFalse(b4);
        assertFalse(FDAO.friendship(users[2], users[2]));
        statement1.executeUpdate();
        statement2.executeUpdate();
    }

    /**
     * tests friends insertion.
     */
    public void testFriendsInsertion() {
        FriendsDAO FDAO = new FriendsDAO(connection);
        assertFalse(FDAO.insertFriends(users[0], users[1]));
        assertFalse(FDAO.insertFriends(users[2], users[0]));
        boolean b1 = FDAO.insertFriends(users[0], users[4]);
        assertTrue(b1);
        assertTrue(FDAO.friendship(users[0], users[4]));
        FDAO.removeFriendship(users[0], users[4]);
    }
}
