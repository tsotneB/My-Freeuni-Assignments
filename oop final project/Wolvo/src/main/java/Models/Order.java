package Models;

import java.util.Objects;
import java.sql.Timestamp;

public class Order {
    private int id;
    private int user;
    private int dish;
    private Timestamp orderDate;
    private Timestamp receiveDate;
    private String district;
    private String address;
    private int courier;
    private int quantity;
    private Status orderStatus;

    public Order(){}

    public int getUser() {
        return user;
    }

    public int getDish() {
        return dish;
    }

    public String getDistrict() {
        return district;
    }

    public int getId() {
        return id;
    }

    public int getCourier() {
        return courier;
    }

    public String getAddress() {
        return address;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public Timestamp getReceiveDate() {
        return receiveDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public Status getOrderStatus() {
        return orderStatus;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDish(int dish) {
        this.dish = dish;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setCourier(int courier) {
        this.courier = courier;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public void setReceiveDate(Timestamp receiveDate) {
        this.receiveDate = receiveDate;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setOrderStatus(Status orderStatus) {
        this.orderStatus = orderStatus;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Order o = (Order) obj;
        return id == o.getId() && user == o.getUser() && dish == o.getDish()
                && courier == o.getCourier() && address.equals(o.getAddress()) && district.equals(o.getDistrict()) && orderDate.equals(o.getOrderDate());
    }

    public String toString(){
        return id + " " + user + " " + dish + " " + courier + " "+ address + " " + " "+ district + " "+
                orderDate.toString() + " " + orderStatus.getStatus() + " " + quantity;
    }
}

