package ispw.foodcare.utils;

import ispw.foodcare.Role;
import ispw.foodcare.bean.AddressBean;
import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.bean.PatientBean;
import ispw.foodcare.bean.UserBean;
import ispw.foodcare.model.Address;
import ispw.foodcare.model.Nutritionist;
import ispw.foodcare.model.Patient;
import ispw.foodcare.model.User;

/*Classe che converte un oggetto entity in un oggetto Bean*/

public class Converter {
    // Costruttore privato per evitare istanziazione
    private Converter() {}

    //Metodo entity -> bean
    public static UserBean userToBean(User user) {
        if (user == null) return null;

        //Base - UserBean
        UserBean bean;

        if(user instanceof Patient patient){
            PatientBean patientBean = new PatientBean();

            //Campi di Patient
            patientBean.setBirthDate(patient.getBirthDate());
            patientBean.setGender(patient.getGender());
            bean = patientBean;
        } else if (user instanceof Nutritionist nutritionist) {
            NutritionistBean nutritionistBean = new NutritionistBean();

            //Campi di Nutritionist
            nutritionistBean.setPiva(nutritionist.getPiva());
            nutritionistBean.setTitoloStudio(nutritionist.getTitoloStudio());
            //nutritionistBean.setIndirizzoStudio(nutritionist.getIndirizzoStudio());
            nutritionistBean.setSpecializzazione(nutritionist.getSpecializzazione());
            nutritionistBean.setAddress(addressToBean(nutritionist.getAddress()));
            bean = nutritionistBean;
        } else {
            bean = new UserBean();
        }

        //Campi comuni di User
        bean.setUsername(user.getUsername());
        bean.setPassword(user.getPassword());
        bean.setName(user.getName());
        bean.setSurname(user.getSurname());
        bean.setEmail(user.getEmail());
        bean.setPhoneNumber(user.getPhoneNumber());
        bean.setRole(user.getRole()); // RoleEnum -> String
        return bean;
    }

    //Metodo entity -> bean
    public static AddressBean addressToBean(Address address) {
        if (address == null) return null;

        AddressBean bean = new AddressBean();
        bean.setVia(address.getVia());
        bean.setCivico(address.getCivico());
        bean.setCap(address.getCap());
        bean.setCitta(address.getCitta());
        bean.setProvincia(address.getProvincia());
        bean.setRegione(address.getRegione());
        return bean;
    }

    //Metodo bean -> entity
    public static User beanToUser(UserBean bean) {
        if (bean == null) return null;

        User user;
        Role role = bean.getRole();

        if(bean instanceof PatientBean patientBean){
            user = new Patient(
                    patientBean.getUsername(),
                    patientBean.getPassword(),
                    patientBean.getName(),
                    patientBean.getSurname(),
                    patientBean.getEmail(),
                    patientBean.getPhoneNumber(),
                    role,
                    patientBean.getBirthDate(),
                    patientBean.getGender()
            );
        } else if(bean instanceof NutritionistBean nutritionistBean){
            user = new Nutritionist(
                    nutritionistBean.getUsername(),
                    nutritionistBean.getPassword(),
                    nutritionistBean.getName(),
                    nutritionistBean.getSurname(),
                    nutritionistBean.getEmail(),
                    nutritionistBean.getPhoneNumber(),
                    role,
                    nutritionistBean.getPiva(),
                    nutritionistBean.getTitoloStudio(),
                    nutritionistBean.getSpecializzazione(),
                    new Address(
                            nutritionistBean.getAddress().getVia(),
                            nutritionistBean.getAddress().getCivico(),
                            nutritionistBean.getAddress().getCap(),
                            nutritionistBean.getAddress().getCitta(),
                            nutritionistBean.getAddress().getProvincia(),
                            nutritionistBean.getAddress().getRegione()
                    )
            );
        } else {
            user = new User(
                    bean.getUsername(),
                    bean.getPassword(),
                    bean.getName(),
                    bean.getSurname(),
                    bean.getEmail(),
                    bean.getPassword(),
                    role
            );
        }
        return user;
    }

    //Metodi per rendere più chiaro il codice
    public static Nutritionist beanToNutritionist(NutritionistBean bean) {
        return (Nutritionist) beanToUser(bean);
    }

    public static Patient beanToPatient(PatientBean bean) {
        return (Patient) beanToUser(bean);
    }
}

