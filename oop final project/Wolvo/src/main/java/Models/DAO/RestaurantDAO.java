package Models.DAO;

import Models.RequestStatus;
import Models.Restaurant;
import Models.Status;

import static Models.Constants.*;

import java.sql.*;
import java.util.*;

public class RestaurantDAO{
    private Connection connection;

    public RestaurantDAO(Connection connection){
        this.connection = connection;
    }

    /**
     *
     * @return list of all the restaurants currently registered in the database
     */

    public List<Restaurant> getRestaurants(){
        List<Restaurant> restaurants = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from restaurants;");
            while (resultSet.next()) {
                restaurants.add(convertToRestaurant(resultSet));
            }
        } catch (SQLException throwables) {}
        return restaurants;
    }

    /**
     *
     * @param id restaurant_id
     * @return Restaurant object with that particular ID
     */

    public Restaurant getRestaurantById(int id){
        try {
            PreparedStatement statement = connection.prepareStatement("select * from restaurants where rest_id = ?");
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return convertToRestaurant(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param name
     * @param manager
     * @param district
     * @param address
     * Inserts new restaurant in the database with data received as a parameters
     */

    public boolean addRestaurant(String name, int manager, String district, String address){
        boolean b = true;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "insert into restaurants (manager_id, name, district, address, rating, raters, add_status) values (?,?,?,?,?,?,?);");
            statement.setInt(1, manager);
            statement.setString(2,name);
            statement.setString(3,district);
            statement.setString(4,address);
            statement.setFloat(5, 0F);
            statement.setInt(6, 0);
            statement.setString(7, PENDING);
            int i = statement.executeUpdate();
            if (i != 1) b = false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return b;
    }

    /**
     *
     * @return the list of all the restaurants pending admin approval (every change made by managers must be approved by admin)
     */
    public List<Restaurant> getPendingRestaurants(){
        List<Restaurant> restaurants = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from restaurants where add_status = ?");
            statement.setString(1, PENDING);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                restaurants.add(convertToRestaurant(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return restaurants;
    }

    /**
     * @param restaurant
     * @param rate
     * updates the rating of the particular restaurant with the new value (called when user rates restaurant after the delivery)
     */

    public void updateRestaurant(Restaurant restaurant, int rate){
        if (rate < 0) return;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE restaurants set rating = ?, raters = ? where rest_id = ?;");
            statement.setFloat(1, (restaurant.getRating() * restaurant.getRaters() + 1.0F * rate)/(restaurant.getRaters() + 1));
            statement.setInt(2, restaurant.getRaters() + 1);
            statement.setInt(3, restaurant.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     *
     * @param restaurant Restaurant type object
     * approves addition of the particular restaurant (will be called by Admin)
     */
    public void approveRestaurant(Restaurant restaurant){
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE restaurants set add_status = ? where rest_id = ?;");
            statement.setString(1, APPROVED);
            statement.setInt(2, restaurant.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     *
     * @param rs
     * @return Restaurant object created with the data taken from the database
     * i.e. it takes a resultset of query and converts it to Restaurant object
     * @throws SQLException
     */
    private Restaurant convertToRestaurant(ResultSet rs) throws SQLException {
        Restaurant r = new Restaurant();
        r.setId(rs.getInt("rest_id"));
        r.setManager_id(rs.getInt("manager_id"));
        r.setName(rs.getString("name"));
        r.setRating(rs.getFloat("rating"));
        r.setDistrict(rs.getString("district"));
        r.setAddress(rs.getString("address"));
        Status status = new RequestStatus();
        status.setStatus(rs.getString("add_status"));
        r.setAdded(status);
        r.setRaters(rs.getInt("raters"));
        return r;
    }

    /**
     * returns restaurant by manager id.
     * @param manager_id int type.
     * @return Restaurant type.
     */
    public Restaurant getRestaurantByManager(int manager_id) {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from restaurants where manager_id = ?");
            statement.setInt(1,manager_id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return convertToRestaurant(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}