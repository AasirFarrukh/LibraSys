package application;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller extends BaseController {
    @FXML
    private ImageView Exit;

    @FXML
    private JFXButton Logout;

    @FXML
    private JFXButton Menu;

    @FXML
    private JFXButton MenuClose;

    @FXML
    private ImageView centerImageView;

    @FXML
    private AnchorPane slider;
    
	@Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }
    
	@FXML
    private void switchToAddBook(ActionEvent event) {
        switchScene("AddBook.fxml", event);
    }
	
	@FXML
    private void switchToCatalog(ActionEvent event) {
        switchScene("ViewCatalog.fxml", event);
    }
	
	@FXML
    private void switchToMember(ActionEvent event) {
        switchScene("AddMember.fxml", event);
    }
	
	@FXML
    private void switchToViewMember(ActionEvent event) {
        switchScene("ViewMember.fxml", event);
    }
	
	@FXML
    private void switchToIssueBook(ActionEvent event) {
        switchScene("IssueBook.fxml", event);
    }
	
	@FXML
    private void switchToReturnBook(ActionEvent event) {
        switchScene("ReturnBook.fxml", event);
    }
	
	@FXML
    private void logout(ActionEvent event) {
        switchScene("Login.fxml", event);
    }
}
