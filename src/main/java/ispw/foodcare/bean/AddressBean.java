package ispw.foodcare.bean;

public class AddressBean {

    private String via;
    private String civico;
    private String cap;
    private String citta;
    private String provincia;
    private String regione;

    public AddressBean() {}

    public String getVia() { return via; }
    public void setVia(String via) {
        /*Metodo trim() non tocca la stringa, ma elimina gli spazi vuoti all'inizio e alla fine*/
        if (via == null || via.trim().isEmpty()) {
            throw new IllegalArgumentException("La via non può essere vuota.");
        }
        // Controllo che sia composta solo da lettere e spazi
        for (char c : via.toCharArray()) {
            if (!Character.isLetter(c) && !Character.isSpaceChar(c)) {
                throw new IllegalArgumentException("La via può contenere solo lettere e spazi.");
            }
        }
        this.via = via.trim();
    }

    public String getCivico() { return civico; }
    public void setCivico(String civico) {
        if (civico == null || civico.trim().isEmpty()) {
            throw new IllegalArgumentException("Il numero civico non può essere vuoto.");
        }
        // Deve essere solo numeri
        for (char c : civico.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("Il numero civico deve contenere solo cifre.");
            }
        }
        this.civico = civico.trim();
    }

    public String getCap() { return cap; }
    public void setCap(String cap) {
        if (cap == null || cap.length() != 5) {
            throw new IllegalArgumentException("Inserimento errato - Il CAP deve essere lungo 5 cifre.");
        }
        for (char c : cap.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("Inserimento errato - Il CAP deve contenere solo numeri.");
            }
        }
        this.cap = cap.trim();
    }

    public String getCitta() { return citta; }
    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) {
        if (provincia == null || provincia.length() != 2) {
            throw new IllegalArgumentException("Inserimento errato - La provincia deve contenere esattamente 2 caratteri.");
        }
        // Deve essere solo lettere
        for (char c : provincia.toCharArray()) {
            if (!Character.isLetter(c)) {
                throw new IllegalArgumentException("Inserimento errato - La provincia deve contenere solo lettere.");
            }
        }
        this.provincia = provincia.toUpperCase();
    }

    public String getRegione() { return regione; }
    public void setRegione(String regione) {
        this.regione = regione;
    }
}
