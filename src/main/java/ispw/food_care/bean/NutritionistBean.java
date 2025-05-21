package ispw.food_care.bean;

public class NutritionistBean extends UserBean {

        private String piva;
        private String titoloStudio;
        private String indirizzoStudio;
        private String specializzazione;

        private AddressBean address;

        public AddressBean getAddress() {return address;}
        public void setAddress(AddressBean address) {this.address = address;}

        // Getter & Setter
        public String getPiva() { return piva; }
        public void setPiva(String piva) { this.piva = piva; }

        public String getTitoloStudio() { return titoloStudio; }
        public void setTitoloStudio(String titoloStudio) { this.titoloStudio = titoloStudio; }

        public String getIndirizzoStudio() { return indirizzoStudio; }
        public void setIndirizzoStudio(String indirizzoStudio) { this.indirizzoStudio = indirizzoStudio; }

        public String getSpecializzazione() { return specializzazione; }
        public void setSpecializzazione(String specializzazione) { this.specializzazione = specializzazione; }
}


