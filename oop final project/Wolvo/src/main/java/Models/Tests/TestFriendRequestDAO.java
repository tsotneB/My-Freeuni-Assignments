package Models.Tests;

import Models.*;
import Models.DAO.FriendsRequestDAO;
import junit.framework.TestCase;
import static Models.Constants.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestFriendRequestDAO extends TestCase {
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
     * sets connection and users.
     * @throws Exception
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
            UserStatus us = new UserStatus();
            us.setStatus(UserType[i]);
            usr.setUserStatus(us);
            Status ps = new PrivacyStatus();
            ps.setStatus(privacyTypes[i]);
            usr.setPrivacyStatus(ps);
            users[i] = usr;
        }
    }

    /**
     * tests constructor.
     */
    public void testConstructor() {
        FriendsRequestDAO FRDAO = new FriendsRequestDAO(connection);
        FRDAO = new FriendsRequestDAO(connection);
    }

    /**
     * tests received requests.
     */
    public void testReceivedRequests() {
        FriendsRequestDAO FRDAO = new FriendsRequestDAO(connection);
        List<User> received1 = new ArrayList<>();
        received1.add(users[3]);
        List<User> received2 = new ArrayList<>();
        received2.add(users[0]);
        List<User> received3 = new ArrayList<>();
        received3.add(users[0]);
        received3.add(users[1]);
        List<User> received4 = new ArrayList<>();
        received4.add(users[1]);
        received4.add(users[2]);
        List<User> received5 = new ArrayList<>();
        received5.add(users[0]);
        received5.add(users[1]);
        received5.add(users[3]);
        List<User> lst1 = FRDAO.receivedRequets(users[0]);
        boolean b1 = received1.containsAll(lst1) && lst1.containsAll(received1);
        assertTrue(b1);
        List<User> lst2 = FRDAO.receivedRequets(users[1]);
        boolean b2 = received2.containsAll(lst2) && lst2.containsAll(received2);
        assertTrue(b2);
        List<User> lst3 = FRDAO.receivedRequets(users[2]);
        boolean b3 = received3.containsAll(lst3) && lst3.containsAll(received3);
        assertTrue(b3);
        List<User> lst4 = FRDAO.receivedRequets(users[3]);
        boolean b4 = received4.containsAll(lst4) && lst4.containsAll(received4);
        assertTrue(b4);
        List<User> lst5 = FRDAO.receivedRequets(users[4]);
        boolean b5 = received5.containsAll(lst5) && lst5.containsAll(received5);
        assertTrue(b5);
    }

    /**
     * tests sent requests.
     */
    public void testSentRequests() {
        FriendsRequestDAO FRDAO = new FriendsRequestDAO(connection);
        List<User> sent1 = new ArrayList<>();
        sent1.add(users[1]);
        sent1.add(users[2]);
        sent1.add(users[4]);
        List<User> sent2 = new ArrayList<>();
        for (int i = 2; i <= 4; i++) sent2.add(users[i]);
        List<User> sent3 = new ArrayList<>();
        sent3.add(users[3]);
        List<User> sent4 = new ArrayList<>();
        sent4.add(users[0]);
        sent4.add(users[4]);
        List<User> sent5 = new ArrayList<>();
        List<User> lst1 = FRDAO.sentRequests(users[0]);
        boolean b1 = lst1.containsAll(sent1) && sent1.containsAll(lst1);
        assertTrue(b1);
        List<User> lst2 = FRDAO.sentRequests(users[1]);
        boolean b2 = lst2.containsAll(sent2) && sent2.containsAll(lst2);
        assertTrue(b2);
        List<User> lst3 = FRDAO.sentRequests(users[2]);
        boolean b3 = lst3.containsAll(sent3) && sent3.containsAll(lst3);
        assertTrue(b3);
        List<User> lst4 = FRDAO.sentRequests(users[3]);
        boolean b4 = lst4.containsAll(sent4) && sent4.containsAll(lst4);
        assertTrue(b4);
        List<User> lst5 = FRDAO.sentRequests(users[4]);
        boolean b5 = lst5.containsAll(sent5) && sent5.containsAll(lst5);
        assertTrue(b5);
    }

    /**
     * tests request statuses.
     */
    public void testRequestStatus() {
        FriendsRequestDAO FDAO = new FriendsRequestDAO(connection);
        Status rsAcc = new RequestStatus();
        Status rsRej = new RequestStatus();
        Status rsNR = new RequestStatus();
        Status rsNS = new RequestStatus();
        rsAcc.setStatus(APPROVED);
        rsRej.setStatus(REJECTED);
        rsNR.setStatus(PENDING);
        rsNS.setStatus(NOTSENT);
        assertEquals(FDAO.requestStatus(users[0], users[2]), rsAcc);
        assertEquals(FDAO.requestStatus(users[0], users[1]), rsAcc);
        assertEquals(FDAO.requestStatus(users[0], users[4]), rsRej);
        assertEquals(FDAO.requestStatus(users[1], users[2]), rsRej);
        assertEquals(FDAO.requestStatus(users[3], users[4]), rsNR);
        assertEquals(FDAO.requestStatus(users[1], users[3]), rsNR);
        assertEquals(FDAO.requestStatus(users[0], users[0]), rsNS);
        assertEquals(FDAO.requestStatus(users[4], users[2]), rsNS);
    }

    /**
     * tests friend requests removal.
     */
    public void testRemoveFriendRequest() throws SQLException {
        FriendsRequestDAO FDAO = new FriendsRequestDAO(connection);
        boolean b1 = FDAO.removeFriendsRequest(users[0], users[1]);
        assertTrue(b1);
        assertEquals(FDAO.requestStatus(users[0], users[1]).getStatus(), NOTSENT);
        PreparedStatement statement1 = connection.prepareStatement("insert into friend_requests(from_id, to_id, request_status) values(?, ?, \"Approved\");");
        statement1.setInt(1, users[0].getId());
        statement1.setInt(2, users[1].getId());
        boolean b2 = FDAO.removeFriendsRequest(users[3], users[0]);
        assertTrue((b2));
        assertEquals(FDAO.requestStatus(users[3], users[0]).getStatus(), NOTSENT);
        PreparedStatement statement2 = connection.prepareStatement("insert into Friend_requests(from_id, to_id, request_status) values(?, ?, \"Approved\");");
        statement2.setInt(1, users[3].getId());
        statement2.setInt(2, users[0].getId());
        boolean b3 = FDAO.removeFriendsRequest(users[0], users[1]);
        assertFalse(b3);
        boolean b4 = FDAO.removeFriendsRequest(users[2], users[2]);
        assertFalse(b4);
        assertEquals(FDAO.requestStatus(users[2], users[2]).getStatus(), NOTSENT);
        statement1.executeUpdate();
        statement2.executeUpdate();
    }

    /**
     * tests friend requeset insertion.
     */
    public void testInsertFriendRequest() {
        FriendsRequestDAO FDAO = new FriendsRequestDAO(connection);
        Status stAcc = new RequestStatus();
        stAcc.setStatus(APPROVED);
        assertFalse(FDAO.insertFriendRequest(users[0], users[1], stAcc));
        Status stRej = new RequestStatus();
        stRej.setStatus(REJECTED);
        assertFalse(FDAO.insertFriendRequest(users[0], users[2], stRej));
        Status stNR = new RequestStatus();
        stNR.setStatus(PENDING);
        boolean b1 = FDAO.insertFriendRequest(users[2], users[4], stNR);
        assertTrue(b1);
        assertEquals(FDAO.requestStatus(users[2], users[4]).getStatus(), PENDING);
        FDAO.removeFriendsRequest(users[2], users[4]);
    }
}
