package ispw.foodcare.controller.applicationcontroller;


import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.bean.PatientBean;
import ispw.foodcare.dao.UserDAO;
import ispw.foodcare.model.Nutritionist;
import ispw.foodcare.model.Patient;
import ispw.foodcare.exeption.AccountAlreadyExistsException;
import ispw.foodcare.model.Session;
import ispw.foodcare.utils.Converter;

import java.util.logging.Logger;

public class RegistrationController {

    private static final Logger logger = Logger.getLogger(RegistrationController.class.getName());
    private final UserDAO userDAO;

    /*Costruttore*/
    public RegistrationController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public RegistrationController() {
        var s = Session.getInstance();
        this.userDAO = s.getUserDAO();
    }

    //Registra nutrizionista
    public boolean registrationNutritionist(NutritionistBean bean) {
        try {
            Nutritionist nutritionist = Converter.beanToNutritionist(bean);
            userDAO.saveUser(nutritionist);
            return true;
        } catch (AccountAlreadyExistsException e) {
            logger.info("Utente già esistente (" + bean.getUsername() + ")");
            return false;
        }
    }

    //Registra paziente
    public boolean registrationPatient(PatientBean bean){
            try {
                Patient patient = Converter.beanToPatient(bean);
                userDAO.saveUser(patient);
                return true;
            } catch (AccountAlreadyExistsException e) {
                logger.info("Utente già esistente (" + bean.getUsername() + ")");
                return false;
            }
    }
}
