package ispw.foodcare.controller.viewcontroller;

import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.controller.applicationcontroller.BookAppointmentController;
import ispw.foodcare.model.Session;
import ispw.foodcare.utils.NavigationManager;
import ispw.foodcare.utils.ShowAlert;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class SearchNutritionistGuiController {

    /*costanti*/
    private static final String ATTR_LAST_SEARCH_RESULTS = "lastSearchResults";
    private static final String ATTR_LAST_SEARCH_QUERY   = "lastSearchQuery";

    @FXML private TextField searchTextField;
    @FXML private TableView<NutritionistBean> nutritionistTableView;
    @FXML private TableColumn<NutritionistBean, String> nameColumn;
    @FXML private TableColumn<NutritionistBean, String> surnameColumn;
    @FXML private TableColumn<NutritionistBean, String> cityColumn;
    @FXML private TableColumn<NutritionistBean, String> specializationColumn;
    @FXML private TableColumn<NutritionistBean, Void>   profileColumn;

    /*evita NPE*/
    private final ShowAlert alert = new ShowAlert();
    /*riuso il controller*/
    private final BookAppointmentController controller = new BookAppointmentController();

    @FXML private void initialize() {

        nutritionistTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        profileColumn.setMinWidth(120);
        profileColumn.setReorderable(false);
        nutritionistTableView.setPlaceholder(new Label("Inserisci una città per iniziare la ricerca"));

        setUpColumns();
        setUpProfileButton();  // usa la cella tipizzata (vedi sotto)

        /*ripristino cache da Session*/
        @SuppressWarnings("unchecked")
        var cachedResults = (javafx.collections.ObservableList<NutritionistBean>)
                Session.getInstance().getAttributes(ATTR_LAST_SEARCH_RESULTS);
        if (cachedResults != null && !cachedResults.isEmpty()) {
            nutritionistTableView.setItems(cachedResults);
        }
        String previousQuery = (String) Session.getInstance().getAttributes(ATTR_LAST_SEARCH_QUERY);
        if (previousQuery != null) searchTextField.setText(previousQuery);

    }

    private void setUpColumns() {
        nameColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getName()));
        surnameColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getSurname()));
        cityColumn.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().getAddress() != null ? cd.getValue().getAddress().getCitta() : ""));
        specializationColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getSpecializzazione()));
    }

    /*Pulsante "Visualizza profilo" in colonna – niente initializer di istanza*/
    private void setUpProfileButton() {
        profileColumn.setCellFactory(col -> new ProfileButtonCell());
    }

    /*Cella dedicata con costruttore*/
    private final class ProfileButtonCell extends TableCell<NutritionistBean, Void> {
        private final Button btn = new Button("Visualizza profilo");
        ProfileButtonCell() {
            btn.setStyle("-fx-background-color: #87cefa; -fx-text-fill: black; -fx-font-weight: bold;");
            btn.setOnAction(e -> {
                NutritionistBean bean = getTableView().getItems().get(getIndex());
                onViewProfileClick(bean);
            });
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
        @Override protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            setGraphic(empty ? null : btn);
        }
    }

    @FXML private void onSearchClick() {
        String city = searchTextField.getText();
        if (city == null || city.isBlank()) {
            alert.showAlert("Campo vuoto", "Inserisci una città per effettuare la ricerca.");
            return;
        }
        List<NutritionistBean> result = controller.searchNutritionistsByCity(city.trim());
        if (result.isEmpty()) {
            alert.showAlert("Nessun risultato", "Nessun nutrizionista trovato nella città \"" + city + "\".");
            nutritionistTableView.getItems().clear();
            Session.getInstance().removeAttribute(ATTR_LAST_SEARCH_RESULTS);
            Session.getInstance().removeAttribute(ATTR_LAST_SEARCH_QUERY);
            return;
        }
        var data = FXCollections.observableArrayList(result);
        nutritionistTableView.setItems(data);
        Session.getInstance().setAttributes(ATTR_LAST_SEARCH_RESULTS, data);
        Session.getInstance().setAttributes(ATTR_LAST_SEARCH_QUERY, city.trim());
    }

    @FXML private void onViewProfileClick(NutritionistBean bean) {
        NavigationManager.switchPane(
                "/ispw/foodcare/BookAppointment/nutritionistProfile.fxml",
                searchTextField,
                NutritionistProfileGuiController.class,
                c -> c.setNutritionistBean(bean)
        );
    }
}

