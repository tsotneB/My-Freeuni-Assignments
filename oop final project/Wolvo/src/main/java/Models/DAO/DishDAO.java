package Models.DAO;

import Models.*;

import java.sql.*;
import java.util.*;

public class DishDAO{
    private Connection connection;

    public DishDAO(Connection connection){
        this.connection = connection;
    }

    /**
     *
     * @return the list of all the dishes currently registered in every restaurant
     */
    public List<Dish> getDishes(){
        List<Dish> dishes = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from dishes;");
            while (resultSet.next()) {
                dishes.add(convertToDish(resultSet));
            }
        } catch (SQLException throwables) {}
        return dishes;
    }

    /**
     *
     * @param id dish_id
     * @return Dish object with that particular dish_id
     */

    public Dish getDishById(int id){
        try {
            PreparedStatement statement = connection.prepareStatement("select * from dishes where dish_id = ?");
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return convertToDish(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    public Dish getDishByRestAndName(int restaurant_id, String name) {
        try {
            PreparedStatement statement = connection.prepareStatement("select * from dishes where rest_id = ? and name = ?");
            statement.setInt(1,restaurant_id);
            statement.setString(2,name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return convertToDish(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    /**
     *
     * @param restaurant
     * @return the list of all the dishes in the particular restaurant (received as a parameter)
     */

    public List<Dish> getRestaurantDishes(Restaurant restaurant){
        List<Dish> dishes = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from dishes where rest_id = ?");
            statement.setInt(1, restaurant.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                dishes.add(convertToDish(resultSet));
            }
        } catch (SQLException throwables) {}
        return dishes;
    }

    /**
     *
     * @param name
     * @param restaurant
     * @param category
     * @param price
     * Inserts new dish in the database with data taken as a parameters
     */
    public void addDish(String name, int restaurant, String category, float price){
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "insert into dishes (name, rest_id, category,price,rating,raters,add_status) values (?,?,?,?,?,?,?);");
            statement.setString(1, name);
            statement.setInt(2, restaurant);
            statement.setString(3,category);
            statement.setFloat(4, price);
            statement.setFloat(5, 0F);
            statement.setFloat(6, 0);
            statement.setString(7, "Pending");
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     *
     * @return the list of all the dishes pending admin approval (every change made by managers must be approved by admin)
     */

    public List<Dish> getPendingDishes(){
        List<Dish> dishes = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from dishes where add_status = ?");
            statement.setString(1, "Pending");
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                dishes.add(convertToDish(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dishes;
    }

    /**
     *
     * @param d
     * @param rate
     * updates the rating of the particular dish with the new value (called when user rates dish after the delivery)
     */

    public void updateDish(Dish d, int rate){
        if(rate < 0)return;
        DishDAO DDAO = new DishDAO(connection);
    //    DDAO.updateDish(d, rate);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE dishes set rating = ?, raters = ? where dish_id = ?;");
            statement.setFloat(1, (float) (d.getRating() * d.getRaters() + 1.0*rate) / (d.getRaters() + 1));
            statement.setInt(2, d.getRaters() + 1);
            statement.setInt(3,d.getDish_id());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     *
     * @param id dish_id
     * approves addition of the particular dish (will be called by Admin)
     */

    public void approveDish(int id){
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE dishes set add_status = ? where dish_id = ?;");
            statement.setString(1, Constants.APPROVED);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     *
     * @param id
     * removes dish with particular id from the database
     */

    public void removeDish(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "Delete from dishes where dish_id = ?;");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     *
     * @param rs
     * @return Dish object created with the data taken from the database
     * i.e. it takes a resultset of query and converts it to Dish object
     * @throws SQLException
     */
    private Dish convertToDish(ResultSet rs) throws SQLException {
        Dish d = new Dish();
        d.setDish_id(rs.getInt("dish_id"));
        d.setRest_id(rs.getInt("rest_id"));
        d.setName(rs.getString("name"));
        d.setRating(rs.getFloat("rating"));
        d.setCategory(rs.getString("category"));
        d.setPrice(rs.getFloat("price"));
        Status status = new RequestStatus();
        status.setStatus(rs.getString("add_status"));
        d.setAdded(status);
        d.setRaters(rs.getInt("raters"));
        return d;
    }
}
