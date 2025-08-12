package application;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReturnBookController extends BaseController {
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
    private ListView<String> bookInfoList;

    @FXML
    private TextField loanBookID;
    
    boolean readyForReturn = false;
    
	@Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }
	
	@FXML
	void loadBookInfo(ActionEvent event) {
	    // Clear previous loan information from the ListView
	    bookInfoList.getItems().clear();
	    readyForReturn = false;

	    // Get the book ID from the TextField
	    String bookID = loanBookID.getText().trim();
        // Establish a connection to the database
        try (Connection con = getConnection()) {
            // SQL query to select loan information based on Book ID
            String sql = "SELECT loanDate, returnDate, renewalCount, memberID FROM loan WHERE bookID = ?";

            // Create a prepared statement
            try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                // Set the book ID parameter
                preparedStatement.setString(1, bookID);

                // Execute the query and get the result set
                ResultSet resultSet = preparedStatement.executeQuery();

                // Check if any loan information exists for the given book ID
                while (resultSet.next()) {
                    bookInfoList.getItems().add("Loan Information:");
                    // Extract loan information
                    String loanDate = resultSet.getString("loanDate");
                    String returnDate = resultSet.getString("returnDate");
                    int renewalCount = resultSet.getInt("renewalCount");
                    String memberID = resultSet.getString("memberID");
                    bookInfoList.getItems().add("  Issue Date: " + loanDate);
                    bookInfoList.getItems().add("  Return Date: " + returnDate);
                    bookInfoList.getItems().add("  Renewal Count: " + renewalCount);
                    bookInfoList.getItems().add("Book Information:");
                    
                    // add book details
                    String bookDetailsSql = "SELECT title, id, author FROM book WHERE isbn = ?";
                    try (PreparedStatement bookDetailsStatement = con.prepareStatement(bookDetailsSql)) {
                        bookDetailsStatement.setString(1, bookID);
                        ResultSet bookDetailsResultSet = bookDetailsStatement.executeQuery();

                        if (bookDetailsResultSet.next()) {
                            bookInfoList.getItems().add("  Book Name: " + bookDetailsResultSet.getString("title"));
                            bookInfoList.getItems().add("  Book ID: " + bookDetailsResultSet.getString("id"));
                            bookInfoList.getItems().add("  Author: " + bookDetailsResultSet.getString("author"));
                        }
                    }

                    bookInfoList.getItems().add("Member Information:");
                    
                    // add member details
                    String memberDetailsSql = "SELECT name, email FROM member WHERE id = ?";
                    try (PreparedStatement memberDetailsStatement = con.prepareStatement(memberDetailsSql)) {
                        memberDetailsStatement.setString(1, memberID);
                        ResultSet memberDetailsResultSet = memberDetailsStatement.executeQuery();

                        if (memberDetailsResultSet.next()) {
                            bookInfoList.getItems().add("  Member Name: " + memberDetailsResultSet.getString("name"));
                            bookInfoList.getItems().add("  Email: " + memberDetailsResultSet.getString("email"));
                        }
                    }
                }

                // Check if no loan information was found
                if (bookInfoList.getItems().isEmpty()) {
                    bookInfoList.getItems().add("No loan information found for the given book ID.");
                }
            }
        }
        catch (SQLException e) {
	        e.printStackTrace();
	    }
        readyForReturn = true;
    } 
	
	@FXML
	void returnBook(ActionEvent event) {
	    if (!readyForReturn) {
	        return;
	    }

	    // Get the book ID from the TextField
	    String bookID = loanBookID.getText().trim();

        // Establish a connection to the database
        try (Connection con = getConnection()) {
            // SQL query to check if the book is in the loan database
            String checkLoanSql = "SELECT 1 FROM loan WHERE bookID = ?";
            try (PreparedStatement checkLoanStatement = con.prepareStatement(checkLoanSql)) {
                // Set the book ID parameter
                checkLoanStatement.setString(1, bookID);

                // Execute the query and get the result set
                ResultSet checkLoanResultSet = checkLoanStatement.executeQuery();

                // Check if the book is in the loan database
                if (checkLoanResultSet.next()) {
                    String bookTitle = getBookTitle(con, bookID);

                    // Show a warning confirmation Alert
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Confirmation");
                    confirmationAlert.setHeaderText(null);
                    confirmationAlert.setContentText("Are you sure you want to return the book '" + bookTitle + "'?");
                    confirmationAlert.showAndWait();

                    if (confirmationAlert.getResult() == ButtonType.OK) {
                        // User confirmed, proceed with returning the book

                        // SQL query to delete loan information based on Book ID
                        String deleteLoanSql = "DELETE FROM loan WHERE bookID = ?";

                        // Create a prepared statement
                        try (PreparedStatement deleteLoanStatement = con.prepareStatement(deleteLoanSql)) {
                            // Set the book ID parameter
                            deleteLoanStatement.setString(1, bookID);

                            // Execute the delete statement
                            int rowsDeleted = deleteLoanStatement.executeUpdate();

                            if (rowsDeleted > 0) {
                                System.out.println("Loan information deleted successfully!");

                                // Update the book status to available
                                String updateBookStatusSql = "UPDATE book SET status = ? WHERE isbn = ?";
                                try (PreparedStatement updateBookStatusStatement = con.prepareStatement(updateBookStatusSql)) {
                                    updateBookStatusStatement.setBoolean(1, true);
                                    updateBookStatusStatement.setString(2, bookID);
                                    int rowsUpdated = updateBookStatusStatement.executeUpdate();

                                    if (rowsUpdated > 0) {
                                        System.out.println("Book status updated to available successfully!");

                                        // Show an alert for successful return
                                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                                        successAlert.setTitle("Return Successful");
                                        successAlert.setHeaderText(null);
                                        successAlert.setContentText("The book has been successfully returned.");
                                        successAlert.showAndWait();
                                        // Clear the list and text field
                                        bookInfoList.getItems().clear();
                                        loanBookID.clear();
                                    } 
                                    else {
                                        System.out.println("Failed to update book status.");
                                    }
                                }
                            } 
                            else {
                                System.out.println("No loan information found for the given book ID.");
                            }
                        }
                    }
                } 
                else {
                    // Book is not in the loan database
                    Alert notInLoanAlert = new Alert(Alert.AlertType.ERROR);
                    notInLoanAlert.setTitle("Error");
                    notInLoanAlert.setHeaderText(null);
                    notInLoanAlert.setContentText("The selected book is not currently on loan.");
                    notInLoanAlert.showAndWait();
                }
            }
        }
        catch (SQLException e) {
        	e.printStackTrace();
        }
    }

	// Method to get the title of a book based on its ID
	private String getBookTitle(Connection con, String bookID) throws SQLException {
	    String bookTitle = "Book Not Found";

	    // SQL query to select book title based on Book ID
	    String selectBookTitleSql = "SELECT title FROM book WHERE isbn = ?";
	    try (PreparedStatement selectBookTitleStatement = con.prepareStatement(selectBookTitleSql)) {
	        // Set the book ID parameter
	        selectBookTitleStatement.setString(1, bookID);

	        // Execute the query and get the result set
	        ResultSet bookTitleResultSet = selectBookTitleStatement.executeQuery();

	        // Check if a book with the given ID exists
	        if (bookTitleResultSet.next()) {
	            bookTitle = bookTitleResultSet.getString("title");
	        }
	        return bookTitle;
	    }
	}

	@FXML
	void renewBook(ActionEvent event) {
	    if (!readyForReturn) {
	        return;
	    }
	    // Get the book ID from the TextField
	    String bookID = loanBookID.getText().trim();
        // Establish a connection to the database
        try (Connection con = getConnection()) {
            // SQL query to check if the book is in the loan database
            String checkLoanSql = "SELECT * FROM loan WHERE bookID = ?";
            try (PreparedStatement checkLoanStatement = con.prepareStatement(checkLoanSql)) {
                // Set the book ID parameter
                checkLoanStatement.setString(1, bookID);

                // Execute the query and get the result set
                ResultSet checkLoanResultSet = checkLoanStatement.executeQuery();

                // Check if the book is in the loan database
                if (checkLoanResultSet.next()) {
                    String bookTitle = getBookTitle(con, bookID);

                    // Show a warning confirmation dialog
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Confirmation");
                    confirmationAlert.setHeaderText(null);
                    confirmationAlert.setContentText("Do you want to renew the book '" + bookTitle + "'?");
                    confirmationAlert.showAndWait();

                    if (confirmationAlert.getResult() == ButtonType.OK) {
                        // User confirmed, proceed with renewing the book

                        // Get the current return date
                        String currentReturnDate = checkLoanResultSet.getString("returnDate");

                        // Extend the return date by 1 week
                        String updatedReturnDate = extendReturnDate(currentReturnDate);

                        // Increment the renewal count
                        int currentRenewalCount = checkLoanResultSet.getInt("renewalCount");
                        int updatedRenewalCount = currentRenewalCount + 1;

                        // SQL query to update return date and renewal count
                        String updateLoanSql = "UPDATE loan SET returnDate = ?, renewalCount = ? WHERE bookID = ?";
                        try (PreparedStatement updateLoanStatement = con.prepareStatement(updateLoanSql)) {
                            updateLoanStatement.setString(1, updatedReturnDate);
                            updateLoanStatement.setInt(2, updatedRenewalCount);
                            updateLoanStatement.setString(3, bookID);

                            // Execute the update statement
                            int rowsUpdated = updateLoanStatement.executeUpdate();

                            if (rowsUpdated > 0) {
                                System.out.println("Return date and renewal count updated successfully!");

                                // Display the updated information
                                loadBookInfo(event);
                            } 
                            else {
                                System.out.println("Failed to update return date and renewal count.");
                            }
                        }
                    }
                } 
                else {
                    // Book is not in the loan database
                    Alert notInLoanAlert = new Alert(Alert.AlertType.ERROR);
                    notInLoanAlert.setTitle("Error");
                    notInLoanAlert.setHeaderText(null);
                    notInLoanAlert.setContentText("The selected book is not currently on loan.");
                    notInLoanAlert.showAndWait();
                }
            }
        }
        catch (SQLException e) {
        	e.printStackTrace();
	    }
	}

	// Method to extend the return date by 1 week
	private String extendReturnDate(String currentReturnDate) {
	    String updatedReturnDate = "0000-00-00";
	    try {
	        java.sql.Date currentDate = java.sql.Date.valueOf(currentReturnDate);
	        long currentMillis = currentDate.getTime();
	        long oneWeekMillis = 7 * 24 * 60 * 60 * 1000; // 7 days in milliseconds
	        long updatedMillis = currentMillis + oneWeekMillis;

	        java.sql.Date updatedDate = new java.sql.Date(updatedMillis);
	        updatedReturnDate = updatedDate.toString();
	    } 
	    catch (IllegalArgumentException e) {
	        e.printStackTrace();
	    }

	    return updatedReturnDate;
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
    private void switchToIssueBook(ActionEvent event) {
        switchScene("IssueBook.fxml", event);
    }
	
	@FXML
    private void logout(ActionEvent event) {
        switchScene("Login.fxml", event);
    }
}
