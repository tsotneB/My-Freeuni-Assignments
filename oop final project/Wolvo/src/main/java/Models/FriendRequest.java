package Models;

/**
 * this class is for User's friendRequests
 */
public class FriendRequest {
    private User From;
    private User To;
    private Status Status;

    /**
     * gets nothing and returns User from which is sent request.
     * @return User type.
     */
    public User getFrom() {
        return From;
    }

    /**
     * gets nothing and returns User to which is sent request.
     * @return User type.
     */
    public User getTo() {
        return To;
    }

    /**
     * gets nothing and returns status of request.
     * @return RequestStatus type which represents request status.
     */
    public Status getStatus() {
        return Status;
    }

    /**
     * gets User from which is sent request and sets it to private variable.
     * returns nothing
     * @param From User type.
     */
    public void setFrom(User From) {
        this.From = From;
    }

    /**
     * gets User to which is sent request and sets it to private variable.
     * @param To User type.
     */
    public void setTo(User To) {
        this.To = To;
    }

    /**
     * gets string which represents request status and sets it to private variable.
     * returns nothing.
     * @param Status String type.
     */
    public void setStatus(Status Status) {
        this.Status = Status;
    }

    /**
     * turns information of this class to string.
     * format is From + To + Status
     * @return
     */
    public String toString() {
        String s = "Request is sent from " + From.getId() + "(first name \"" + From.getFirstName() +
                "\" last name \"" + From.getLastName() + "\") to " + To.getId() +
                "(first name \"" + To.getFirstName() +
                "\" last name \"" + To.getLastName() +"\")" +
                "\nrequest status is " + Status.getStatus();
        return s;
    }
}