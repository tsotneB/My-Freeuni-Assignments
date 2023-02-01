package Models.Tests;

import Models.DAO.ManagerDAO;
import Models.Manager;
import Models.RequestStatus;
import Models.Status;
import junit.framework.TestCase;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestManagerDAO extends TestCase {
    private Connection connection;

    private int ids[] = {501,502,503,504,505};
    private String[] emails = {"tbabu19@freeuni.edu.ge","tarus19@freeuni.edu.ge","achuk19@freeuni.edu.ge",
            "tbabu19(1)@freeuni.edu.ge","tarus19(1)@freeuni.edu.ge"};
    private String[] firstNames = {"Tsotne","Temur","Akaki","Tsotne(1)","Temur(1)"};
    private String[] lastNames = {"Babunashvili","Arustashvili","Chukhua","Babunashvili(1)","Arustashvili(1)"};
    private String[] passwords = {"c80adfeea5a0af6d3ab04a8dba3a8769064f0d90","5ed092a75b55d250d7cf19448ff66601d254d356",
                    "db0d9ba0b474fc1a9ce19a389f4ed37df6350b3a","c80adfeea5a0af6d3ab04a8dba3a8769064f0d90",
            "5ed092a75b55d250d7cf19448ff66601d254d356"};
    private int[] rest_ids = {1001,1002,1003,1004,1005};
    private String[] phoneNumbers = {"555-68-53-05","595-05-57-77","555-72-53-62","555-68-53-05","595-05-57-77"};

    /**
     * sets up connection
     */
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

    public void testConstructor() {
        ManagerDAO managerDAO = new ManagerDAO(connection);
        assertNotNull(managerDAO);
    }

    private List<Manager> convertToManagers() {
        List<Manager> managers = new ArrayList<>();
        for (int i=0;i<5;i++) {
            Manager manager = new Manager();
            manager.set_id(ids[i]);
            manager.setEmail(emails[i]);
            manager.setFirstName(firstNames[i]);
            manager.setLastName(lastNames[i]);
            manager.setPassword(passwords[i]);
            manager.setRest_id(rest_ids[i]);
            manager.setPhoneNumber(phoneNumbers[i]);
            Status status = new RequestStatus();
            manager.setAddStatus(status);
            managers.add(manager);
        }
        return managers;
    }

    public void testGetAllandPendings() {
        ManagerDAO managerDAO = new ManagerDAO(connection);
        List<Manager> managers= managerDAO.getManagers();
        List<Manager> managerscmp = convertToManagers();
        assert(managers.containsAll(managerscmp));
        assert(managerscmp.containsAll(managers));
        managers = managerDAO.getPendingManagers();
        assert(managers.containsAll(managerscmp));
        assert(managerscmp.containsAll(managers));
    }

    public void testGetByID() {
        ManagerDAO managerDAO = new ManagerDAO(connection);
        assertNull(managerDAO.getManagerByID(323));
        assertNull(managerDAO.getManagerByID(1));
        Manager mansearch = managerDAO.getManagerByID(503);
        Manager manager = new Manager();
        manager.set_id(ids[2]);
        manager.setEmail(emails[2]);
        manager.setFirstName(firstNames[2]);
        manager.setLastName(lastNames[2]);
        manager.setPassword(passwords[2]);
        manager.setRest_id(rest_ids[2]);
        manager.setPhoneNumber(phoneNumbers[2]);
        Status status = new RequestStatus();
        manager.setAddStatus(status);
        assertEquals(manager,mansearch);
    }

    public void testAddApprove() throws SQLException {
        ManagerDAO managerDAO = new ManagerDAO(connection);
        assertNull(managerDAO.getManagerByEmail("test@test"));
        boolean k = managerDAO.addManager("test@test","test","test","testp","44");
        assertTrue(k);
        PreparedStatement statement = connection.prepareStatement("delete from managers where email =?;");
        statement.setString(1,"test@test");
        int i = statement.executeUpdate();
        assertEquals(1, i);
        assertNull(managerDAO.getManagerByEmail("test@test"));
        boolean k1 = managerDAO.addManager("test@test","test","test","testp","44");
        assertTrue(k1);
        Manager manager = managerDAO.getManagerByEmail("test@test");
        assertNotNull(manager);
        managerDAO.approveManager(manager.getId());
        manager = managerDAO.getManagerByEmail("test@test");
        assertEquals("Approved",manager.getAddStatus().getStatus());
        statement = connection.prepareStatement("delete from managers where email =?;");
        statement.setString(1,"test@test");
        i = statement.executeUpdate();
        assertEquals(1, i);
    }

    public void testgetByEmailandRemove(){
        ManagerDAO managerDAO = new ManagerDAO(connection);
        Manager managersearch = managerDAO.getManagerByEmail("tbabu19(1)@freeuni.edu.ge");
        Manager manager = new Manager();
        manager.set_id(ids[3]);
        manager.setEmail(emails[3]);
        manager.setFirstName(firstNames[3]);
        manager.setLastName(lastNames[3]);
        manager.setPassword(passwords[3]);
        manager.setRest_id(rest_ids[3]);
        manager.setPhoneNumber(phoneNumbers[3]);
        Status status = new RequestStatus();
        manager.setAddStatus(status);
        assertEquals(managersearch,manager);
        boolean k = managerDAO.addManager("test@test","test","test","ttt","123");
        assertTrue(k);
        Manager man = managerDAO.getManagerByEmail("test@test");
        managerDAO.removeManager(man.getId());
        assertNull(managerDAO.getManagerByEmail("test@test"));
    }

    /**
     * changes manager restaurant.
     */
    public void testChangeRestaurant() {
        ManagerDAO MDAO = new ManagerDAO(connection);
        for (int i = 0; i < 5; i++) {
            assertEquals(MDAO.getManagerByID(ids[i]).getRest_id(), rest_ids[i]);
            MDAO.changeRestaurant(rest_ids[(i + 1) % 5], ids[i]);
            assertEquals(MDAO.getManagerByID(ids[i]).getRest_id(), rest_ids[(i + 1) % 5]);
            MDAO.changeRestaurant(rest_ids[i], ids[i]);
            assertEquals(MDAO.getManagerByID(ids[i]).getRest_id(), rest_ids[i]);
        }
    }
}
