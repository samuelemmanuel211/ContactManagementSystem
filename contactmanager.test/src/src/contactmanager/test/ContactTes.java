package contactmanager.test;

import contactmanager.model.Contact;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

class ContactTest {
    @Test
    void testContactCreation() {
        Contact contact = new Contact("John Doe", "1234567890", "john@example.com");
        assertEquals("John Doe", contact.getName());
        assertEquals("1234567890", contact.getPhone());
        assertEquals("john@example.com", contact.getEmail());
    }
}
