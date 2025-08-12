package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LoginController implements Initializable {
    @FXML
    private ImageView Exit;
    
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    public void initialize(URL location, ResourceBundle resources) {
        // functionality to exit
        Exit.setOnMouseClicked(event -> {
            closeProgram();
        });
    }

    @FXML
    private void switchToHome(ActionEvent event) {
        switchScene("Dashboard.fxml", event);
    }

    private void switchScene(String sceneName, ActionEvent event) {
        // Get the entered username and password
        String enteredUsername = username.getText();
        String enteredPassword = password.getText();

        // Authenticate the user against the database
        if (authenticateUser(enteredUsername, enteredPassword)) {
            // Switch to the home screen
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName));
                Parent root = loader.load();

                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Display an error message
            showAlert("Authentication Error", "Invalid username or password.");
        }
    }
    
    private boolean authenticateUser(String enteredUsername, String enteredPassword) {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Database connection parameters
            String url = "jdbc:mysql://localhost:3306/Final_project";
            String dbUsername = "root";
            String dbPassword = "2105422";

            try (Connection con = DriverManager.getConnection(url, dbUsername, dbPassword)) {
                String sql = "SELECT * FROM person WHERE name = ? AND password = ?";
                try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                    preparedStatement.setString(1, enteredUsername);
                    preparedStatement.setString(2, enteredPassword);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    // Check if a matching record is found
                    return resultSet.next();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeProgram() {
        Stage stage = (Stage) Exit.getScene().getWindow();
        stage.close();
    }
}
