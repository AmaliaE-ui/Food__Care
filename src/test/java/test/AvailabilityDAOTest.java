package test;

import ispw.foodcare.dao.AvailabilityDAO;
import ispw.foodcare.model.Availability;
import ispw.foodcare.model.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/** Testa il metodo AvailabilityDAO.deleteAvailabilitybydata(...) */
class AvailabilityDAOTest {

    private Session s;
    private AvailabilityDAO dao;

    @BeforeEach
    void setUp() {
        s = Session.getInstance();
        s.setRam(true); s.setDB(false); s.setFs(false);
        dao = s.getAvailabilityDAO();

        // seed: passato, oggi, futuro (stesso nutrizionista)
        dao.addAvailability(av("nutriT", LocalDate.now().minusDays(1), LocalTime.of(9,0)));
        dao.addAvailability(av("nutriT", LocalDate.now(),             LocalTime.of(10,0)));
        dao.addAvailability(av("nutriT", LocalDate.now().plusDays(1), LocalTime.of(11,0)));
    }

    @Test
    void deleteAvailabilitybydata_keepsOnlyFuture() {
        dao.deleteAvailabilitybydata(LocalDate.now());

        var remaining = dao.getAvailabilitiesForNutritionist("nutriT");
        assertEquals(1, remaining.size());
        assertTrue(remaining.get(0).getDate().isAfter(LocalDate.now()));
    }

    private static Availability av(String u, LocalDate d, LocalTime t) {
        var a = new Availability();
        a.setNutritionistUsername(u);
        a.setDate(d);
        a.setStartTime(t);
        a.setEndTime(t.plusMinutes(45));
        return a;
    }
}
