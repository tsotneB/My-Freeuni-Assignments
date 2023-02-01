package Models.DAO;

import Models.*;
import com.mysql.cj.x.protobuf.MysqlxCrud;

import java.sql.*;
import java.util.*;
import java.util.Date;
import static Models.Constants.*;

public class OrderDAO {
    private Connection connection;

    public OrderDAO(Connection connection){
        this.connection = connection;
    }

    /**
     *
     * @return list of all the orders ever made
     */
    public List<Order> getOrders(){
        List<Order> orders = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from orders;");
            while (resultSet.next()) {
                orders.add(convertToOrder(resultSet));
            }
        } catch (SQLException throwables) {}
        return orders;
    }

    /**
     *
     * @param id order_id
     * @return list of all the orders made by particular user (with that user_id)
     */
    public List<Order> getUserOrders(int id){
        List<Order> orders = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from orders where user_id = ? order by " +
                    "order_date desc;");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(convertToOrder(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return orders;
    }

    /**
     *
     * @param user
     * @param dish
     * @param district
     * @param address
     * @param courier
     * Inserts new order in the database with data received as a parameters
     * @return "accepted" if everything goes well
     */

    public boolean addOrder(int user, int dish, String district, String address, int courier, int quantity){
        CourierDAO cDao = new CourierDAO(connection);
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        boolean b = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "insert into orders (user_id, dish_id, order_date, district, courier_id, location, order_status, quantity) values (?,?,?,?,?,?,?,?);");
            statement.setInt(1, user);
            statement.setInt(2,dish);
            statement.setTimestamp(3,ts);
            statement.setString(4,district);
            statement.setInt(5,courier);
            statement.setString(6,address);
            statement.setString(7, ONWAY);
            statement.setInt(8, quantity);
            int i = statement.executeUpdate();
            if (i != 0) b = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return b;
    }

    /**
     *
     * @param rs
     * @return Order object created with the data taken from the database
     * i.e. it takes a resultset of query and converts it to Order object
     * @throws SQLException
     */
    private Order convertToOrder(ResultSet rs) throws SQLException{
        Order o = new Order();
        o.setId(rs.getInt("order_id"));
        o.setUser(rs.getInt("user_id"));
        o.setDish(rs.getInt("dish_id"));
        o.setOrderDate(rs.getTimestamp("order_date"));
        o.setReceiveDate(rs.getTimestamp("receive_date"));
        Status os = new OrderStatus();
        os.setStatus(rs.getString("order_status"));
        o.setOrderStatus(os);
        o.setDistrict(rs.getString("district"));
        o.setCourier(rs.getInt("courier_id"));
        o.setAddress(rs.getString("location"));
        o.setQuantity(rs.getInt("quantity"));
        return o;
    }

    /**
     * returns courrier current order.
     * @param courier_id int.
     * @return Order type.
     */
    public Order getCouriersCurrentOrder(int courier_id) {
        Order ord = null;
        try {
            PreparedStatement statement = connection.prepareStatement("select * from orders where courier_id = ? and order_status =" +
                    "'OnWay'");
            statement.setInt(1, courier_id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ord = convertToOrder(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ord;
    }

    /**
     * sets specified courier order as delivered.
     * @param courier_id int type courier_id specified.
     */
    public void markAsDelivered(int courier_id) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE orders set order_status = ?, receive_date = ? where courier_id=? and order_status = ?;");
            statement.setString(1,"Delivered");
            statement.setTimestamp(2, new Timestamp(new Date().getTime()));
            statement.setInt(3, courier_id);
            statement.setString(4,"OnWay");
            statement.executeUpdate();
            statement = connection.prepareStatement("select * from orders where courier_id = ? and order_status = ?;");
            statement.setInt(1,courier_id);
            statement.setString(2,"Delivered");
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) return;
            Order currOrder = convertToOrder(rs);
            CourierDAO courierDAO = new CourierDAO(connection);
            courierDAO.markAsFree(currOrder.getCourier());
        } catch (SQLException throwables) {
        }
    }

    public Order getByID(int order_id) {
        Order ord = null;
        try {
            PreparedStatement statement = connection.prepareStatement("select * from orders where order_id = ?;");
            statement.setInt(1, order_id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ord = convertToOrder(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ord;
    }
}
