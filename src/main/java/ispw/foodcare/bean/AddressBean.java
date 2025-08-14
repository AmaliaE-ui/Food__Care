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
        String v = nonBlank(via, "La via non può essere vuota.");
        // Consenti lettere, cifre, spazio, apostrofo, punto, virgola, trattino
        for (int i = 0; i < v.length(); i++) {
            char c = v.charAt(i);
            if (!Character.isLetter(c) && !Character.isDigit(c) &&
                    c != ' ' && c != '\'' && c != '’' && c != '.' && c != ',' && c != '-') {
                throw new IllegalArgumentException("La via contiene caratteri non ammessi.");
            }
        }
        this.via = v;
    }

    public String getCivico() { return civico; }
    public void setCivico(String civico) {
        String v = nonBlank(civico, "Il numero civico non può essere vuoto.");
        this.civico = v;
    }


    public String getCap() { return cap; }
    public void setCap(String cap) {
        String v = nonBlank(cap, "Il CAP non può essere vuoto.");
        if (v.length() != 5) throw new IllegalArgumentException("Il CAP deve avere 5 cifre.");
        for (int i = 0; i < v.length(); i++) {
            if (!Character.isDigit(v.charAt(i))) {
                throw new IllegalArgumentException("Il CAP deve contenere solo cifre.");
            }
        }
        this.cap = v;
    }

    public String getCitta() { return citta; }
    public void setCitta(String citta) {
        String v = nonBlank(citta, "La città non può essere vuota.");
        for (int i = 0; i < v.length(); i++) {
            char c = v.charAt(i);
            if (!Character.isLetter(c) && c != ' ' && c != '\'' && c != '’' && c != '-') {
                throw new IllegalArgumentException("La città contiene caratteri non ammessi.");
            }
        }
        this.citta = v;
    }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) {
        String v = nonBlank(provincia, "La provincia non può essere vuota.").toUpperCase();
        if (v.length() != 2 || !isAsciiLetter(v.charAt(0)) || !isAsciiLetter(v.charAt(1))) {
            throw new IllegalArgumentException("La provincia deve contenere esattamente 2 lettere.");
        }
        this.provincia = v;
    }

    private boolean isAsciiLetter(char c){
        return (c >= 'A' && c <= 'Z');
    }

    public String getRegione() { return regione; }
    public void setRegione(String regione) {
        String v = nonBlank(regione, "La regione non può essere vuota.");
        for (int i = 0; i < v.length(); i++) {
            char c = v.charAt(i);
            if (!Character.isLetter(c) && c != ' ' && c != '\'' && c != '’' && c != '-') {
                throw new IllegalArgumentException("La regione contiene caratteri non ammessi.");
            }
        }
        this.regione = v;
    }

    /*Metodo di supporto*/
    private static String nonBlank(String s, String msg){
        if (s == null) throw new IllegalArgumentException(msg);
        String t = s.trim();
        if (t.isEmpty()) throw new IllegalArgumentException(msg);
        return t;
    }
}
