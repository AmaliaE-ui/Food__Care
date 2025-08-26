package ispw.foodcare.utils;

import ispw.foodcare.Role;
import ispw.foodcare.bean.*;
import ispw.foodcare.model.*;

public class Converter {
    /*Costruttore privato per evitare istanziazione*/
    private Converter() {}

    /*Metodo entity -> bean*/
    public static UserBean userToBean(User user) {
        if (user == null) return null;

        //Base - UserBean
        UserBean bean;

        if(user instanceof Patient patient){
            PatientBean patientBean = new PatientBean();

            /*Campi di Patient*/
            patientBean.setBirthDate(patient.getBirthDate());
            patientBean.setGender(patient.getGender());
            bean = patientBean;
        } else if (user instanceof Nutritionist nutritionist) {
            NutritionistBean nutritionistBean = new NutritionistBean();

            /*Campi di Nutritionist*/
            nutritionistBean.setPiva(nutritionist.getPiva());
            nutritionistBean.setTitoloStudio(nutritionist.getTitoloStudio());
            nutritionistBean.setSpecializzazione(nutritionist.getSpecializzazione());
            nutritionistBean.setAddress(addressToBean(nutritionist.getAddress()));
            bean = nutritionistBean;
        } else {
            bean = new UserBean();
        }

        /*Campi comuni di User*/
        bean.setUsername(user.getUsername());
        bean.setPassword(user.getPassword());
        bean.setName(user.getName());
        bean.setSurname(user.getSurname());
        bean.setEmail(user.getEmail());
        bean.setPhoneNumber(user.getPhoneNumber());
        bean.setRole(user.getRole()); // RoleEnum -> String
        return bean;
    }

    /*Metodo entity -> bean*/
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

    /*Metodo bean -> entity*/
    public static User beanToUser(UserBean bean) {
        if (bean == null) return null;

        User user;
        Role role = bean.getRole();

        if(bean instanceof PatientBean patientBean){
            var creds = new Patient.Credentials(patientBean.getUsername(), patientBean.getPassword());
            var anag = new Patient.Anagraphic(
                    patientBean.getName(),
                    patientBean.getSurname(),
                    patientBean.getEmail(),
                    patientBean.getPhoneNumber()
            );
            var profile = new Patient.PatientProfile(patientBean.getBirthDate(), patientBean.getGender());

            user = new Patient(creds, anag, role, profile);
        } else if(bean instanceof NutritionistBean nutritionistBean){
            var creds = new Nutritionist.Credentials(nutritionistBean.getUsername(), nutritionistBean.getPassword());
            var anag = new Nutritionist.Anagraphic(
                    nutritionistBean.getName(),
                    nutritionistBean.getSurname(),
                    nutritionistBean.getEmail(),
                    nutritionistBean.getPhoneNumber()
            );
            var profile = new Nutritionist.NutritionistProfile(
                    nutritionistBean.getPiva(),
                    nutritionistBean.getTitoloStudio(),
                    nutritionistBean.getSpecializzazione(),
                    new Address(
                            nutritionistBean.getAddress().getVia(),
                            nutritionistBean.getAddress().getCivico(),
                            nutritionistBean.getAddress().getCap(),
                            nutritionistBean.getAddress().getCitta(),
                            nutritionistBean.getAddress().getProvincia(),
                            nutritionistBean.getAddress().getRegione())
            );
            user = new Nutritionist(creds, anag, profile, role);
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

    public static Nutritionist beanToNutritionist(NutritionistBean bean) {
        return (Nutritionist) beanToUser(bean);
    }

    public static Patient beanToPatient(PatientBean bean) {
        return (Patient) beanToUser(bean);
    }

    /*Metodo  Entity -> Bean*/
    public static AppointmentBean appointmentToBean(Appointment appointment) {
        AppointmentBean bean = new AppointmentBean();
        bean.setDate(appointment.getDate());
        bean.setTime(appointment.getTime());
        bean.setNotes(appointment.getNotes());
        bean.setNutritionistUsername(appointment.getNutritionistUsername());
        bean.setStatus(appointment.getStatus());
        return bean;
    }

}

