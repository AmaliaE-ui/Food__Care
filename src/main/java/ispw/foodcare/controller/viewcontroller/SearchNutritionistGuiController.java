package ispw.foodcare.controller.viewcontroller;

import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.controller.applicationcontroller.BookAppointmentController;
import ispw.foodcare.model.Session;
import ispw.foodcare.utils.NavigationManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class SearchNutritionistGuiController {

    private String lastSearchResults = "lastSearchResults";
    private String lastSearchQuery = "lastSearchQuery";

    @FXML private TextField searchTextField;
    @FXML private TableView<NutritionistBean> nutritionistTableView;
    @FXML private TableColumn<NutritionistBean, String> nameColumn;
    @FXML private TableColumn<NutritionistBean, String> surnameColumn;
    @FXML private TableColumn<NutritionistBean, String> cityColumn;
    @FXML private TableColumn<NutritionistBean, String> specializationColumn;
    @FXML private TableColumn<NutritionistBean, Void> profileColumn;

    @FXML
    public void initialize() {
        nutritionistTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        profileColumn.setMinWidth(120);
        //impedisce all'utente di ridimensionare colonna
        profileColumn.setReorderable(false);

        Label placeholderLabel = new Label("Inserisci una città per iniziare la ricerca");
        nutritionistTableView.setPlaceholder(placeholderLabel);

        setUpColumns();
        setUpProfileButton();

        // Ripristino dei dati precedenti da sessione
        ObservableList<NutritionistBean> cachedResults =
                (ObservableList<NutritionistBean>) Session.getInstance().getAttributes(lastSearchResults);
        if (cachedResults != null && !cachedResults.isEmpty()) {
            nutritionistTableView.setItems(cachedResults);
        }

        String previousQuery = (String) Session.getInstance().getAttributes(lastSearchQuery);
        if (previousQuery != null) {
            searchTextField.setText(previousQuery);
        }
    }

    /*Colonne con il corretto binding per visualizzare i dati.*/
    private void setUpColumns() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        surnameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSurname()));
        cityColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAddress() != null
                        ? cellData.getValue().getAddress().getCitta()
                        : ""));
        specializationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSpecializzazione()));
    }

    /*Pulsante "Visualizza profilo" all'interno della tabella.*/
    private void setUpProfileButton() {
        profileColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Visualizza profilo");

            {
                btn.setStyle("-fx-background-color: #87cefa; -fx-text-fill: black; -fx-font-weight: bold;");
                btn.setOnAction(event -> {
                    NutritionistBean bean = getTableView().getItems().get(getIndex());
                    handleViewProfile(bean);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    @FXML
    private void onSearchClicked() {
        String city = searchTextField.getText();
        if (city == null || city.isBlank()) {
            showAlert("Campo vuoto", "Inserisci una città per effettuare la ricerca.");
            return;
        }

        BookAppointmentController controller = new BookAppointmentController();
        List<NutritionistBean> result = controller.searchNutritionistsByCity(city);

        if (result.isEmpty()) {
            showAlert("Nessun risultato", "Nessun nutrizionista trovato nella città \"" + city + "\".");
            nutritionistTableView.getItems().clear();
            Session.getInstance().removeAttribute(lastSearchResults);
            Session.getInstance().removeAttribute(lastSearchQuery);
            return;
        }

        ObservableList<NutritionistBean> data = FXCollections.observableArrayList(result);
        nutritionistTableView.setItems(data);

        // Salva in sessione i dati per ripristino al ritorno
        Session.getInstance().setAttributes(lastSearchResults, data);
        Session.getInstance().setAttributes(lastSearchQuery, city);
    }

    /*Alert generico.*/
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /*Carica la schermata del profilo nutrizionista.*/
    private void handleViewProfile(NutritionistBean bean) {
        NavigationManager.switchPane("/ispw/foodcare/BookAppointment/nutritionistProfile.fxml",
                searchTextField,
                bean
        );
    }
}
