package Models;

import java.sql.Statement;

public class Manager {
    private int manager_id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int rest_id;
    private String phoneNumber;
    private Status addStatus;

    /**
     *
     * @param id
     * sets id for the manager
     */
    public void set_id(int id) {
        manager_id = id;
    }

    /**
     * sets first name for the manager
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * sets last name for the manager
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * sets email for the manager
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * sets password for the manager
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * sets the id of the restaurant the manager owns
     * @param rest_id
     */
    public void setRest_id(int rest_id) {
        this.rest_id = rest_id;
    }

    /**
     * sets phone number for the manager
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * sets addition status of the manager (Pending or approved)
     * @param status
     */
    public void setAddStatus(Status status) {
        this.addStatus = status;
    }
    /**
     *
     * @return id of the manager
     */
    public int getId() {
        return manager_id;
    }

    /**
     *
     * @return first name of the manager
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @return last name of the manager
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @return password of the manager
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @return email address of the manager
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return phone number of the manager
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     *
     * @return id of the restaurant the manager owns
     */
    public int getRest_id() {
        return rest_id;
    }


    /**
     *
     * @return addition status of the manager (Pending or approved)
     */
    public Status getAddStatus() {
        return addStatus;
    }

    /**
     *
     * @param obj
     * @return true if object received as a parameter is equal to this manager
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Manager o = (Manager) obj;
        return email.equals(o.getEmail()) && firstName.equals(o.getFirstName())
                && lastName.equals(o.getLastName()) && password.equals(o.getPassword()) &&
                    phoneNumber.equals(o.getPhoneNumber()) && rest_id == o.rest_id && addStatus.equals(o.addStatus);

    }

    /**
     *
     * @return manager object converted to string
     */
    @Override
    public String toString() {
        return "Id: " + manager_id + " first name: " + firstName + " last name: " + lastName +
                " email: " + email + " password: " + password + " restaurant_id: " + rest_id +
                    " phone number: " + phoneNumber + " status: " + addStatus.getStatus();
    }
}
