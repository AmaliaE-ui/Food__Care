package ispw.foodcare.bean;

public class NutritionistBean extends UserBean {

        private String piva;
        private String titoloStudio;
        private String specializzazione;

        private AddressBean address;

        public NutritionistBean() {
                //Costruttore vuoto
        }

        public AddressBean getAddress() {return address;}
        public void setAddress(AddressBean address) {this.address = address;}

        // Getter & Setter
        public String getPiva() { return piva; }
        public void setPiva(String piva) { this.piva = piva; }

        public String getTitoloStudio() { return titoloStudio; }
        public void setTitoloStudio(String titoloStudio) { this.titoloStudio = titoloStudio; }

        public String getSpecializzazione() { return specializzazione; }
        public void setSpecializzazione(String specializzazione) { this.specializzazione = specializzazione; }
}


