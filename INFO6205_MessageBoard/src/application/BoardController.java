package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import database.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BoardController extends InitialData implements Initializable{

	
    @FXML
    private Text usernameText;

	@FXML
	private Button logoutButton;
	
	@FXML
	private Button addBoardButton;

	@FXML
	private Button viewUnreadButton;
	
	@FXML
	private ListView<String> boardListView;
	
	@FXML
    private TextField searchBar;
    
	@FXML
	private Button createButton;
	
	@FXML
    private Button nextButton;
	
	@FXML
    private Button addarticle;
	
	@FXML
	private Text articleauthor;

	@FXML
	private TextArea articlecontent;

	@FXML
	private Text articletopic;
	
	@FXML
    private ListView<String> articlelistView;
	
	private ArticleList articleList;
	@FXML
    private AnchorPane searchAnchorPane;
	@FXML
    private AnchorPane articleAnchorPane;
	@FXML
    private AnchorPane commentAnchorPane;
	
	public void initData(User user) {
		setCurrentUser(user);
	}
	
    public void setUsername(String username) {
        usernameText.setText(username);
    }
    
    public BoardController() {
    }

	public void initData(User user, BoardList boardList, UnreadComment unreadComments) {
		setCurrentUser(user);
		setCurrentBoardList(boardList);
		setCurrentUnreadComments(unreadComments);
	}
    
	public void logoutButtonClicked(ActionEvent event) throws IOException, SQLException {
		
        Connection connection = DatabaseConnector.getDBConnection();
        if (connection == null) {
            System.out.println("Failed to establish connection to the database.");
            return;
        }
        
        DatabaseConnector.Userlogout(connection, getCurrentUser().getId());
        
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
		Parent root = (Parent) loader.load();
		
	    // Close the current stage
	    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    currentStage.close();
	    
	    // Show the login screen
		Scene newScene = new Scene(root);
		Stage newStage = new Stage();
		newStage.setScene(newScene);
		newStage.show();
	}
	
	public void addBoardButtonClicked(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AddBoard.fxml"));
		Parent root = (Parent) loader.load();
		AddBoardController addBoardController = loader.getController();
		addBoardController.initData(getCurrentUser(), getCurrentBoardList());
		
	    // Close the current stage
	    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    currentStage.close();
	    
	    // Show the login screen
		Scene newScene = new Scene(root);
		Stage newStage = new Stage();
		newStage.setScene(newScene);
		newStage.show();
	}

	public void addArticleButtonClicked(ActionEvent event) throws IOException {
	    // Check if a board is selected
	    if (boardListView.getSelectionModel().isEmpty()) {
	        // Display an error message or handle the situation accordingly
	        System.out.println("Please select a board before adding an article.");
	        return;
	    }
	    
	    
        String selectedBoard = boardListView.getSelectionModel().getSelectedItem();
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("AddArticle.fxml"));
	    Parent root = (Parent) loader.load();

	    AddArticleController addArticleController = loader.getController();
	    addArticleController.initData(getCurrentUser(), getCurrentBoardList(), selectedBoard);

	    
	    // Close the current stage
	    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    currentStage.close();
	    
	    // Show the login screen
	    Scene newScene = new Scene(root);
	    Stage newStage = new Stage();
	    newStage.setScene(newScene);
	    newStage.show();
	}
	public void viewUnreadButtonClicked(ActionEvent event) throws IOException, SQLException {
		FXMLLoader newloader = new FXMLLoader(getClass().getResource("UnreadComment.fxml"));
		Parent root = (Parent) newloader.load();
		UnreadCommentController unreadCommentController = newloader.getController();
        Connection connection = DatabaseConnector.getDBConnection();
		UnreadComment unreadcomments = DatabaseConnector.getUnreadComments(connection, getCurrentUser().getId());
		if (unreadcomments.isEmpty()) {
			unreadCommentController.setContent("No unread");
		} else {
			unreadCommentController.setContent(unreadcomments.peek().getContent());
			unreadcomments.dequeue();
		}
		unreadCommentController.initData(getCurrentUser(), getCurrentBoardList(), unreadcomments);

	    // Close the current stage
	    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    currentStage.close();
	    
	    // Show the login screen
		Scene newScene = new Scene(root);
		Stage newStage = new Stage();
		newStage.setScene(newScene);
		newStage.show();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	    try {
	        // Initialize articleList
	        articleList = new ArticleList();
	        searchAnchorPane.setVisible(true);
	        articleAnchorPane.setVisible(false);
	        commentAnchorPane.setVisible(false);

	        Connection connection = DatabaseConnector.getDBConnection();
	        BoardList boardList = DatabaseConnector.getAllBoards(connection);
	        ObservableList<String> boardNames = FXCollections.observableArrayList();
	        for (Board board : boardList.getAllBoards()) {
	            boardNames.add(board.getBoardName());
	        }
	        boardListView.setItems(boardNames);
	        boardListView.setOnMouseClicked(event -> {
	            String selectedBoard = boardListView.getSelectionModel().getSelectedItem();
	            if (selectedBoard != null) {
	                try {
	                	searchAnchorPane.setVisible(true);
	                    articleAnchorPane.setVisible(false);
	                    commentAnchorPane.setVisible(false);
	                    ArticleList articles = DatabaseConnector.getArticlesByBoardName(connection, selectedBoard);
	                    // Check if articlelistView is not null
	                    if (articlelistView != null) {
	                        articlelistView.getItems().clear();
	                        for (Article article : articles.getAllArticles()) {
	                            articlelistView.getItems().add(article.getTitle());
	                        }
	                        // Set event handler for article selection
	                        articlelistView.setOnMouseClicked(articleEvent -> {
	                            String selectedArticleTitle = articlelistView.getSelectionModel().getSelectedItem();
	                            if (selectedArticleTitle != null) {
	                                // Iterate through the list of articles to find the selected article
	                                for (Article article : articles.getAllArticles()) {
	                                    if (article.getTitle().equals(selectedArticleTitle)) {
	                                        // Display article details
	                                        articletopic.setText(article.getTitle());
	                                        articleauthor.setText(article.getAuthorId());
	                                        articlecontent.setText(article.getContent());
	                                        break; // Exit the loop once the article is found
	                                    }
	                                }
	                            }
	                        });

	                    } else {
	                        System.out.println("articlelistView is null");
	                    }
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        });
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}



	public void search(ActionEvent event) {
	    if (articleList == null) {
	        System.out.println("Article list is null!");
	        return;
	    }
	    
	    articlelistView.getItems().clear();
	    String searchWords = searchBar.getText().toLowerCase(); // Get search keywords
	    List<String> searchResults = searchList(searchWords, articleList.getAllArticles());
	    articlelistView.getItems().addAll(searchResults);
	}

	private List<String> searchList(String searchWords, List<Article> listOfArticles) {
	    List<String> searchResults = new ArrayList<>();

	    for (Article article : listOfArticles) {
	        if (article.getTitle().toLowerCase().contains(searchWords)) { // Modify to search by topic
	            searchResults.add(article.getTitle());
	        }
	    }

	    return searchResults;
	}

	public void goToNextAnchorPane(ActionEvent event) {
        if (articleAnchorPane != null) {
            searchAnchorPane.setVisible(false);
            articleAnchorPane.setVisible(true);
        } else {
            // Handle the case where articleAnchorPane is null
            System.out.println("Article anchor pane is null!");
        }
    }
	public void displayArticleDetails(ActionEvent event) {
	    String selectedTitle = articlelistView.getSelectionModel().getSelectedItem();
	    if (selectedTitle != null) {
	        try {
	            String selectedBoard = boardListView.getSelectionModel().getSelectedItem();
	            if (selectedBoard != null) {
	                Connection connection = DatabaseConnector.getDBConnection();
	                if (connection != null) {
	                    ArticleList articles = DatabaseConnector.getArticlesByBoardName(connection, selectedBoard);
	                    for (Article article : articles.getAllArticles()) {
	                        if (article.getTitle().equals(selectedTitle)) {
	                            articletopic.setText(article.getTitle());
	                            articleauthor.setText(article.getAuthorId());
	                            articlecontent.setText(article.getContent());
	                            break;
	                        }
	                    }
	                } else {
	                    System.out.println("Failed to establish connection to the database.");
	                }
	            } else {
	                System.out.println("No board selected.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

}
