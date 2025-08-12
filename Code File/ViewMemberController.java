package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ViewMemberController extends BaseController {
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
    private TableView<Member> TableView;

    @FXML
    private TableColumn<Member, Integer> idCol;

    @FXML
    private TableColumn<Member, String> nameCol;

    @FXML
    private TableColumn<Member, String> emailCol;

    @FXML
    private TableColumn<Member, String> addressCol;

    @FXML
    private TableColumn<Member, String> phoneCol;

    private ObservableList<Member> memberList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        fetchDataFromDatabase();
        initializeTableColumns();
    }

    private void fetchDataFromDatabase() {
        // Establish a connection to the database
        try (Connection con = getConnection()) {
            // SQL query to select all columns from the member database
            String sql = "SELECT * FROM member";

            // Create a prepared statement
            try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                // Execute the query and get the result set
                ResultSet resultSet = preparedStatement.executeQuery();

                // Clear the existing data in the list
                memberList.clear();

                // Iterate through the result set and add data to the list
                while (resultSet.next()) {
                    Member member = new Member();
                    member.setId(resultSet.getInt("id"));
                    member.setName(resultSet.getString("name"));
                    member.setEmail(resultSet.getString("email"));
                    member.setAddress(resultSet.getString("address"));
                    member.setPhone(resultSet.getString("phoneNo"));

                    // Add the member to the list
                    memberList.add(member);
                }

                // Set the list as the data source for the TableView
                TableView.setItems(memberList);
            }
        } 
        catch (SQLException e) {
        	e.printStackTrace();
        }
    }

    private void initializeTableColumns() {
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        addressCol.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        phoneCol.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
    }

	public static class Member {
	    private final SimpleIntegerProperty id;
	    private final StringProperty name;
	    private final StringProperty email;
	    private final StringProperty address;
	    private final StringProperty phone;
	
	    public Member() {
	        this.id = new SimpleIntegerProperty();
	        this.name = new SimpleStringProperty();
	        this.email = new SimpleStringProperty();
	        this.address = new SimpleStringProperty();
	        this.phone = new SimpleStringProperty();
	    }
	
	    // id
	    public int getId() {
	        return id.get();
	    }
	
	    public void setId(int id) {
	        this.id.set(id);
	    }
	
	    public SimpleIntegerProperty idProperty() {
	        return id;
	    }
	
	    // name
	    public String getName() {
	        return name.get();
	    }
	
	    public void setName(String name) {
	        this.name.set(name);
	    }
	
	    public StringProperty nameProperty() {
	        return name;
	    }
	
	    // email
	    public String getEmail() {
	        return email.get();
	    }
	
	    public void setEmail(String email) {
	        this.email.set(email);
	    }
	
	    public StringProperty emailProperty() {
	        return email;
	    }
	
	    // address
	    public String getAddress() {
	        return address.get();
	    }
	
	    public void setAddress(String address) {
	        this.address.set(address);
	    }
	
	    public StringProperty addressProperty() {
	        return address;
	    }
	
	    // phone
	    public String getPhone() {
	        return phone.get();
	    }
	
	    public void setPhone(String phone) {
	        this.phone.set(phone);
	    }
	
	    public StringProperty phoneProperty() {
	        return phone;
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
