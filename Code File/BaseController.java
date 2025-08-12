package application;

import com.jfoenix.controls.JFXButton;

import Database.DatabaseManager;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public abstract class BaseController implements Initializable {
	
	protected Connection getConnection() throws SQLException {
	    return DatabaseManager.getConnection();
	}

    @FXML
    protected ImageView Exit;

    @FXML
    protected JFXButton Logout;

    @FXML
    protected JFXButton Menu;

    @FXML
    protected JFXButton MenuClose;

    @FXML
    protected ImageView centerImageView;

    @FXML
    protected AnchorPane slider;
    
    protected Stage stage;
    protected javafx.scene.Scene scene;
    protected javafx.scene.Parent root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Exit.setOnMouseClicked(event -> {
            System.exit(0);
        });

        // Set the initial state of the slider to fully closed
        slider.setTranslateX(-176);

        // Highlight the "Menu" button initially
        highlightMenuButton(false);

        Menu.setOnMouseClicked(event -> {
            toggleMenu(true);
        });

        MenuClose.setOnMouseClicked(event -> {
            toggleMenu(false);
        });
    }

    private void toggleMenu(boolean open) {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(slider);

        if (open) {
            // Adjust the initial state of the slider before playing the animation
            slider.setTranslateX(-176);

            slide.setToX(0);
            slide.play();
            slide.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(false);
                MenuClose.setVisible(true);
                highlightMenuButton(true);
            });
        } 
        else {
            slide.setToX(-176);
            slide.play();
            slide.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(true);
                MenuClose.setVisible(false);
                highlightMenuButton(false);
            });
        }
    }

    private void highlightMenuButton(boolean highlight) {
        if (highlight) {
            Menu.setStyle("-fx-text-fill: WHITE; -fx-background-color: #146886; -fx-border-color: WHITE; -fx-border-width: 0px 0px 0px 3px;");
        } 
        else {
            Menu.setStyle("-fx-text-fill: WHITE; -fx-background-color: transparent; -fx-border-color: transparent;");
        }
    }
    
    // Switch screen method
    protected void switchScene(String fxmlPath, ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource(fxmlPath));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new javafx.scene.Scene(root);
            stage.setScene(scene);
            stage.show();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
