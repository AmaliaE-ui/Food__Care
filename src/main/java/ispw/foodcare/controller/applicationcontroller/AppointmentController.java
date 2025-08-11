package ispw.foodcare.controller.applicationcontroller;

import ispw.foodcare.bean.AppointmentBean;
import ispw.foodcare.dao.AppointmentDAO;
import java.util.List;

public class AppointmentController {

    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    public boolean hasUnviewedAppointmentsForNutritionist(String nutritionistUsername) {
        return appointmentDAO.hasUnviewedAppointmentsForNutritionist(nutritionistUsername);
    }

    public void markAppointmentsAsViewedForNutritionist(String nutritionistUsername) {
        appointmentDAO.markAppointmentsAsViewedForNutritionist(nutritionistUsername);
    }

    public List<AppointmentBean> getAppointmentsForNutritionist(String nutritionistUsername) {
        List<AppointmentBean> beans = appointmentDAO.getAppointmentBeansForNutritionistWithPatientName(nutritionistUsername);
        return beans;
    }
}
