package Models;

import java.util.Objects;

public class Courier{
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String phoneNumber;
    private String district;
    private float rating;
    private int raters;
    private int completedOrders;
    private Status isAdded;
    private Status isFree;
    private int currOrder;

    public Courier(){}


    /**
     * sets id for Courier
     * @param id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * sets email for Courier
     * @param email
     */

    public void setEmail(String email){
        this.email = email;
    }

    /**
     * sets first name for Courier
     * @param firstName
     */
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    /**
     * sets last name for Courier
     * @param lastName
     */

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    /**
     * sets password for Courier
     * @param password
     */

    public void setPassword(String password){
        this.password = password;
    }

    /**
     * sets working district for Courier
     * @param district
     */
    public void setDistrict(String district){
        this.district = district;
    }

    /**
     * sets phone number for Courier
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    /**
     * sets rating for Courier (0 by default)
     * @param rating
     */
    public void setRating(float rating){
        this.rating = rating;
    }

    /**
     * sets number of users who rated this courier (0 by default)
     * @param raters
     */
    public void setRaters(int raters){
        this.raters = raters;
    }

    /**
     * sets number of completed orders by this courier (0 by default)
     * @param completedOrders
     */

    public void setCompletedOrders(int completedOrders){
        this.completedOrders = completedOrders;
    }

    /**
     * sets registration status for courier (pending or approved)
     * pending by default
     * @param isAdded
     */

    public void setAdded(Status isAdded){
        this.isAdded = isAdded;
    }

    /**
     * sets current working status for courier (free or occupied)
     * free by default
     * @param isFree
     */
    public void setFree(Status isFree){
        this.isFree = isFree;
    }

    /**
     * sets current order to specified value.
     * @param currOrder int type.
     */
    public void setCurrOrder(int currOrder) {
        this.currOrder = currOrder;
    }

    /**
     *
     * @return courier_id
     */

    public int getId(){
        return id;
    }

    /**
     *
     * @return email address of courier
     */
    public String getEmail(){
        return email;
    }

    /**
     *
     * @return first name of courier
     */

    public String getFirstName(){
        return firstName;
    }

    /**
     *
     * @return working district of courier
     */
    public String getDistrict(){
        return district;
    }

    /**
     *
     * @return last name of courier
     */

    public String getLastName(){
        return lastName;
    }

    /**
     *
     * @return password of courier
     */

    public String getPassword(){
        return password;
    }

    /**
     *
     * @return phone number of courier
     */
    public String getPhoneNumber(){
        return phoneNumber;
    }

    /**
     *
     * @return rating of courier
     */
    public float getRating(){
        return rating;
    }

    /**
     *
     * @return number of users who rated this courier
     */
    public int getRaters(){
        return raters;
    }

    /**
     *
     * @return number of completed orders by this courier
     */
    public int getCompletedOrders(){
        return completedOrders;
    }

    /**
     *
     * @return registration status of courier (pending or approved)
     */

    public Status getAdded(){
        return isAdded;
    }

    /**
     *
     * @return working status of courier (Free or occupied)
     */

    public Status getFree(){
        return isFree;
    }

    /**
     * returns current order.
     * @return int type, representing current order.
     */
    public int getCurrOrder() {
        return currOrder;
    }


    /**
     *
     * @param obj
     * @return true if object received as a parameter is equal to this particular courier
     */
    @Override
    public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Courier o = (Courier) obj;
            return id == o.getId() && email.equals(o.getEmail()) && firstName.equals(o.getFirstName())
                    && lastName.equals(o.getLastName()) && password.equals(o.getPassword()) &&
                     district.equals(o.getDistrict()) && phoneNumber.equals(o.getPhoneNumber()) &&
                     rating == o.getRating() && raters == getRaters() && completedOrders == o.getCompletedOrders()
                     && isAdded.equals(o.isAdded) && isFree.equals(o.isFree);
    }

    /**
     *
     * @return courier object converted to string
     */
    @Override
    public String toString(){
        return id + " " + email + " " + firstName + " " + lastName + " "+ password + " " + " "+ district
        + " "+ phoneNumber + " " + rating + " " + raters + " " +completedOrders + " " + currOrder + " "+ isAdded.getStatus() + " " + isFree.getStatus();
    }
}

