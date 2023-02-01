package Models.DAO;

import Models.*;

import java.sql.*;
import java.util.*;

import static Models.Constants.*;

public class CourierDAO {
    private Connection connection;

    public CourierDAO(Connection connection){
        this.connection = connection;
    }

    /**
     *
     * @return List of all the currently registered couriers
     */
    public List<Courier> getCouriers(){
        List<Courier> couriers = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from couriers;");
            while (resultSet.next()) {
                couriers.add(convertToCourier(resultSet));
            }
        } catch (SQLException throwables) {}
        return couriers;
    }

    /**
     *
     * @param email of courier (submitted during registration)
     * @return Courier with that particular email address
     */

    public Courier getCourierByEmail(String email){
        try {
            PreparedStatement statement = connection.prepareStatement("select * from couriers where email = ?");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return convertToCourier(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param district
     * @return List of all the couriers working in that particular district
     */

    public List<Courier> getDistrictCouriers(String district){
        List<Courier> couriers = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from couriers where district = ?");
            statement.setString(1, district);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                couriers.add(convertToCourier(resultSet));
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return couriers;
    }

    /**
     *
     * @param email
     * @param firstName
     * @param lastName
     * @param district
     * @param password
     * @param phoneNumber
     * Inserts new courier in the database with data received as a parameters
     */
    public boolean addCourier(String email, String firstName, String lastName, String district, String password, String phoneNumber){
        boolean added = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "insert into couriers (email, first_name, last_name, district, password, phone_number, rating, raters, completed_orders, curr_status, add_status) " +
                            "values (?,?,?,?,?,?,?,?,?,?,?);");
            statement.setString(1, email);
            statement.setString(2,firstName);
            statement.setString(3,lastName);
            statement.setString(4,district);
            statement.setString(5,password);
            statement.setString(6,phoneNumber);
            statement.setFloat(7, 0F);
            statement.setInt(8, 0);
            statement.setInt(9, 0);
            statement.setString(10, FREE);
            statement.setString(11, PENDING);
            int i = statement.executeUpdate();
            if (i != 0) added = true;
        } catch (SQLException throwables) {}
        return added;
    }

    /**
     *
     * @param courier
     * approves registration of the particular courier (will be called by Admin)
     */
    public void approveCourier(Courier courier){
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE couriers set add_status = ? where courier_id = ?;");
            Status status = new RequestStatus();
            status.setStatus(APPROVED);
            statement.setString(1, status.getStatus());
            statement.setInt(2, courier.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    /**
     *
     * @param courier
     * marks that the particular courier is no longer free (i.e. working on the order)
     */

    public void acceptOrder(Courier courier) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE couriers set curr_status = ? where courier_id = ?;");
            Status status = new CourierStatus();
            status.setStatus(OCCUPIED);
            statement.setString(1, status.getStatus());
            statement.setInt(2, courier.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     *
     * @param courier
     * @param rate
     * updates the rating of the particular courier with the new value (called when user rates courier after the delivery)
     */
    public void updateCourier(Courier courier, int rate){
        int rated = 0;
        if (rate >= 0) rated = 1;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE couriers set rating = ?, raters = ?, completed_orders = ?, curr_status = ? where courier_id = ?;");
            float nRating = courier.getRaters() + rated == 0 ? 0F :
                    (courier.getRating() * courier.getRaters() + (float)rate * rated) / (courier.getRaters() + rated);
            statement.setFloat(1, nRating);
            statement.setInt(2, courier.getRaters() + rated);
            statement.setInt(3, courier.getCompletedOrders() + 1);
            Status status = new CourierStatus();
            status.setStatus(FREE);
            statement.setString(4, status.getStatus());
            statement.setInt(5, courier.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     *
     * @return the list of couriers whose registration is pending admin approval
     */

    public List<Courier> getPendingCouriers(){
        List<Courier> couriers = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from couriers where add_status = ?");
            Status status = new RequestStatus();
            status.setStatus(PENDING);
            statement.setString(1, status.getStatus());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                couriers.add(convertToCourier(resultSet));
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return couriers;
    }

    /**
     *
     * @param id courier_id
     * @return the courier with that particular courier_id
     */
    public Courier getCourierById(int id){
        try {
            PreparedStatement statement = connection.prepareStatement("select * from couriers where courier_id = ?");
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return convertToCourier(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param rs
     * @return Courier object created with the data taken from the database
     * i.e. it takes a resultset of query and converts it to Courier object
     * @throws SQLException
     */
    private Courier convertToCourier(ResultSet rs) throws SQLException{
        Courier c = new Courier();
        c.setId(rs.getInt("courier_id"));
        c.setEmail(rs.getString("email"));
        c.setFirstName(rs.getString("first_name"));
        c.setLastName(rs.getString("last_name"));
        c.setPassword(rs.getString("password"));
        c.setDistrict(rs.getString("district"));
        c.setPhoneNumber(rs.getString("phone_number"));
        c.setRating(rs.getFloat("rating"));
        c.setCompletedOrders(rs.getInt("completed_orders"));
        c.setRaters(rs.getInt("raters"));
        Status add = new RequestStatus();
        add.setStatus(rs.getString("add_status"));
        c.setAdded(add);
        Status free = new CourierStatus();
        free.setStatus(rs.getString("curr_status"));
        c.setFree(free);
        c.setCurrOrder(rs.getInt("curr_order"));
        return c;
    }

    /**
     * marks courier as free.
     * @param courier_id int type.
     */
    public void markAsFree(int courier_id) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE couriers set curr_status = ? where courier_id = ?;");
            Status status = new CourierStatus();
            status.setStatus(FREE);
            statement.setString(1, status.getStatus());
            statement.setInt(2, courier_id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * marks courier as occupied.
     * @param courier_id int type.
     */
    public void markAsOccupied(int courier_id) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE couriers set curr_status = ? where courier_id = ?;");
            Status status = new CourierStatus();
            status.setStatus(OCCUPIED);
            statement.setString(1, status.getStatus());
            statement.setInt(2, courier_id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * removes courier specified by id.
     * @param id int type id of courier.
     */
    public void removeCourier(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "Delete from couriers where courier_id = ?;");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * retrutns free courier from district specified.
     * @param district String type.
     * @return Courier type.
     */
    public Courier getFreeCourier(String district) {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from couriers where district = ?" +
                    "and curr_status = ?");
            statement.setString(1,district);
            statement.setString(2,FREE);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return convertToCourier(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
