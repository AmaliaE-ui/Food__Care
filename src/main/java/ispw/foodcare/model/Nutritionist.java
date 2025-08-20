package ispw.foodcare.model;

import ispw.foodcare.Role;

public class Nutritionist extends User {

    private String piva;
    private String titoloStudio;
    private String specializzazione;

    private Address address;

    public record Credentials(String username, String password) {}
    public record Anagraphic(String name, String surname, String email, String phone) {}
    public record NutritionistProfile(String piva, String titoloStudio, String specializzazione, Address address) {}

    public Nutritionist(Credentials c, Anagraphic a, NutritionistProfile p, Role role){
        super(c.username, c.password, a.name, a.surname, a.email, a.phone, role);
        this.piva = p.piva;
        this.titoloStudio = p.titoloStudio;
        this.specializzazione = p.specializzazione;
        this.address = p.address;
    }

    //Getter e Setter
    public String getPiva() { return piva; }

    public String getTitoloStudio() { return titoloStudio; }

    public String getSpecializzazione() { return specializzazione; }
    public void setSpecializzazione(String specializzazione) { this.specializzazione = specializzazione; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
}

