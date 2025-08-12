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

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ViewCatalogController extends BaseController {
    @FXML
    private JFXButton AddBookButton;

    @FXML
    private JFXButton CatalogButton;

    @FXML
    private ImageView Exit;

    @FXML
    private JFXButton HomeButton;

    @FXML
    private JFXButton Logout;

    @FXML
    private JFXButton Menu;

    @FXML
    private JFXButton MenuClose;

    @FXML
    private TableView<Book> TableView;

    @FXML
    private TableColumn<Book, String> TitleCol;

    @FXML
    private TableColumn<Book, String> BookIDCol;

    @FXML
    private TableColumn<Book, String> AuthorCol;

    @FXML
    private TableColumn<Book, String> AuthorIDCol;

    @FXML
    private TableColumn<Book, String> GenreCol;

    @FXML
    private TableColumn<Book, String> DateCol;

    @FXML
    private TableColumn<Book, String> StatusCol;

    @FXML
    private ImageView centerImageView;

    @FXML
    private AnchorPane slider;

    private ObservableList<Book> bookList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        fetchDataFromDatabase();

        // Initialize TableView columns
        initializeTableColumns();
    }

    private void fetchDataFromDatabase() {
        // Establish a connection to the database
        try (Connection con = getConnection()) {
            // SQL query to select all columns from the book Database
            String sql = "SELECT * FROM book";

            // Create a prepared statement
            try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                // Execute the query and get the result set
                ResultSet resultSet = preparedStatement.executeQuery();

                // Clear the existing data in the list
                bookList.clear();

                // Iterate through the result set and add data to the list
                while (resultSet.next()) {
                    Book book = new Book();
                    book.setTitle(resultSet.getString("title"));
                    book.setBookid(resultSet.getString("isbn"));
                    book.setAuthor(resultSet.getString("author"));
                    book.setAuthorid(resultSet.getString("orcid"));
                    book.setGenre(resultSet.getString("genre"));
                    book.setDate(resultSet.getString("date"));

                    // Convert boolean status to "Available" or "Not Available"
                    boolean statusValue = resultSet.getBoolean("status");
                    book.setStatus(statusValue ? "Available" : "Not Available");

                    // Add the book to the list
                    bookList.add(book);
                }

                // Set the list as the data source for the TableView
                TableView.setItems(bookList);
            }
        } 
        catch (SQLException e) {
        	e.printStackTrace();
        }
    }

    private void initializeTableColumns() {
        TitleCol.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        BookIDCol.setCellValueFactory(cellData -> cellData.getValue().bookidProperty());
        AuthorCol.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        AuthorIDCol.setCellValueFactory(cellData -> cellData.getValue().authoridProperty());
        GenreCol.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
        DateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        StatusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
    }

    // Book Class
    public class Book {
        private final StringProperty title;
        private final StringProperty isbn;
        private final StringProperty author;
        private final StringProperty orcid;
        private final StringProperty genre;
        private final StringProperty date;
        private final StringProperty status;

        public Book() {
            this.title = new SimpleStringProperty();
            this.isbn = new SimpleStringProperty();
            this.author = new SimpleStringProperty();
            this.orcid = new SimpleStringProperty();
            this.genre = new SimpleStringProperty();
            this.date = new SimpleStringProperty();
            this.status = new SimpleStringProperty();
        }

        // Title
        public String getTitle() {
            return title.get();
        }

        public void setTitle(String title) {
            this.title.set(title);
        }

        public StringProperty titleProperty() {
            return title;
        }

        // Book ID
        public String getBookid() {
            return isbn.get();
        }

        public void setBookid(String bookid) {
            this.isbn.set(bookid);
        }

        public StringProperty bookidProperty() {
            return isbn;
        }

        // Author
        public String getAuthor() {
            return author.get();
        }

        public void setAuthor(String author) {
            this.author.set(author);
        }

        public StringProperty authorProperty() {
            return author;
        }

        // Author ID
        public String getAuthorid() {
            return orcid.get();
        }

        public void setAuthorid(String authorid) {
            this.orcid.set(authorid);
        }

        public StringProperty authoridProperty() {
            return orcid;
        }

        // Genre
        public String getGenre() {
            return genre.get();
        }

        public void setGenre(String genre) {
            this.genre.set(genre);
        }

        public StringProperty genreProperty() {
            return genre;
        }

        // Date
        public String getDate() {
            return date.get();
        }

        public void setDate(String date) {
            this.date.set(date);
        }

        public StringProperty dateProperty() {
            return date;
        }

        // Status
        public String getStatus() {
            return status.get();
        }

        public void setStatus(String status) {
            this.status.set(status);
        }

        public StringProperty statusProperty() {
            return status;
        }
    }

    @FXML
    private void switchToAddBook(ActionEvent event) {
        switchScene("AddBook.fxml", event);
    }

    @FXML
    private void switchToHome(ActionEvent event) {
        switchScene("DashBoard.fxml", event);
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
