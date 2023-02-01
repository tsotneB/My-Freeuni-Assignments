package Models.Tests;

import Models.Constants;
import Models.DAO.DishDAO;
import Models.Dish;
import Models.RequestStatus;
import Models.Restaurant;
import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestDishDAO extends TestCase {
    private Connection connection;
    private String[] names = {"Alpen Gold","Khinkali","Khachapuri","Cookie","Peach"};
    private int[] rest_ids = {1001,1002,1003,1004,1005};
    private float[] prices = {(float) 2.60,(float) 1.20, (float) 15.00, (float) 3.40, (float) 1.50};
    private String[] categories = {"Candy","Meat","Georgian","Candy","Fruit"};
    private int numRaters[] = {1,1,1,1,1};
    private float[] ratings ={(float) 3.40,(float) 4.50, (float) 5.00, (float) 5.00, (float) 4.60};
    private String[] status = {"Pending","Pending","Pending","Pending","Pending"};

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

    private List<Dish> fillDishList() {
        List<Dish> dishes = new ArrayList<>();
        for (int i=0;i<5;i++) {
            Dish newDish = new Dish();
            newDish.setName(names[i]);
            newDish.setRest_id(rest_ids[i]);
            newDish.setPrice(prices[i]);
            newDish.setCategory(categories[i]);
            newDish.setRaters(numRaters[i]);
            newDish.setRating(ratings[i]);
            RequestStatus requestStatus = new RequestStatus();
            requestStatus.setStatus(status[i]);
            newDish.setAdded(requestStatus);
            dishes.add(newDish);
        }
        return dishes;
    }

    public void testGetAll() {
        DishDAO dishDAO = new DishDAO(connection);
        List<Dish> dishList = dishDAO.getDishes();
        List<Dish> dishes = fillDishList();
        assert(dishes.containsAll(dishList));
        assert(dishList.containsAll(dishes));
    }

    public void testGetByID() {
        DishDAO dishDAO = new DishDAO(connection);
        Dish dish = dishDAO.getDishById(212);
        Dish newDish = new Dish();
        newDish.setDish_id(212);
        newDish.setName(names[2]);
        newDish.setRest_id(rest_ids[2]);
        newDish.setPrice(prices[2]);
        newDish.setCategory(categories[2]);
        newDish.setRaters(numRaters[2]);
        newDish.setRating(ratings[2]);
        RequestStatus requestStatus = new RequestStatus();
        requestStatus.setStatus(status[2]);
        newDish.setAdded(requestStatus);
        assertEquals(newDish,dish);
    }

    public void testGetByIDNoEntry() {
        DishDAO dishDAO = new DishDAO(connection);
        assertNull(dishDAO.getDishById(1));
        assertNull(dishDAO.getDishById(2));
        assertNull(dishDAO.getDishById(3));
        assertNull(dishDAO.getDishById(4));
        assertNull(dishDAO.getDishById(5));
    }

    public void testGetPending() {
        DishDAO dishDAO = new DishDAO(connection);
        List<Dish> dishList = dishDAO.getPendingDishes();
        List<Dish> dishes = fillDishList();
        assert(dishes.containsAll(dishList));
        assert(dishList.containsAll(dishes));
    }

    public void testGetRestaurantDishes() {
        DishDAO dishDAO = new DishDAO(connection);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1002);
        restaurant.setName("Machakhela");
        restaurant.setManager_id(502);
        restaurant.setDistrict("Didube");
        restaurant.setAddress("Some Adress2");
        restaurant.setRaters(2001);
        restaurant.setRating((float) 3.20);
        RequestStatus requestStatus = new RequestStatus();
        requestStatus.setStatus("Rejected");
        restaurant.setAdded(requestStatus);
        List<Dish> dishRest = dishDAO.getRestaurantDishes(restaurant);
        assertEquals(1,dishRest.size());
        Dish newDish = new Dish();
        newDish.setName(names[1]);
        newDish.setRest_id(rest_ids[1]);
        newDish.setPrice(prices[1]);
        newDish.setCategory(categories[1]);
        newDish.setRaters(numRaters[1]);
        newDish.setRating(ratings[1]);
        RequestStatus rs = new RequestStatus();
        requestStatus.setStatus(status[1]);
        newDish.setAdded(rs);
        assertEquals(newDish,dishRest.get(0));
    }

    public void testGetByNameAndRestID() throws SQLException {
        DishDAO dishDAO = new DishDAO(connection);
        assertNull(dishDAO.getDishByRestAndName(1005,"Test"));
        dishDAO.addDish("TestDish",1005,"Test",(float) 3.50);
        PreparedStatement ps = connection.prepareStatement("delete from dishes where name = ? and rest_id = ?");
        ps.setString(1, "TestDish");
        ps.setInt(2, 1005);
        assertEquals(1, ps.executeUpdate());
    }


    public void testAddApproveUpdate() {
        DishDAO dishDAO = new DishDAO(connection);
        assertNull(dishDAO.getDishByRestAndName(1005,"Test"));
        dishDAO.addDish("TestDish",1005,"Test",(float) 3.50);
        Dish newDish = new Dish();
        newDish.setName("TestDish");
        newDish.setRest_id(1005);
        newDish.setPrice((float) 3.5);
        newDish.setCategory("Test");
        newDish.setAdded(new RequestStatus());
        assertEquals(newDish,dishDAO.getDishByRestAndName(1005,"TestDish"));
        dishDAO.approveDish(dishDAO.getDishByRestAndName(1005,"TestDish").getDish_id());
        RequestStatus rs = new RequestStatus();
        rs.setStatus(Constants.APPROVED);
        newDish.setAdded(rs);
        assertEquals(newDish,dishDAO.getDishByRestAndName(1005,"TestDish"));
        dishDAO.updateDish(dishDAO.getDishByRestAndName(1005,"TestDish"),4);
        newDish.setRaters(1);
        newDish.setRating((float)4);
        assertEquals(newDish,dishDAO.getDishByRestAndName(1005,"TestDish"));
        dishDAO.removeDish(dishDAO.getDishByRestAndName(1005,"TestDish").getDish_id());
        assertNull(dishDAO.getDishByRestAndName(1005,"TestDish"));
    }
}
