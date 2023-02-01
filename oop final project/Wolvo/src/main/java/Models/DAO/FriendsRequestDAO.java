package Models.DAO;

import Models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * this class is for connection to 'friends_request' table
 */
public class FriendsRequestDAO {
    private Connection connection;

    /**
     * constructor, initializes connection.
     * @param connection Connection is for sql statement.
     */
    public FriendsRequestDAO(Connection connection){
        this.connection = connection;
    }

    /**
     * Converts given resultset ro User by id.
     * @param resultSet Resultset executed by sql statement.
     * @param id is user Id we want to convert into user.
     * @return User we are looking for.
     * @throws SQLException may be thrown while executing sql statement.
     */
    private User convertToUser(ResultSet resultSet, int id) throws SQLException {
        User usr = new User();
        usr.setId(id);
        PreparedStatement statement = connection.prepareStatement("select * from users where user_id = ?;");
        statement.setInt(1, usr.getId());
        ResultSet result = statement.executeQuery();
        if(result.next()){
            usr.setDistrict(result.getString("district"));
            usr.setEmail(result.getString("email"));
            usr.setFirstName(result.getString("first_name"));
            usr.setLastName(result.getString("last_name"));
            usr.setPassword(result.getString("password"));
            usr.setAddress(result.getString("building_address"));
            UserStatus us = new UserStatus();
            us.setStatus(result.getString("user_type"));
            usr.setUserStatus(us);
            usr.setPhoneNumber(result.getString("phone_number"));
            PrivacyStatus ps = new PrivacyStatus();
            ps.setStatus(result.getString("privacy"));
            usr.setPrivacyStatus(ps);
        }
        return usr;
    }

    /**
     * gets User and returns list of requests received.
     * @param usr User type we want to get list of requests.
     * @return list of users.
     */
    public List<User> receivedRequets(User usr) {
        List<User> received = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select from_id from friend_requests where to_id = ?;");
            statement.setInt(1, usr.getId());
            ResultSet result = statement.executeQuery();
            while(result.next()){
                received.add(convertToUser(result, result.getInt("from_id")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return received;
    }

    /**
     * gets User and returns sent requests.
     * @param usr User which we want to get sent requests.
     * @return list of users requests are sent.
     */
    public List<User> sentRequests(User usr) {
        List<User> received = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select to_id from friend_requests where from_id = ?;");
            statement.setInt(1, usr.getId());
            ResultSet result = statement.executeQuery();
            while(result.next()){
                received.add(convertToUser(result, result.getInt("to_id")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return received;
    }

    /**
     * gets two user and returns status of request,
     * returns "NotSent" if request is not sent.
     * @param usr1 User type.
     * @param usr2 User type.
     * @return RequestStatus type represents status of sent request.
     */
    public Status requestStatus(User usr1, User usr2) {
        Status status = new RequestStatus();
        try {
            PreparedStatement statement = connection.prepareStatement("select request_status from friend_requests where from_id = ? and to_id = ?;");
            statement.setInt(1, usr1.getId());
            statement.setInt(2, usr2.getId());
            ResultSet result = statement.executeQuery();
            if(result.next()){
                status.setStatus(result.getString("request_status"));
            } else {
                status.setStatus("NotSent");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return status;
    }

    /**
     * removes sent friend request.
     * @param usr1 User type.
     * @param usr2 User type.
     * @return boolean represents if request was removed.
     */
    public boolean removeFriendsRequest(User usr1, User usr2) {
        boolean answer = false;
        int removed = 0;
        try {
            PreparedStatement statement = connection.prepareStatement("delete from Friend_requests where from_id = ? and to_id = ?;");
            statement.setInt(1, usr1.getId());
            statement.setInt(2, usr2.getId());
            removed = statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (removed != 0) answer = true;
        return answer;
    }

    /**
     * inserts friend request in database between user1 and user2.
     * @param usr1 User type.
     * @param usr2 User type.
     * @param status Status type.
     * @return boolean type representing if friendship inserted successfully.
     */
    public boolean insertFriendRequest(User usr1, User usr2, Status status) {
        boolean answer = false;
        int inserted = 0;
        try {
            PreparedStatement statement = connection.prepareStatement("insert into Friend_requests(from_id, to_id, request_status) values(?, ?, ?);");
            statement.setInt(1, usr1.getId());
            statement.setInt(2, usr2.getId());
            statement.setString(3, status.getStatus());
            inserted = statement.executeUpdate();
        } catch (SQLException throwables) {
        }
        if (inserted != 0) answer = true;
        return answer;
    }
}