package Models.Tests;

import Models.CourierStatus;
import Models.Dish;
import Models.Status;
import junit.framework.TestCase;
import static Models.Constants.*;

public class TestDish extends TestCase {

    private int[] dish_id = {1, 2, 3};
    private int[] rest_id = {1, 2, 3};
    private String[] category = {"Candy", "Meat", "Lunch"};
    private String[] name = {"Alpen gold", "Khinkali", "Tea"};
    private String[] status = {"bla", PENDING, APPROVED};
    private float[] price = {2.6F, 1.2F, 0.3F};
    private int[] raters = {0, 55, 12};
    private float[] rating = {0F, 5.0F, 3.5F};


    public void testConstructor() {
        Dish dish = new Dish();
        dish = new Dish();
    }

    public void testDish() {
        Dish[] dish = new Dish[3];
        for (int i = 0; i < 3; i++) {
            dish[i] = new Dish();
            dish[i].setDish_id(dish_id[i]);
            dish[i].setRest_id(rest_id[i]);
            dish[i].setCategory(category[i]);
            dish[i].setName(name[i]);
            Status st = new CourierStatus();
            st.setStatus(status[i]);
            dish[i].setAdded(st);
            dish[i].setRaters(raters[i]);
            dish[i].setRating(rating[i]);
            dish[i].setPrice((price[i]));
        }
        for (int i = 0; i < 3; i++) {
            assertEquals(dish_id[i], dish[i].getDish_id());
            assertEquals(rest_id[i], dish[i].getRest_id());
            assertEquals(category[i], dish[i].getCategory());
            assertEquals(name[i], dish[i].getName());
            Status st = new CourierStatus();
            st.setStatus(status[i]);
            assertEquals(st, dish[i].getAdded());
            assertEquals(raters[i], dish[i].getRaters());
            assertEquals(rating[i], dish[i].getRating());
            assertEquals(price[i], dish[i].getPrice());
        }
    }

    public void testEquals() {
        Dish[] dish = new Dish[3];
        for (int i = 0; i < 3; i++) {
            dish[i] = new Dish();
            dish[i].setDish_id(dish_id[i]);
            dish[i].setRest_id(rest_id[i]);
            dish[i].setCategory(category[i]);
            dish[i].setName(name[i]);
            Status st = new CourierStatus();
            st.setStatus(status[i]);
            dish[i].setAdded(st);
            dish[i].setRaters(raters[i]);
            dish[i].setRating(rating[i]);
            dish[i].setPrice((price[i]));
        }
        for (int i = 0; i < 3; i++) {
            assertTrue(dish[i].equals(dish[i]));
            assertFalse(dish[i].equals(dish[(i + 1) % 3]));
            assertFalse(dish[i].equals(null));
        }
        Dish[] EQ = new Dish[3];
        for (int i = 0; i < 3; i++) {
            EQ[i] = new Dish();
            EQ[i].setDish_id(dish_id[i]);
            EQ[i].setRest_id(rest_id[i]);
            EQ[i].setCategory(category[i]);
            EQ[i].setName(name[i]);
            Status st = new CourierStatus();
            st.setStatus(status[i]);
            EQ[i].setAdded(st);
            EQ[i].setRaters(raters[i]);
            EQ[i].setRating(rating[i]);
            EQ[i].setPrice((price[i]));
        }
        for (int i = 0; i < 3; i++) {
            assertTrue(dish[i].equals(EQ[i]));
            assertFalse(dish[i].equals(EQ[(i + 1) % 3]));
        }
    }

    public void testToString() {
        Dish[] dish = new Dish[3];
        for (int i = 0; i < 3; i++) {
            dish[i] = new Dish();
            dish[i].setDish_id(dish_id[i]);
            dish[i].setRest_id(rest_id[i]);
            dish[i].setCategory(category[i]);
            dish[i].setName(name[i]);
            Status st = new CourierStatus();
            st.setStatus(status[i]);
            dish[i].setAdded(st);
            dish[i].setRaters(raters[i]);
            dish[i].setRating(rating[i]);
            dish[i].setPrice((price[i]));
        }
        for (int i = 0; i < 3; i++) {
            String s =  "Id: " + dish[i].getDish_id() + " name: " + dish[i].getName() + " restaurant id: " + dish[i].getRest_id() +
                    " price: " + dish[i].getPrice() + " raters: " + dish[i].getRaters() + " rating: " + dish[i].getRating() +
                      " Category: " + dish[i].getCategory() + " Status: " + dish[i].getAdded().getStatus();
            assertEquals(dish[i].toString(), s);
        }
    }

}
