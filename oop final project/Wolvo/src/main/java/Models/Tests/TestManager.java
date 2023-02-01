package Models.Tests;

import Models.Manager;
import Models.RequestStatus;
import Models.Status;
import junit.framework.TestCase;

import static org.junit.Assert.assertNotEquals;

public class TestManager extends TestCase {
    public void testConstructor() {
        Manager man = new Manager();
        assertNotNull(man);
    }

    public void testSetters0() {
        Manager man = new Manager();
        man.set_id(1);
        man.setFirstName("Man");
        man.setLastName("ager");
        man.setEmail("Man@ager.com");
        assertEquals(1,man.getId());
        assertEquals("Man",man.getFirstName());
        assertEquals("ager",man.getLastName());
        assertEquals("Man@ager.com",man.getEmail());
    }

    public void testSetters1() {
        Manager man = new Manager();
        man.set_id(2);
        man.setRest_id(1);
        man.setPhoneNumber("1234567");
        man.setLastName("ager");
        man.setEmail("Man@ager.com");
        assertEquals(2,man.getId());
        assertEquals("Man@ager.com",man.getEmail());
        assertEquals(1,man.getRest_id());
        assertEquals("ager",man.getLastName());
    }

    public void testEqual() {
        Manager man1 = new Manager();
        man1.set_id(1);
        man1.setPassword("abc");
        man1.setFirstName("man");
        man1.setLastName("ager");
        man1.setPhoneNumber("12345678");
        man1.setRest_id(1);
        man1.setEmail("test@test");
        RequestStatus rs = new RequestStatus();
        rs.setStatus("Pending");
        man1.setAddStatus(rs);
        Manager man2 = new Manager();
        man2.set_id(1);
        assertNotEquals(man1, man2);
        man2.setFirstName("man");
        man2.setLastName("ager");
        assertNotEquals(man1, man2);
        man2.setPassword("abc");
        assertNotEquals(man1, man2);
        man2.setRest_id(1);
        man2.setEmail("test@test");
        RequestStatus rs1 = new RequestStatus();
        rs1.setStatus("Pending");
        man2.setAddStatus(rs1);
        man2.setPhoneNumber("12345678");
        assertEquals(man1,man2);
    }

    public void testToString() {
        Manager man = new Manager();
        man.set_id(1);
        man.setFirstName("man");
        man.setLastName("ager");
        man.setEmail("Test@test.com");
        man.setPassword("123");
        man.setRest_id(1);
        man.setPhoneNumber("12345678");
        man.setAddStatus(new RequestStatus());
        String s = "Id: 1 first name: man last name: ager email: Test@test.com password: 123 restaurant_id: 1 " +
                "phone number: 12345678 status: Pending";
        assertEquals(s,man.toString());
    }
}
