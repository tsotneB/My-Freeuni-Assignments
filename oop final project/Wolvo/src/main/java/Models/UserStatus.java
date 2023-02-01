package Models;

import static Models.Constants.*;

/**
 * this class is for user's status
 */
public class UserStatus implements Status {
    private String status;

    /**
     * constructor. default user status is Normal user.
     */
    public UserStatus() {
        status = CUSTOMER;
    }

    /**
     * gets nothing and returns string representing user's status.
     * @return String which represents user status.
     */
    @Override
    public String getStatus() {
        return status;
    }

    /**
     * gets user status and sets it to private variable.
     * @param status representing user status.
     * @return if status been successfully set.
     */
    @Override
    public boolean setStatus(String status) {
        if (status.equals(ADMIN) || status.equals(CUSTOMER) ||
                status.equals(COURIER) || status.equals(MANAGER)) {
          this.status = status;
          return true;
        }
        return false;
    }

    /**
     * compares object to UserStatus object.
     * @param obj Object type which is compared to this class type.
     * @return boolean representing if both are equal UserStatus.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof UserStatus)) return false;
        return ((UserStatus) obj).getStatus().equals(status);
    }
}
