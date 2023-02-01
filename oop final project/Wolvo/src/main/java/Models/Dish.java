package Models;

import java.util.Objects;

public class Dish {
    private int dish_id;
    private String name;
    private int rest_id;
    private String category;
    private Status added;
    private int raters;
    private float price;
    private float rating;

    public Dish(){}

    /**
     *
     * @return price of the dish
     */
    public float getPrice() {
        return price;
    }

    /**
     *
     * @return id of the dish
     */
    public int getDish_id() {
        return dish_id;
    }

    /**
     *
     * @return category of the dish
     */
    public String getCategory() {
        return category;
    }

    /**
     *
     * @return rating of the dish
     */
    public float getRating() {
        return rating;
    }

    /**
     *
     * @return id of the restaurant which serves this particular dish
     */
    public int getRest_id() {
        return rest_id;
    }

    /**
     *
     * @return name of the dish
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return addition status of the dish (pending or approved)
     */

    public Status getAdded() {
        return added;
    }

    /**
     *
     * @return number of users who rated this dish
     */

    public int getRaters() {
        return raters;
    }

    /**
     * sets id for the dish
     * @param dish_id
     */
    public void setDish_id(int dish_id) {
        this.dish_id = dish_id;
    }

    /**
     * sets name for the dish
     * @param name
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets price for the dish
     * @param price
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * sets category for the dish
     * @param category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * sets rating for the dish
     * @param rating
     */
    public void setRating(float rating) {
        this.rating = rating;
    }

    /**
     * sets restaurant_id for the dish
     * @param rest_id
     */
    public void setRest_id(int rest_id) {
        this.rest_id = rest_id;
    }

    /**
     * sets addition status for the dish (Pending or approved)
     * @param s
     */

    public void setAdded(Status s) {
        this.added = s;
    }

    /**
     * sets number of users who rated the dish
     * @param raters
     */
    public void setRaters(int raters) {
        this.raters = raters;
    }

    /**
     *
     * @param obj
     * @return true if object order as a parameter is equal to this particular dish
     */
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Dish d = (Dish) obj;
        return rest_id == d.getRest_id() && name.equals(d.getName()) &&
                price == d.getPrice() && rating == d.getRating() && category.equals(d.getCategory());
    }

    /**
     *
     * @return dish converted to string
     */
    public String toString(){
        String s =  "Id: " + dish_id + " name: " + name + " restaurant id: " + rest_id +
                " price: " + price + " raters: " + raters + " rating: " + rating + " Category: " + category + " Status: "
                    + added.getStatus();
        return s;
    }
}