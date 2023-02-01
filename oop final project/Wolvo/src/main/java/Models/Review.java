package Models;

import java.util.Objects;

public class Review{
    private int review_id;
    private int user;
    private int dish;
    private int courier;
    private int dishRating;
    private int courierRating;
    private int order_id;
    private String courierText;
    private String dishText;

    public Review(){}


    /**
     * sets order id for the review
     * @param order_id
     */

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
    /**
     * sets review_id
     * @param review_id
     */

    public void setReview_id(int review_id) {this.review_id = review_id; }
    /**
     * sets id of the user who made this review
     * @param user
     */
    public void setUser(int user){
        this.user = user;
    }

    /**
     * sets id of the dish in the order user rated
     * @param dish
     */
    public void setDish(int dish){
        this.dish = dish;
    }

    /**
     * sets id of the courier in the order user rated
     * @param courier
     */
    public void setCourier(int courier){
        this.courier = courier;
    }

    /**
     * sets rating of the dish in the order made by user
     * USER MAY NOT HAVE RATED THE DISH
     * @param dishRating
     */
    public void setDishRating(int dishRating){
        this.dishRating = dishRating;
    }

    /**
     * sets rating of the courier who made the delivery of the order made by user
     * USER MAY NOT HAVE RATED THE COURIER
     * @param courierRating
     */
    public void setCourierRating(int courierRating){
        this.courierRating = courierRating;
    }

    /**
     * sets the comment user named about the order
     * MAY BE NULL
     * @param text
     */
    public void setCourierText(String text){
        this.courierText = text;
    }

    public void setDishText(String text) {
        this.dishText = text;
    }
    /**
     *
     * @return id of review
     */
    public int getReview_id() {return review_id; }

    /**
     *
     * @return rating of the dish in the order user reviewed
     * MAY BE NULL
     */

    public int getDishRating() {
        return dishRating;
    }

    /**
     *
     * @returnrating of the courier in the order user reviewed
     * MAY BE NULL
     */
    public int getCourierRating() {
        return courierRating;
    }


    /**
     *
     * @return order id of the review;
     */
    public int getOrder_id() {
        return order_id;
    }
    /**
     *
     * @return the comment user made about the order
     * MAY BE NULL
     */
    public String getCourierText() {
        return courierText;
    }


    /**
     *
     * @return the comment user made about the dish
     * MAY BE NULL
     */
    public String getDishText() {
        return dishText;
    }

    /**
     *
     * @return ID Of the courier who delivered the order user reviewed
     * MAY BE NULL
     */
    public int getCourier() {
        return courier;
    }

    /**
     * @return ID of the dish in the order user reviewed
     */
    public int getDish() {
        return dish;
    }

    /**
     *
     * @return id of the user who reviewed the order
     */
    public int getUser() {
        return user;
    }

    /**
     *
     * @param obj
     * @return true if the object received as a parameter is equal to this review
     */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Review o = (Review)obj;
        return review_id == o.getReview_id() && courier == o.getCourier() && dish == o.getDish() && user == o.getUser() &&
                order_id == o.getOrder_id() && dishRating == o.getDishRating() && courierRating == o.getCourierRating()
                && courierText.equals(o.getCourierText()) && dishText.equals(o.getDishText());
    }

    /**
     *
     * @return review converted to string
     */
    public String toString(){
        return review_id + " " + order_id + " " + user + " " + dish + " " + courier + " " +
                dishRating + " "+ courierRating + " " + courierText + " " + dishText;
    }
}

