package Models;

import java.util.Objects;

public class Restaurant{
    private int rest_id;
    private int manager_id;
    private String name;
    private String district;
    private String address;
    private float rating;
    private int raters;
    private Status status;

    public Restaurant(){}

    /**
     * sets id for the restaurant
     * @param id
     */
    public void setId(int id) {
        rest_id = id;
    }

    /**
     * sets address for the restaurant
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * sets district for the restaurant
     * @param district
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * sets id of the manager of the restaurant
     * @param id
     */
    public void setManager_id(int id){
        manager_id = id;
    }

    /**
     * sets rating for the restaurant (0, by default)
     * @param rating
     */
    public void setRating(float rating){
        this.rating = rating;
    }

    /**
     * sets name for the restaurant
     * @param name
     */

    public void setName(String name){
        this.name = name;
    }

    /**
     * sets number of users who rated the restaurant (0 by default)
     * @param raters
     */
    public void setRaters(int raters) {
        this.raters = raters;
    }

    /**
     * sets addition status for the restaurant (Pending or approved)
     * pending by default
     * @param status
     */

    public void setAdded(Status status) {
        this.status = status;
    }

    /**
     * @return restaurant_id;
     */
    public int getId(){
        return rest_id;
    }

    /**
     *
     * @return id of the manager of the restaurant
     */

    public int getManager_id(){
        return manager_id;
    }

    /**
     * @return district of the restaurant
     */

    public String getDistrict(){
        return district;
    }

    /**
     *
     * @return address of the restaurant
     */
    public String getAddress(){
        return address;
    }


    /**
     *
     * @return rating of the restaurant
     */
    public float getRating(){
        return rating;
    }

    /**
     *
     * @return name of the restaurant
     */

    public String getName(){
        return name;
    }

    /**
     *
     * @return number of users who rated this restaurant
     */

    public int getRaters() {
        return raters;
    }


    /**
     *
     * @return addition status of the restaurant (pending or approved)
     */
    public Status getAdded() {
        return status;
    }

    /**
     *
     * @param obj
     * @return true if object received as a parameter is equal to this restaurant
     */

    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Restaurant o = (Restaurant) obj;
        return rest_id == o.getId() && raters == o.getRaters() && status.equals(o.getAdded()) &&
                manager_id == o.getManager_id() && rating == o.getRating() &&
                address.equals(o.getAddress()) && district.equals(o.getDistrict()) && name.equals(o.getName());
    }

    /**
     *
     * @return restaurant converted to string
     */

    public String toString(){
        return "Id: "+ rest_id + "n/ name: "+name+ "/n manager id: " + manager_id +"/n district: "+
                district+ "/n address: " + address + "n/ rating: " + rating;
    }
}