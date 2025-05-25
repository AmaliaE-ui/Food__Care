package ispw.foodcare.controller.applicationcontroller;

import ispw.foodcare.bean.AddressBean;
import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.bean.PatientBean;
import ispw.foodcare.dao.AddressDAO;
import ispw.foodcare.dao.PatientDAO;
import ispw.foodcare.dao.UserDAO;
import ispw.foodcare.model.NutritionistModel;
import ispw.foodcare.model.PatientModel;

import java.sql.SQLException;

public class RegistrationController {

    public void registerNutritionist(NutritionistBean bean, AddressBean addressBean) throws SQLException {
        int addressId = new AddressDAO().saveAddress(addressBean);
        new NutritionistModel().registerNutritionist(bean, addressId);
    }

    public void registerPatient(PatientBean bean) throws SQLException {
        new PatientModel().registerPatient(bean);
    }
}
