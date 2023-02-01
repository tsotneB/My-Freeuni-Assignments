package Models.Tests;

import Models.*;
import junit.framework.TestCase;
import static Models.Constants.*;

public class TestStatus extends TestCase {

    /**
     * tests user status
     */
    public void testUserStatus() {
        Status st = new UserStatus();
        st.setStatus(ADMIN);
        assertEquals(ADMIN, st.getStatus());
        st.setStatus(CUSTOMER);
        assertEquals(CUSTOMER, st.getStatus());
        st.setStatus(COURIER);
        assertEquals(COURIER, st.getStatus());
        st.setStatus(MANAGER);
        assertEquals(MANAGER, st.getStatus());
        boolean b = st.setStatus("foo");
        assertFalse(b);
        assertEquals(MANAGER, st.getStatus());
        Status stEq = new UserStatus();
        stEq.setStatus(MANAGER);
        assertTrue(st.equals(stEq));
        Status stNE = null;
        assertFalse(st.equals(stNE));
        String str = "Manager";
        assertFalse(st.equals(str));
    }

    /**
     * tests privacy status
     */
    public void testPrivacyStatus() {
        Status st = new PrivacyStatus();
        st.setStatus(PRIVATE);
        assertEquals(PRIVATE, st.getStatus());
        st.setStatus(PUBLIC);
        assertEquals(PUBLIC, st.getStatus());
        st.setStatus(FRIENDS);
        assertEquals(FRIENDS, st.getStatus());
        boolean b = st.setStatus("foo");
        assertFalse(b);
        assertEquals(FRIENDS, st.getStatus());
        Status stEq = new PrivacyStatus();
        stEq.setStatus(FRIENDS);
        assertTrue(st.equals(stEq));
        Status stNE = null;
        assertFalse(st.equals(stNE));
        String str = "Friends";
        assertFalse(st.equals(str));
    }

    /**
     * tests request status
     */
    public void testRequestStatus() {
        Status st = new RequestStatus();
        st.setStatus(APPROVED);
        assertEquals(APPROVED, st.getStatus());
        st.setStatus(REJECTED);
        assertEquals(REJECTED, st.getStatus());
        st.setStatus(PENDING);
        assertEquals(PENDING, st.getStatus());
        st.setStatus(NOTSENT);
        assertEquals(NOTSENT, st.getStatus());
        boolean b = st.setStatus("foo");
        assertFalse(b);
        assertEquals(NOTSENT, st.getStatus());
        Status stEq = new RequestStatus();
        stEq.setStatus(NOTSENT);
        assertTrue(st.equals(stEq));
        Status stNE = null;
        assertFalse(st.equals(stNE));
        String str = "NotSent";
        assertFalse(st.equals(str));
    }

    /**
     * tests courier status
     */
    public void testCourierStatus() {
        Status st = new CourierStatus();
        st.setStatus(FREE);
        assertEquals(FREE, st.getStatus());
        st.setStatus(OCCUPIED);
        assertEquals(OCCUPIED, st.getStatus());
        boolean b = st.setStatus("foo");
        assertFalse(b);
        assertEquals(OCCUPIED, st.getStatus());
        Status stEq = new CourierStatus();
        stEq.setStatus(OCCUPIED);
        assertTrue(st.equals(stEq));
        Status stNE = null;
        assertFalse(st.equals(stNE));
        String str = "OCCUPIED";
        assertFalse(st.equals(str));
    }

    /**
     * tests order status
     */
    public void testOrderStatus() {
        Status st = new OrderStatus();
        st.setStatus(DELIVERED);
        assertEquals(DELIVERED, st.getStatus());
        st.setStatus(ONWAY);
        assertEquals(ONWAY, st.getStatus());
        st.setStatus(NOTRECEIVE);
        assertEquals(NOTRECEIVE, st.getStatus());
        boolean b = st.setStatus("foo");
        assertFalse(b);
        assertEquals(NOTRECEIVE, st.getStatus());
        Status stEq = new OrderStatus();
        stEq.setStatus(NOTRECEIVE);
        assertTrue(st.equals(stEq));
        Status stNE = null;
        assertFalse(st.equals(stNE));
        String str = "NotReceive";
        assertFalse(st.equals(str));
    }
}
