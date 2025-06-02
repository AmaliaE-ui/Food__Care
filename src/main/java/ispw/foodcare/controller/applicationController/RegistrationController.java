package ispw.foodcare.controller.applicationcontroller;


import ispw.foodcare.bean.UserBean;
import ispw.foodcare.controller.guicontroller.UserDAOInterface;
import ispw.foodcare.exeption.AccountAlreadyExistsException;
import ispw.foodcare.model.Session;
import ispw.foodcare.model.User;
import ispw.foodcare.utils.Converter;
import ispw.foodcare.utils.factory.UserDAOFactory;

public class RegistrationController {

    //Metodo
    public boolean register(UserBean userBean) {

        //Se necessario imposta il DAO da Session
        if(Session.getInstance().getUserDAO() == null) {
            boolean isRam = Session.getInstance().isRam();
            boolean isDB = Session.getInstance().isDB();

            UserDAOInterface dao = UserDAOFactory.getInstance().getUserDAO(isDB, isRam);
            Session.getInstance().setUserDAO(dao);
        }

        //Converti bean -> entity
        User user = Converter.beanToUser(userBean);

        try {
            //Salvataggio centalizzato
            Session.getInstance().getUserDAO().saveUser(user);
            return true;
        } catch (AccountAlreadyExistsException e) {
            System.err.println("⚠️ Registrazione fallita: utente già esistente.");
            return false;
        } catch (Exception e) {
            System.err.println("❌ Errore durante la registrazione: " + e.getMessage());
            return false;
        }
    }
}
