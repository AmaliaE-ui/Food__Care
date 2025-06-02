package ispw.foodcare.controller.applicationcontroller;

import ispw.foodcare.bean.UserBean;
import ispw.foodcare.controller.guicontroller.UserDAOInterface;
import ispw.foodcare.model.Session;
import ispw.foodcare.utils.factory.UserDAOFactory;


public class LoginController {

    private static final LoginController instance = new LoginController();

    private LoginController() {}

    public static LoginController getInstance() {
        return instance;
    }


    //Dovrebbe essere User di tipo entity(model) non la UserBean --> le bean passano i dati solo tra GuiController e ControllerAppicativo
    public static UserBean authenticateUser(String username, String password) {

        //Se la DAO non è ancora impostata la setta in base alla modalità scelta
        if(Session.getInstance().getUserDAO() == null) {
            boolean isRam = Session.getInstance().isRam();
            boolean isDB = Session.getInstance().isDB();

            //Uso Factory per ottenere l'implementazione giusta
            UserDAOInterface dao = UserDAOFactory.getInstance().getUserDAO(isDB,isRam);
            Session.getInstance().setUserDAO(dao);
        }

        //Esegui Login usando la DAO memeorizzata in Sessione
        return Session.getInstance().getUserDAO().checkLogin(username, password);
    }
}
