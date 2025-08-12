package application;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.effects.JFXDepthManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class IssueBookController extends BaseController {

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
    private HBox bookInfo;

    @FXML
    private HBox memberInfo;

    @FXML
    private Text BookAuthor;

    @FXML
    private Text BookName;

    @FXML
    private Text BookStatus;

    @FXML
    private TextField bookIDInfo;
    
    @FXML
    private Text MemberContact;

    @FXML
    private Text MemberName;
    
    @FXML
    private TextField memberIDInput;
    
    @FXML
    private JFXButton IssueLoanButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        // Additional initialization for the main controller, if needed
        JFXDepthManager.setDepth(bookInfo, 1);
        JFXDepthManager.setDepth(memberInfo, 1);
    }

    @FXML
    void loadBookInfo(ActionEvent event) {
        String bookID = bookIDInfo.getText().trim();

            try (Connection con = getConnection()) {
                // SQL query to select book information based on Book ID
                String sql = "SELECT title, author, status FROM book WHERE isbn = ?";

                // Create a prepared statement
                try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                    // Set the book ID parameter
                    preparedStatement.setString(1, bookID);

                    // Execute the query and get the result set
                    ResultSet resultSet = preparedStatement.executeQuery();

                    // Check if a book with the given ID exists
                    if (resultSet.next()) {
                        // Display book information
                        BookName.setText("Book Name: " + resultSet.getString("title"));
                        BookAuthor.setText("Author: " + resultSet.getString("author"));
                        String status = resultSet.getBoolean("status") ? "Available" : "Not Available";
                        BookStatus.setText("Status: " + status);
                    } else {
                        // Book with the given ID not found
                        BookName.setText("Book Not Found");
                        BookAuthor.setText("");
                        BookStatus.setText("");
                    }
                }
            } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions, show alert, etc.
        }
    }
    
    @FXML
    void loadMemberInfo(ActionEvent event) {
        String memberID = memberIDInput.getText().trim();

            // Establish a connection to the database
            try (Connection con = getConnection()) {
                // SQL query to select member information based on Member ID
                String sql = "SELECT name, email FROM member WHERE id = ?";

                // Create a prepared statement
                try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                    // Set the member ID parameter
                    preparedStatement.setString(1, memberID);

                    // Execute the query and get the result set
                    ResultSet resultSet = preparedStatement.executeQuery();

                    // Check if a member with the given ID exists
                    if (resultSet.next()) {
                        // Display member information
                        MemberName.setText("Member Name: " + resultSet.getString("name"));
                        MemberContact.setText("Email: " + resultSet.getString("email"));
                    } else {
                        // Member with the given ID not found
                        MemberName.setText("Member Not Found");
                        MemberContact.setText("");
                    }
                }
            } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions, show alert, etc.
        }
    }
    
    @FXML
    void loanBook(ActionEvent event) {
        String memberID = memberIDInput.getText().trim();
        String bookID = bookIDInfo.getText().trim();

        // Establish a connection to the database
        try (Connection con = getConnection()) {
        	
            // Check if the book is available
            String checkAvailabilitySql = "SELECT status FROM book WHERE isbn = ?";
            try (PreparedStatement checkAvailabilityStatement = con.prepareStatement(checkAvailabilitySql)) {
                checkAvailabilityStatement.setString(1, bookID);
                ResultSet availabilityResultSet = checkAvailabilityStatement.executeQuery();

                if (availabilityResultSet.next()) {
                    boolean isBookAvailable = availabilityResultSet.getBoolean("status");

                    if (isBookAvailable) {
                        // Show a confirmation alert
                        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmationAlert.setTitle("Confirmation");
                        confirmationAlert.setHeaderText(null);
                        confirmationAlert.setContentText("Are you sure you want to issue this book to this member?");
                        confirmationAlert.showAndWait();
                        // User clicked OK, proceed with issuing the book
                        if (confirmationAlert.getResult() == ButtonType.OK) {
                            // SQL query to insert loan information into the table
                            String insertSql = "INSERT INTO loan (memberID, bookID) VALUES (?, ?)";

                            // Create a prepared statement
                            try (PreparedStatement insertStatement = con.prepareStatement(insertSql)) {
                                // Set values based on your TextField inputs
                                insertStatement.setString(1, memberID);
                                insertStatement.setString(2, bookID);

                                // Execute the insert statement
                                int rowsInserted = insertStatement.executeUpdate();

                                if (rowsInserted > 0) {
                                    System.out.println("Loan information inserted successfully!");

                                    // Update the book status to not available
                                    String updateBookStatusSql = "UPDATE book SET status = ? WHERE isbn = ?";
                                    try (PreparedStatement updateBookStatusStatement = con.prepareStatement(updateBookStatusSql)) {
                                        updateBookStatusStatement.setBoolean(1, false);
                                        updateBookStatusStatement.setString(2, bookID);
                                        int rowsUpdated = updateBookStatusStatement.executeUpdate();

                                        if (rowsUpdated > 0) {
                                            System.out.println("Book status updated successfully!");

                                            // Show a success alert
                                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                                            successAlert.setTitle("Success");
                                            successAlert.setHeaderText(null);
                                            successAlert.setContentText("Book issued successfully!");
                                            successAlert.showAndWait();

                                            // Clear fields
                                            bookIDInfo.clear();
                                            memberIDInput.clear();
                                            BookName.setText("Book Name");
                                            BookAuthor.setText("Author");
                                            BookStatus.setText("Status");
                                            MemberName.setText("Member Name");
                                            MemberContact.setText("Contact");
                                        } 
                                        else {
                                            System.out.println("Failed to update book status.");
                                        }
                                    }
                                } 
                                else {
                                    System.out.println("No rows were inserted for loan information.");
                                }
                            }
                        } 
                        else {
                            // User clicked Cancel or closed the dialog, do nothing
                            System.out.println("Book issuance canceled.");
                        }
                    } 
                    else {
                        // Book is not available
                        Alert notAvailableAlert = new Alert(Alert.AlertType.ERROR);
                        notAvailableAlert.setTitle("Error");
                        notAvailableAlert.setHeaderText(null);
                        notAvailableAlert.setContentText("The selected book is not available for loan.");
                        notAvailableAlert.showAndWait();
                    }
                } 
                else {
                    // Book with the given ID not found
                    System.out.println("Book Not Found");
                }
            }
        } 
        catch (SQLException e) {
        	e.printStackTrace();
        }
    }

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
    private void switchToMember(ActionEvent event) {
        switchScene("AddMember.fxml", event);
    }

    @FXML
    private void switchToViewMember(ActionEvent event) {
        switchScene("ViewMember.fxml", event);
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
