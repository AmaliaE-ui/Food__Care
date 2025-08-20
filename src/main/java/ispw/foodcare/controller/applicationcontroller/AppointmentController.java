package ispw.foodcare.controller.applicationcontroller;

import ispw.foodcare.bean.AppointmentBean;
import ispw.foodcare.dao.AppointmentDAO;
import ispw.foodcare.model.Appointment;
import ispw.foodcare.model.Session;
import ispw.foodcare.utils.Converter;
import java.util.List;

public class AppointmentController {

    private final AppointmentDAO appointmentDAO;

    /*Costruttore*/
    public AppointmentController(AppointmentDAO appointmentDAO) {
        this.appointmentDAO = appointmentDAO;
    }

    public AppointmentController (){
        var s = Session.getInstance();
        this.appointmentDAO = s.getAppointmentDAO();
    }

    public boolean hasUnviewedAppointmentsForNutritionist(String nutritionistUsername) {
        return appointmentDAO.hasUnviewedAppointmentsForNutritionist(nutritionistUsername);
    }

    public void markAppointmentsAsViewedForNutritionist(String nutritionistUsername) {
        appointmentDAO.markAppointmentsAsViewedForNutritionist(nutritionistUsername);
    }

    public List<AppointmentBean> getAppointmentsForNutritionist(String nutritionistUsername) {
        List<Appointment> listo = appointmentDAO.getAppointmentForNutritionistWithUsername(nutritionistUsername) ;
        return listo.stream()
                .map(n -> Converter.appointmentToBean(n))
                .toList();
    }
}
