package ispw.foodcare.model;

 public class Address {

     private String via;
     private String civico;
     private String cap;
     private String citta;
     private String provincia;
     private String regione;

     public Address(String via, String civico, String cap, String citta, String provincia, String regione) {
         this.via = via;
         this.civico = civico;
         this.cap = cap;
         this.citta = citta;
         this.provincia = provincia;
         this.regione = regione;
     }

     //Getter e Setter
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
