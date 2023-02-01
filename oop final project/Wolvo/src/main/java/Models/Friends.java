package Models;

import java.util.List;
import java.util.Set;

/**
 * this class is for user's friends
 */
public class Friends {
    private Set<User> friends;
    private User usr;

    /**
     * constructor. does nothing.
     */
    public Friends() {

    }

    /**
     * gets nothing, returns user.
     * @return User type.
     */
    public User getUser() {
        return usr;
    }

    /**
     * gets nothing, returns List of friends (usr's friends).
     * @return List<User> type.
     */
    public Set<User> getFriends() {
        return friends;
    }

    /**
     * gets list of friends and sets it to private variable.
     * returns nothing.
     * @param friends List of Users.
     */
    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    /**
     * gets user and sets it to private variable.
     * returns nothing.
     * @param usr User type.
     */
    public void setUser(User usr) {
        this.usr = usr;
    }

    /**
     * compares if Object is equal to Friends type object.
     * @param obj which is compared to Friends.
     * @return boolean false if are not equal, true if are equal.
     */
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Friends)) return false;
        return ((Friends) obj).getUser().equals(usr) && ((Friends) obj).getFriends().equals(friends);
    }

    /**
     * turns information of this class to string.
     * format is user + list of user's friends.
     * @return String which contains class information.
     */
    public String toString() {
        String s = "(first name \"" + usr.getFirstName() +
                "\" last name \"" + usr.getLastName() + "\") Friends are ";
        for (User fr : friends) {
            s = s + "(first name \"" + fr.getFirstName() +
                    "\" last name \"" + fr.getLastName() + "\"), ";
        }
        return s;
    }
}