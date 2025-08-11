package ispw.foodcare.bean;

public class NutritionistBean extends UserBean {

        private String piva;
        private String titoloStudio;
        private String specializzazione;
        private AddressBean address;

        // Costruttore vuoto
        public NutritionistBean() {}

        // Getter e Setter
        public AddressBean getAddress() {return address;}
        public void setAddress(AddressBean address) {
                if(address == null) {
                        throw new IllegalArgumentException("L'indirizzo non può essere nullo.");
                }
                this.address = address;}

        public String getPiva() { return piva; }
        public void setPiva(String piva) {
                if (piva == null || piva.trim().isEmpty()) {
                        throw new IllegalArgumentException("La partita IVA non può essere vuota.");
                }
                if (!piva.matches("\\d{11}")) { // formato standard di 11 numeri
                        throw new IllegalArgumentException("La partita IVA deve contenere 11 cifre.");
                }
                this.piva = piva.trim();
        }

        public String getTitoloStudio() { return titoloStudio; }
        public void setTitoloStudio(String titoloStudio) { this.titoloStudio = titoloStudio; }

        public String getSpecializzazione() { return specializzazione; }
        public void setSpecializzazione(String specializzazione) { this.specializzazione = specializzazione; }
}


