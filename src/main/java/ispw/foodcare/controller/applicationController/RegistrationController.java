package ispw.foodcare.controller.applicationcontroller;


import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.bean.PatientBean;
import ispw.foodcare.model.Nutritionist;
import ispw.foodcare.model.Patient;
import ispw.foodcare.exeption.AccountAlreadyExistsException;
import ispw.foodcare.utils.Converter;


public class RegistrationController {

    private final UserService userService = new UserService(); //Creo istanza


    //Metodo riceve Bean, lo valida, lo converte e lo registra
    public boolean registrationNutritionist(NutritionistBean bean) throws AccountAlreadyExistsException {
        //Converti il Bean in un Model
        Nutritionist nutritionist = Converter.beanToNutritionist(bean);
        //Registrazione
        return userService.registerUser(nutritionist);
    }

    public boolean registrationPatient(PatientBean bean) throws AccountAlreadyExistsException {
        Patient patient = Converter.beanToPatient(bean);
        return userService.registerUser(patient);
    }

}
