package Models;

import java.util.Objects;

public class User {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Status userStatus;
    private Status privacyStatus;
    private String district;
    private String address;
    private String phoneNumber;

    public User(){
    }

    /**
     * sets address of the user
     * @param address
     */

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * sets district of the user
     * @param district
     */

    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * sets email address of the user
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * sets id of the user
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * sets first name of the user
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * sets last name of the user
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * sets password of the user
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * sets phone number of the user
     * @param phoneNumber
     */

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * sets privacy status for the user
     * can only be "Everyone","Friends" or "No one"
     * @param privacyStatus
     */
    public void setPrivacyStatus(Status privacyStatus) {
        this.privacyStatus = privacyStatus;
    }

    /**
     * sets userStatus for the user
     * Can only be "Admin" or "Customer"
     * @param userStatus
     */
    public void setUserStatus(Status userStatus) {
        this.userStatus = userStatus;
    }

    /**
     *
     * @return id of the user
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return privacy Status of the user
     */

    public Status getPrivacyStatus() {
        return privacyStatus;
    }

    /**
     *
     * @return type of the user (admin or customer)
     */
    public Status getUserStatus() {
        return userStatus;
    }

    /**
     *
     * @return address of the user
     */

    public String getAddress() {
        return address;
    }

    /**
     *
     * @return district of the user
     */
    public String getDistrict() {
        return district;
    }

    /**
     *
     * @return email address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @return last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @return password of the user
     */

    public String getPassword() {
        return password;
    }

    /**
     *
     * @return phone number of the user
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     *
     * @param obj
     * @return true if the object received as a parameter is equal to the user
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User that = (User) obj;
        return Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName) && Objects.equals(email,that.email)
                    && Objects.equals(password,that.password) && Objects.equals(district,that.district)
                        && Objects.equals(address,that.address) && Objects.equals(phoneNumber,that.phoneNumber)
                            && Objects.equals(userStatus.getStatus(),that.userStatus.getStatus())
                                    && Objects.equals(privacyStatus.getStatus(),that.privacyStatus.getStatus());
    }

    /**
     *
     * @return user converted to string
     */
    @Override
    public String toString() {
        return firstName + " " + lastName + ", " + id + ", " + email + ", " + phoneNumber + ", "
                 + district + " " + address + " " + userStatus.getStatus() + " " + privacyStatus.getStatus();
    }
}
