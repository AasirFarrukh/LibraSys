package application;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddBookController extends BaseController {
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
    private JFXButton addButton;

    @FXML
    private TextField author;

    @FXML
    private TextField date;

    @FXML
    private TextField genre;
    
    @FXML
    private TextField orcid;

    @FXML
    private TextField isbn;

    @FXML
    private TextField status;

    @FXML
    private TextField title;

	@Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }
    
	@FXML
	private void addBook() {
	    System.out.println("Attempting to add book...");
	    try {
	        // Validate input fields
	        if (!validateInput()) {
	            return;
	        }

	        // Establish a connection to the database
	        try (Connection con = getConnection()) {
	            System.out.println("Database is connected successfully");

	            // Check if ISBN already exists
	            if (isISBNExists(con, isbn.getText())) {
	                showAlert(Alert.AlertType.ERROR, "Error", "ISBN already exists in the database.");
	                return;
	            }

	            // SQL query to insert book data into the database
	            String sql = "INSERT INTO book (title, author, orcid, isbn, genre, date, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

	            // Create a prepared statement
	            try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {

	                // Set values based on your TextField inputs
	                preparedStatement.setString(1, title.getText());
	                preparedStatement.setString(2, author.getText());
	                preparedStatement.setString(3, orcid.getText());
	                preparedStatement.setString(4, isbn.getText());
	                preparedStatement.setString(5, genre.getText());
	                preparedStatement.setString(6, date.getText());

	                // Validate and set the status
	                String statusInput = status.getText().toLowerCase();  // Convert to lowercase for case-insensitive check
	                boolean statusValue;

	                if (statusInput.equals("available")) {
	                    statusValue = true;
	                } 
	                else if (statusInput.equals("not available")) {
	                    statusValue = false;
	                } 
	                else {
	                    showAlert(Alert.AlertType.ERROR, "Error", "Status can only be 'Available' or 'Not Available'.");
	                    return;
	                }

	                preparedStatement.setBoolean(7, statusValue);

	                // Execute the insert statement
	                int rowsInserted = preparedStatement.executeUpdate();
	                
	                // Successful Insert
	                if (rowsInserted > 0) {
	                    System.out.println("Book data inserted successfully!");
	                    showAlert(Alert.AlertType.INFORMATION, "Success", "Book data inserted successfully!");
	                } 
	                else {	// Error
	                    System.out.println("No rows were inserted.");
	                    showAlert(Alert.AlertType.WARNING, "Warning", "No rows were inserted.");
	                }
	            }
	        }
	    } 
	    catch (SQLException e) {
	        System.err.println("An error occurred while connecting to the database or inserting data.");
	        e.printStackTrace();
	        showAlert(Alert.AlertType.ERROR, "Error", "Please fill credentials in correct format.");
	    }
	}

	private boolean isISBNExists(Connection connection, String isbn) throws SQLException {
	    // SQL query to check if ISBN already exists
	    String checkISBNQuery = "SELECT COUNT(*) FROM book WHERE isbn = ?";
	    try (PreparedStatement checkISBNStatement = connection.prepareStatement(checkISBNQuery)) {
	        checkISBNStatement.setString(1, isbn);
	        ResultSet resultSet = checkISBNStatement.executeQuery();
	        resultSet.next();
	        return resultSet.getInt(1) > 0;
	    }
	}

    @FXML
    private void resetPage() {
    	title.clear();	// Clear any text you entered
        author.clear();
        orcid.clear();
        isbn.clear();
        genre.clear();
        date.clear();
        status.clear();
    }

    // Input validation method
    private boolean validateInput() {
        if (title.getText().isEmpty() || author.getText().isEmpty() || orcid.getText().isEmpty() || isbn.getText().isEmpty()
                || genre.getText().isEmpty() || date.getText().isEmpty() || status.getText().isEmpty()) {
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
    
    // Screen switch
    @FXML
    private void switchToHome(ActionEvent event) {
        switchScene("Dashboard.fxml", event);
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
