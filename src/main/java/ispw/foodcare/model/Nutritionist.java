package ispw.foodcare.model;

import ispw.foodcare.Role;

public class Nutritionist extends User {


    private String piva;
    private String titoloStudio;
    private String specializzazione;

    private Address address;

    public Nutritionist(String username, String password, String name, String surname, String email,
                        String phoneNumber, Role role, String piva, String titoloStudio, String specializzazione, Address address) {
        super(username, password, name, surname, email, phoneNumber, role);
        this.piva = piva;
        this.titoloStudio = titoloStudio;
        this.specializzazione = specializzazione;
        this.address = address;
    }

    //Getter e Setter
    public String getPiva() { return piva; }
    public void setPiva(String piva) { this.piva = piva; }

    public String getTitoloStudio() { return titoloStudio; }
    public void setTitoloStudio(String titoloStudio) { this.titoloStudio = titoloStudio; }

    public String getSpecializzazione() { return specializzazione; }
    public void setSpecializzazione(String specializzazione) { this.specializzazione = specializzazione; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
}

