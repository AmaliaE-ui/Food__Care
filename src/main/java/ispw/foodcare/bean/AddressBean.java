package ispw.foodcare.bean;

public class AddressBean {

    private String via;
    private String civico;
    private String cap;
    private String citta;
    private String provincia;
    private String regione;

    // Costruttore vuoto
    public AddressBean() {}

    // Getter e setter
    public String getVia() { return via; }
    public void setVia(String via) { this.via = via; }

    public String getCivico() { return civico; }
    public void setCivico(String civico) { this.civico = civico; }

    public String getCap() { return cap; }
    public void setCap(String cap) { this.cap = cap; }

    public String getCitta() { return citta; }
    public void setCitta(String citta) { this.citta = citta; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public String getRegione() { return regione; }
    public void setRegione(String regione) { this.regione = regione; }
}