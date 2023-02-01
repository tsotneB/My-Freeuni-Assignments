package Models.Tests;

import Models.PrivacyStatus;
import Models.Status;
import Models.User;
import Models.DAO.UserDAO;
import Models.UserStatus;
import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static Models.Constants.ADMIN;

public class TestUserDAO extends TestCase {
    private int[] id = {1,2,3,4,5};
    private String[] email = {"tbabu19@freeuni.edu.ge","tarus19@freeuni.edu.ge","achuk19@freeuni.edu.ge",
            "tbabu19(1)@freeuni.edu.ge", "tarus19(1)@freeuni.edu.ge"};
    private String[] firstNames = {"Tsotne","Temur","Akaki", "Tsotne(1)", "Temur(1)"};
    private String[] lastNames = {"Babunashvili","Arustashvili","Chukhua", "Babunashvili(1)","Arustashvili(1)"};
    private String[] passwords = {"c80adfeea5a0af6d3ab04a8dba3a8769064f0d90","5ed092a75b55d250d7cf19448ff66601d254d356",
            "db0d9ba0b474fc1a9ce19a389f4ed37df6350b3a", "c80adfeea5a0af6d3ab04a8dba3a8769064f0d90","5ed092a75b55d250d7cf19448ff66601d254d356"};
    private String[] types = {"Admin","Customer","Admin", "Customer", "Admin"};
    private String[] privacyTypes = {"Private","Friends","Private", "Public", "Private"};
    private String[] districts = {"Didube","Saburtalo","Gldani","Didube","Saburtalo"};
    private String[] addresses = {"Dighmis Masivi V kvartali 1a","Fanjikidze str 22a/26","3 MD Naneishvili str 20/8","Dighmis Masivi V kvartali 1a","Fanjikidze str 22a/26"};
    private String[] phoneNumbers = {"555685305","595055777","555725362","555685305","595055777"};
    private Connection connection;

    public void setUp() {
        if (connection != null) return;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
        }
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/wolvo_test_db?user=root&password=root");
        } catch (SQLException throwables) {
        }
    }

    private List<User> convertToUserList() {
        List<User> set = new ArrayList<>();
        for (int i=0;i<5;i++) {
            User user = new User();
            user.setId(id[i]);
            user.setEmail(email[i]);
            user.setFirstName(firstNames[i]);
            user.setLastName(lastNames[i]);
            user.setPassword(passwords[i]);
            UserStatus userStatus = new UserStatus();
            userStatus.setStatus(types[i]);
            user.setUserStatus(userStatus);
            PrivacyStatus privacyStatus = new PrivacyStatus();
            privacyStatus.setStatus(privacyTypes[i]);
            user.setPrivacyStatus(privacyStatus);
            user.setDistrict(districts[i]);
            user.setAddress(addresses[i]);
            user.setPhoneNumber(phoneNumbers[i]);
            set.add(user);
        }
        return set;
    }

    public void testgetAll() {
        assertNotNull(connection); // Connected to DB

        UserDAO userDAO = new UserDAO(connection);

        List<User> usersAnswer = convertToUserList();
        for (User user : userDAO.getAll()) {
            assert(usersAnswer.contains(user));
            usersAnswer.remove(user);
        }


        assert(usersAnswer.isEmpty());
    }

    private User convertToUser(int ind) {
        User newUser = new User();
        newUser.setId(id[ind]);
        UserStatus userStatus = new UserStatus();
        userStatus.setStatus(types[ind]);
        newUser.setUserStatus(userStatus);
        PrivacyStatus privacyStatus = new PrivacyStatus();
        privacyStatus.setStatus(privacyTypes[ind]);
        newUser.setPrivacyStatus(privacyStatus);
        newUser.setAddress(addresses[ind]);
        newUser.setDistrict(districts[ind]);
        newUser.setPhoneNumber(phoneNumbers[ind]);
        newUser.setPassword(passwords[ind]);
        newUser.setFirstName(firstNames[ind]);
        newUser.setLastName(lastNames[ind]);
        newUser.setEmail(email[ind]);
        return newUser;
    }


    public void testGetByEmail() {
        UserDAO userDAO = new UserDAO(connection);
        User firstUser = userDAO.getByEmail(email[0]);
        assertNotNull(firstUser);
        assertEquals(convertToUser(0),firstUser);

        User secondUser = userDAO.getByEmail(email[1]);
        assertNotNull(secondUser);
        assertEquals(convertToUser(1),secondUser);

        User thirdUser = userDAO.getByEmail(email[2]);
        assertNotNull(secondUser);
        assertEquals(convertToUser(2),thirdUser);
    }

    public void testNoSuchEntry() {
        UserDAO userDAO = new UserDAO(connection);

        User user = userDAO.getByEmail("randomString");
        assertNull(user);

        user = userDAO.getByEmail("tbabu18@freeuni.edu.ge");
        assertNull(user);

        user = userDAO.getByEmail("tbab19@freeuni.edu.ge");
        assertNull(user);
    }

    public void testAddEntry() {
        UserDAO userDAO = new UserDAO(connection);

        User newUser = new User();

        newUser.setEmail("test@subject.com");
        newUser.setFirstName("test 1");
        newUser.setLastName("test 2");
        newUser.setPassword("test123");
        UserStatus newUserStatus = new UserStatus();
        newUserStatus.setStatus("Customer");
        newUser.setUserStatus(newUserStatus);
        PrivacyStatus newPrivacyStatus = new PrivacyStatus();
        newPrivacyStatus.setStatus("Friends");
        newUser.setPrivacyStatus(newPrivacyStatus);
        newUser.setDistrict("TestDistrict");
        newUser.setAddress("TestAddress");
        newUser.setPhoneNumber("3241234");
        assertNull(userDAO.getByEmail(newUser.getEmail()));
        int rowsAffected = userDAO.addUser(newUser.getEmail(),newUser.getFirstName(),newUser.getLastName(),newUser.getPassword(),
                newUser.getUserStatus().getStatus(),newUser.getPrivacyStatus().getStatus(), newUser.getDistrict(),newUser.getAddress(),newUser.getPhoneNumber());
        assertEquals(1,rowsAffected);

        User usercmp = userDAO.getByEmail(newUser.getEmail());
        assertEquals(newUser,usercmp);

        int removed = userDAO.removeUser(newUser.getEmail());
        assertEquals(1,removed);

        assertNull(userDAO.getByEmail(newUser.getEmail()));
    }

    /**
     * tests getByName.
     */
    public void testGetByName() {
        UserDAO UDAO = new UserDAO(connection);
        List<User> l0 = UDAO.getByName("Tsotne Babunashvili");
        assertEquals(1, l0.size());
        assertTrue(l0.get(0).getFirstName().equals("Tsotne"));
        assertTrue(l0.get(0).getLastName().equals("Babunashvili"));
        List<User> l1 = UDAO.getByName("bla bla bla");
        assertTrue(l1.isEmpty());
        List<User> l2 = UDAO.getByName("Akaki Chukhua");
        assertEquals(1, l2.size());
        assertEquals("Akaki", l2.get(0).getFirstName());
    }

    /**
     * tests getByID.
     */
    public void testGetByID() {
        UserDAO UDAO = new UserDAO(connection);
        for (int i = 0; i < 5; i++) {
            User u = UDAO.getByID(id[i]);
            assertEquals(u, convertToUser(i));
        }
    }

    /**
     * tests makeAdmin.
     */
    public void testMakeAdmin() throws SQLException {
        UserDAO UDAO = new UserDAO(connection);
        for (int i = 0; i < 5; i++) {
            UDAO.makeAdmin(id[i]);
            assertEquals(UDAO.getByID(id[i]).getUserStatus().getStatus(), ADMIN);
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users set user_type = ? where user_id = ?;");
            statement.setString(1, types[i]);
            statement.setInt(2, id[i]);
            statement.executeUpdate();
        }
    }
}
