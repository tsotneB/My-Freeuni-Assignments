package Models.DAO;

import Models.*;

import java.sql.*;
import java.util.*;

public class ReviewDAO{
    private Connection connection;

    public ReviewDAO(Connection connection){
        this.connection = connection;
    }

    /**
     *
     * @param dish
     * @return list of all the reviews about the particular dish (received as a parameter)
     */

    public List<Review> getDishReviews(Dish dish){
        List<Review> reviews = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from reviews where dish_id = ? " +
                    "and dish_review is not null and dish_id != -1;");
            statement.setInt(1, dish.getDish_id());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                reviews.add(convertToReview(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return reviews;
    }

    /**
     *
     * @param user
     * @param dish
     * @param courier
     * @param dishRating
     * @param courierRating
     * @param dishtext
     * inserts new review in the database (called when user rates either courier, dish or restaurant after the delivery)
     */

    public void addReview(int order_id, User user, Dish dish, Courier courier, int dishRating, int courierRating, String courtext,String dishtext){
        CourierDAO cDao = new CourierDAO(connection);
        cDao.updateCourier(courier, courierRating);
        DishDAO dDao = new DishDAO(connection);
        dDao.updateDish(dish, dishRating);
        try{
            PreparedStatement statement = connection.prepareStatement(
                    "insert into reviews (user_id, dish_id, courier_id, dish_rating, courier_rating, courier_review, dish_review, order_id)" +
                            " values (?,?,?,?,?,?,?,?);");
            statement.setInt(1, user.getId());
            statement.setInt(2, dish.getDish_id());
            statement.setInt(3, courier.getId());
            statement.setInt(4, dishRating);
            statement.setInt(5, courierRating);
            statement.setString(6, courtext);
            statement.setString(7,dishtext);
            statement.setInt(8,order_id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     *
     * @param rs
     * @return Review object created with the data taken from the database
     * i.e. it takes a resultset of query and converts it to Review object
     * @throws SQLException
     */
    private Review convertToReview(ResultSet rs) throws SQLException{
        Review r = new Review();
        r.setReview_id(rs.getInt("review_id"));
        r.setUser(rs.getInt("user_id"));
        r.setOrder_id(rs.getInt("order_id"));
        r.setDish(rs.getInt("dish_id"));
        r.setCourier(rs.getInt("courier_id"));
        r.setDishRating(rs.getInt("dish_rating"));
        r.setCourierRating(rs.getInt("courier_rating"));
        r.setCourierText(rs.getString("courier_review"));
        r.setDishText(rs.getString("dish_review"));
        return r;
    }

    /**
     *
     * @param courier
     * @return list of reviews made about the particular courier
     */

    public List<Review> getCourierReviews(Courier courier) {
        List<Review> reviews = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from reviews where courier_id = ? " +
                    "and courier_review is not null and courier_rating != -1;");
            statement.setInt(1, courier.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                reviews.add(convertToReview(resultSet));
            }
        } catch (SQLException throwables) {}
        return reviews;
    }

    /**
     * returns Review by order_id specified.
     * @param order_id int type, represents order_id.
     * @return Review type.
     */
    public Review getByID(int order_id) {
        Review ord = null;
        try {
            PreparedStatement statement = connection.prepareStatement("select * from reviews where order_id = ?;");
            statement.setInt(1, order_id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ord = convertToReview(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ord;
    }


}
