package Models.Tests;

import Models.FriendRequest;
import Models.RequestStatus;
import Models.Status;
import Models.User;
import junit.framework.TestCase;
import static Models.Constants.*;

public class TestFriendRequest extends TestCase {

    public void testRequest1() {
        User usrFr = new User();
        usrFr.setId(1);
        User usrTo = new User();
        usrTo.setId(2);
        FriendRequest fr = new FriendRequest();
        fr.setFrom(usrFr);
        fr.setTo(usrTo);
        Status rs = new RequestStatus();
        rs.setStatus(PENDING);
        fr.setStatus(rs);
        assertEquals(fr.getFrom().getId(), 1);
        assertEquals(fr.getTo().getId(), 2);
        assertEquals(fr.getStatus().getStatus(), PENDING);
    }

    public void testRequest2() {
        String s = "Request is sent from 1(first name \"Tsotne\" last name \"Babunashvili\") to " +
                "2(first name \"Akaki\" last name \"Chukhua\")" +
                "\nrequest status is Approved";
        User usr1 = new User();
        usr1.setId(1);
        usr1.setFirstName("Tsotne");
        usr1.setLastName("Babunashvili");
        User usr2 = new User();
        usr2.setId(2);
        usr2.setLastName("Chukhua");
        usr2.setFirstName("Akaki");
        FriendRequest fr = new FriendRequest();
        fr.setFrom(usr1);
        fr.setTo(usr2);
        Status rs = new RequestStatus();
        rs.setStatus(APPROVED);
        fr.setStatus(rs);
        assertEquals(s, fr.toString());
    }
}
