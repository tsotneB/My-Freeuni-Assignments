package Models.Tests;

import Models.Courier;
import Models.DAO.CourierDAO;
import Models.DAO.DishDAO;
import Models.DAO.ReviewDAO;
import Models.DAO.UserDAO;
import Models.Dish;
import Models.Review;
import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestReviewDAO extends TestCase {
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

    public void testDishReviews0() {
        ReviewDAO reviewDAO = new ReviewDAO(connection);
        Dish dish = new Dish();
        dish.setDish_id(211);
        List<Review> revs = reviewDAO.getDishReviews(dish);
        Review firstRev = new Review();
        firstRev.setReview_id(301);
        firstRev.setOrder_id(104);
        firstRev.setUser(2);
        firstRev.setDish(211);
        firstRev.setCourier(2);
        firstRev.setDishRating(3);
        firstRev.setCourierRating(2);
        firstRev.setCourierText("Fuf");
        firstRev.setDishText("Fuf");
        assertEquals(1,revs.size());
        assertEquals(firstRev,revs.get(0));
    }

    public void testDishReviews1() {
        ReviewDAO reviewDAO = new ReviewDAO(connection);
        Dish dish = new Dish();
        dish.setDish_id(213);
        List<Review> revs = reviewDAO.getDishReviews(dish);
        Review firstRev = new Review();
        firstRev.setReview_id(303);
        firstRev.setUser(4);
        firstRev.setOrder_id(106);
        firstRev.setDish(213);
        firstRev.setCourier(4);
        firstRev.setCourierRating(-1);
        firstRev.setDishRating(-1);
        firstRev.setCourierText("");
        firstRev.setDishText("");
        assertEquals(1,revs.size());
        assertEquals(firstRev,revs.get(0));
    }

    public void testDishReviewNoEntry() {
        ReviewDAO reviewDAO = new ReviewDAO(connection);
        Dish dish = new Dish();
        dish.setDish_id(1);
        List<Review> revs = reviewDAO.getDishReviews(dish);
        assert(revs.isEmpty());
        dish.setDish_id(215);
        revs = reviewDAO.getDishReviews(dish);
        assert(revs.isEmpty());
    }

    public void testCourierReviewNo0() {
        ReviewDAO reviewDAO = new ReviewDAO(connection);
        Courier courier = new Courier();
        courier.setId(3);
        List<Review> revs = reviewDAO.getCourierReviews(courier);
        assertEquals(1,revs.size());
        Review firstRev = new Review();
        firstRev.setReview_id(302);
        firstRev.setOrder_id(105);
        firstRev.setUser(3);
        firstRev.setDish(212);
        firstRev.setCourier(3);
        firstRev.setDishRating(1);
        firstRev.setCourierRating(1);
        firstRev.setCourierText("normie");
        firstRev.setDishText("normie");
        assertEquals(firstRev,revs.get(0));
    }

    public void testCourierReviewNo1() {
        ReviewDAO reviewDAO = new ReviewDAO(connection);
        Courier courier = new Courier();
        courier.setId(5);
        List<Review> revs = reviewDAO.getCourierReviews(courier);
        assertEquals(1,revs.size());
        Review firstRev = new Review();
        firstRev.setReview_id(304);
        firstRev.setOrder_id(107);
        firstRev.setUser(5);
        firstRev.setDish(214);
        firstRev.setCourier(5);
        firstRev.setDishRating(5);
        firstRev.setCourierRating(5);
        firstRev.setCourierText("MAGARIA");
        firstRev.setDishText("MAGARIA");
        assertEquals(firstRev,revs.get(0));
    }


    public void testCourierReviewNoEntry() {
        ReviewDAO reviewDAO = new ReviewDAO(connection);
        Courier courier = new Courier();
        courier.setId(62);
        List<Review> revs = reviewDAO.getCourierReviews(courier);
        assert(revs.isEmpty());
        courier.setId(6);
        revs = reviewDAO.getCourierReviews(courier);
        assert(revs.isEmpty());
    }


    public void testAddReview() throws SQLException {
        ReviewDAO reviewDAO = new ReviewDAO(connection);
        UserDAO userDAO = new UserDAO(connection);
        CourierDAO courierDAO = new CourierDAO(connection);
        DishDAO dishDAO  = new DishDAO(connection);
        courierDAO.addCourier("test@test.com","test","test","Didube","test","12345679");
        List<Review> revs = reviewDAO.getCourierReviews(courierDAO.getCourierByEmail("test@test.com"));
        assert(revs.isEmpty());
        reviewDAO.addReview(239, userDAO.getByEmail("tarus19@freeuni.edu.ge"), dishDAO.getDishById(212),courierDAO.getCourierByEmail("test@test.com"),
                3,2,"cool","cool");
        revs = reviewDAO.getCourierReviews(courierDAO.getCourierByEmail("test@test.com"));
        assertEquals(1,revs.size());
        Review review = new Review();
        review.setReview_id(0);
        review.setOrder_id(239);
        review.setUser(userDAO.getByEmail("tarus19@freeuni.edu.ge").getId());
        review.setCourier(courierDAO.getCourierByEmail("test@test.com").getId());
        review.setDish(212);
        review.setDishRating(3);
        review.setCourierRating(2);
        review.setCourierText("cool");
        review.setDishText("cool");
        Review review1 = revs.get(0);
        review1.setReview_id(0);
        assertEquals(review,review1);
        PreparedStatement ps = connection.prepareStatement("delete from couriers where email = \"test@test.com\";");
        ps.executeUpdate();
        PreparedStatement pss = connection.prepareStatement("delete from reviews where dish_id = 212 and user_id = ? and courier_id = ?;");
        pss.setInt(1, userDAO.getByEmail("tarus19@freeuni.edu.ge").getId());
        pss.setInt(2, review.getCourier());
        pss.executeUpdate();
    }

    /**
     * tests getByID.
     */
    public void testGetByID() {
        ReviewDAO RDAO = new ReviewDAO(connection);
        Review firstRev = new Review();
        firstRev.setReview_id(301);
        firstRev.setOrder_id(104);
        firstRev.setUser(2);
        firstRev.setDish(211);
        firstRev.setCourier(2);
        firstRev.setDishRating(3);
        firstRev.setCourierRating(2);
        firstRev.setCourierText("Fuf");
        firstRev.setDishText("Fuf");
        assertEquals(RDAO.getByID(104), firstRev);

        Review secRev = new Review();
        secRev.setReview_id(303);
        secRev.setUser(4);
        secRev.setOrder_id(106);
        secRev.setDish(213);
        secRev.setCourier(4);
        secRev.setCourierRating(-1);
        secRev.setDishRating(-1);
        secRev.setCourierText("");
        secRev.setDishText("");
        assertEquals(RDAO.getByID(106), secRev);

        Review thirdRev = new Review();
        thirdRev.setReview_id(302);
        thirdRev.setOrder_id(105);
        thirdRev.setUser(3);
        thirdRev.setDish(212);
        thirdRev.setCourier(3);
        thirdRev.setDishRating(1);
        thirdRev.setCourierRating(1);
        thirdRev.setCourierText("normie");
        thirdRev.setDishText("normie");
        assertEquals(RDAO.getByID(105), thirdRev);

        Review fourthRev = new Review();
        fourthRev.setReview_id(304);
        fourthRev.setOrder_id(107);
        fourthRev.setUser(5);
        fourthRev.setDish(214);
        fourthRev.setCourier(5);
        fourthRev.setDishRating(5);
        fourthRev.setCourierRating(5);
        fourthRev.setCourierText("MAGARIA");
        fourthRev.setDishText("MAGARIA");
        assertEquals(RDAO.getByID(107), fourthRev);
    }
}
