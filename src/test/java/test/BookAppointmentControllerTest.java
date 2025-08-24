package test;

import ispw.foodcare.Role;
import ispw.foodcare.controller.applicationcontroller.BookAppointmentController;
import ispw.foodcare.model.Address;
import ispw.foodcare.model.Nutritionist;
import ispw.foodcare.model.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/* Testa il metodo BookAppointmentController.searchNutritionistsByCity(...) */
class BookAppointmentControllerSearchTest {

    private Session s;
    private BookAppointmentController controller;

    @BeforeEach
    void setUp() {
        s = Session.getInstance();
        s.setRam(true); s.setDB(false); s.setFs(false);

        var nDao = s.getNutritionistDAO();

        /*RAM: uno a Roma, uno a Milano*/
        nDao.saveNutritionistRam(
                new Nutritionist(
                        new Nutritionist.Credentials("nutriRoma", "pw"),
                        new Nutritionist.Anagraphic("Ada", "Lovelace", "a@x", "111"),
                        new Nutritionist.NutritionistProfile(
                                "IT000000001","LM","Dietetica",
                                new Address("Via A","1","00100","Roma","RM","Lazio")),
                        Role.NUTRITIONIST));

        nDao.saveNutritionistRam(
                new Nutritionist(
                        new Nutritionist.Credentials("nutriMi", "pw"),
                        new Nutritionist.Anagraphic("Alan", "Turing", "b@x", "222"),
                        new Nutritionist.NutritionistProfile(
                                "IT000000002","LM","Sport",
                                new Address("Via B","2","20100","Milano","MI","Lombardia")),
                        Role.NUTRITIONIST));

        controller = new BookAppointmentController();
    }

    @Test
    void search_byCity_exactMatch() {
        var list = controller.searchNutritionistsByCity("Roma");
        assertEquals(1, list.size());
        assertEquals("nutriRoma", list.get(0).getUsername());
    }

    @Test
    void search_byCity_caseInsensitive() {
        var list = controller.searchNutritionistsByCity("rOmA");
        assertEquals(1, list.size());
    }

    @Test
    void search_byCity_noResults() {
        assertTrue(controller.searchNutritionistsByCity("Napoli").isEmpty());
    }
}