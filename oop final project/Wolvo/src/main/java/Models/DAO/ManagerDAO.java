package Models.DAO;

import Models.Manager;
import Models.RequestStatus;
import Models.Status;

import javax.persistence.criteria.CriteriaBuilder;
import java.awt.image.AreaAveragingScaleFilter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static Models.Constants.*;

/**
 * this class is for connection to 'managers' table
 */
public class ManagerDAO {
    private Connection connection;

    /**
     * constructor, which initializes connection.
     * @param connection type of Connection
     */
    public ManagerDAO(Connection connection) {
        this.connection = connection;
    }

    private Manager convertToManager(ResultSet rs) throws SQLException {
        Manager manager = new Manager();
        manager.set_id(rs.getInt("manager_id"));
        manager.setEmail(rs.getString("email"));
        manager.setFirstName(rs.getString("first_name"));
        manager.setLastName(rs.getString("last_name"));
        manager.setPassword(rs.getString("password"));
        manager.setRest_id(rs.getInt("rest_id"));
        manager.setPhoneNumber(rs.getString("phone_number"));
        Status status = new RequestStatus();
        status.setStatus(rs.getString("add_status"));
        manager.setAddStatus(status);
        return manager;
    }

    /**
     * inserts new Manager into the database
     * @param email
     * @param firstName
     * @param lastName
     * @param password
     * @param phoneNumber
     * @return true if the database was affected by this SQL Statement
     */
    public boolean addManager(String email, String firstName, String lastName, String password, String phoneNumber) {
        boolean b = true;
        try {
            PreparedStatement statement = connection.prepareStatement(
                   "insert into managers (email, first_name, last_name, password, phone_number) " +
                           "values (?,?,?,?,?);");
            statement.setString(1, email);
            statement.setString(2,firstName);
            statement.setString(3,lastName);
            statement.setString(4,password);
            statement.setString(5, phoneNumber);
            int i = statement.executeUpdate();
            if (i != 1) b = false;
        } catch (SQLException throwables) {
            b = false;
        }
        return b;
    }

    /**
     * removes manager with particular ID from the database
     * @param id manager_id
     */
    public void removeManager(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "delete from managers where manager_id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException throwables) {}
    }

    /**
     * @return list of all the managers
     */
    public List<Manager> getManagers() {
        List<Manager> managers = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from managers;");
            while (resultSet.next()) {
                managers.add(convertToManager(resultSet));
            }
        }  catch (SQLException throwables) {}
        return managers;
    }


    public Manager getManagerByEmail(String email) {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from managers where email = ?");
            statement.setString(1,email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return convertToManager(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    /**
     * @param id
     * @return Manager with the particular id
     */
    public Manager getManagerByID(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from managers where manager_id = ?");
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return convertToManager(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * @return list of all the managers waiting for admin approval to sign up
     */

    public List<Manager> getPendingManagers() {
        List<Manager> managers = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from managers where add_status = ?");
            statement.setString(1, "Pending");
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                managers.add(convertToManager(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return managers;
    }

    /**
     * approves manager registration (by admin)
     * @param id
     */
    public void approveManager(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE managers set add_status = ? where manager_id = ?;");
            Status status = new RequestStatus();
            status.setStatus(APPROVED);
            statement.setString(1, status.getStatus());
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * sets new rest_id to manager.
     * @param rest_id int type.
     * @param manager_id int type.
     */
    public void changeRestaurant(int rest_id, int manager_id) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE managers set rest_id = ? where manager_id = ?;");
            statement.setInt(1, rest_id);
            statement.setInt(2, manager_id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
