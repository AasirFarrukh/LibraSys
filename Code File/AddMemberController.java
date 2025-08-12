package application;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddMemberController extends BaseController {
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
    
    @FXML
    private TextField name;

    @FXML
    private JFXButton addButton;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private TextField address;

    @FXML
    private TextField phoneNo;
    
    @FXML
    private TextField cnic;
    
	@Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }
	
	@FXML
    private void addMember() {
        try {
            // Validate input fields
            if (!validateInput()) {
                return;
            }

            // Establish a connection to the database
            try (Connection con = getConnection()) {
                System.out.println("Database is connected successfully");
                
                if (cnicExists(con, cnic.getText())) {
                    showAlert(Alert.AlertType.WARNING, "Warning", "Member with the provided CNIC already exists.");
                    return;
                }

                // SQL query to insert patron data into the database
                String sql = "INSERT INTO member (name, email, password, cnic, address, phoneNo) VALUES (?, ?, ?, ?, ?, ?)";

                // Create a prepared statement
                try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {

                    // Set values based on your TextField inputs
                    preparedStatement.setString(1, name.getText());
                    preparedStatement.setString(2, email.getText());
                    preparedStatement.setString(3, password.getText());
                    preparedStatement.setString(4, cnic.getText());
                    preparedStatement.setString(5, address.getText());
                    preparedStatement.setString(6, phoneNo.getText());

                    // Execute the insert statement
                    int rowsInserted = preparedStatement.executeUpdate();

                    if (rowsInserted > 0) {
                        System.out.println("Member data inserted successfully!");
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Member data inserted successfully!");
                    } 
                    else {
                        System.out.println("No rows were inserted.");
                        showAlert(Alert.AlertType.WARNING, "Warning", "No rows were inserted.");
                    }
                }
            }
        } 
        catch (SQLException e) {
            System.err.println("An error occurred while connecting to the database or inserting data.");
            e.printStackTrace();
        }
    }
	
	private boolean cnicExists(Connection connection, String cnic) throws SQLException {
        String query = "SELECT * FROM member WHERE cnic = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cnic);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
	
	// Input validation method
    private boolean validateInput() {
        if (name.getText().isEmpty() || email.getText().isEmpty() || password.getText().isEmpty() || address.getText().isEmpty()
                || phoneNo.getText().isEmpty() || cnic.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields.");
            return false;
        }
        return true;
    }
	
	// Show alert method
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
	
	@FXML
    private void resetPage() {
		name.clear();
        email.clear();
        password.clear();
        cnic.clear();
        address.clear();
        phoneNo.clear();
	}
	
	// Switch Screen
	@FXML
    private void switchToHome(ActionEvent event) {
        switchScene("Dashboard.fxml", event);
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
