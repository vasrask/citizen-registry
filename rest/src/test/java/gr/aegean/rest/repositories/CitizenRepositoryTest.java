package gr.aegean.rest.repositories;

import gr.aegean.domain.Citizen;
import org.junit.jupiter.api.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CitizenRepositoryTest {

    private static Citizen citizen;

    @BeforeAll
    static void setup() {
        citizen = new Citizen();
        citizen.setFirstName("Miles");
        citizen.setMiddleName("Dewey");
        citizen.setLastName("Davis");
        citizen.generateRegistryNumber();
        citizen.setDateOfBirth("26-06-1926");
        citizen.setPlaceOfBirth("Alton");
    }

    @Test
    @Order(1)
    void testSave() {
        Citizen saved = CitizenRepository.save(citizen);
        assertNotNull(saved);
        assertNotNull(saved.getRegistryNumber());
    }

    @Test
    @Order(2)
    void testFindByRegistryNumber() {
        Citizen found = CitizenRepository.findByRegistryNumber(citizen.getRegistryNumber());
        assertNotNull(found);
        assertEquals(citizen.getFullName(), found.getFullName());
    }

    @Test
    @Order(3)
    void testFindAll() {
        Map<String, Citizen> all = CitizenRepository.findAll();
        assertTrue(all.containsKey(citizen.getRegistryNumber()));
    }

    @Test
    @Order(4)
    void testDelete() {
        boolean deleted = CitizenRepository.delete(citizen.getRegistryNumber());
        assertTrue(deleted);
        assertNull(CitizenRepository.findByRegistryNumber(citizen.getRegistryNumber()));
    }
}
