package Models;

import static Models.Constants.*;

/**
 * this class is to represent status of request.
 */
public class RequestStatus implements Status {
    String status;

    /**
     * constructor, default value of request status is notResponded.
     */
    public RequestStatus() {
        status = PENDING;
    }

    /**
     * sets given status active.
     * @param status is string, it is set to private variable.
     * @return if status been successfully set.
     */
    @Override
    public boolean setStatus(String status) {
        if (status.equals(APPROVED) || status.equals(REJECTED)
                || status.equals(PENDING) || status.equals(NOTSENT)) {
           this.status = status;
           return true;
        }
        return false;
    }

    /**
     * gets nothing, returns request status.
     * @return String represents request status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * compares object to RequestStatus object.
     * @param obj Object type which is compared to this class type.
     * @return boolean representing if both are equal RequestStatus.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof RequestStatus)) return false;
        return ((RequestStatus) obj).getStatus().equals(status);
    }
}
