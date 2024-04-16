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

public class BoardController extends InitialData implements Initializable {

	@FXML
	private Text usernameText;

	@FXML
	private Button logoutButton;

	@FXML
	private Button addBoardButton;

	@FXML
	private Button viewUnreadButton;

	@FXML
	private Button commentButton;

	@FXML
	private Button commentSortButton;

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
	private Button searchArticleButton;

	@FXML
	private TextArea commentTextArea;

	@FXML
	private ListView<String> articlelistView;

	@FXML
	private ListView<String> commentListView;

	@FXML
	private AnchorPane searchAnchorPane;
	
	@FXML
	private AnchorPane commentAnchorPane;

	// Define a boolean variable to track the current sorting order
	private boolean ascendingOrder = true;

	private ArticleList articleList;

	public void initData(User user) {
		setCurrentUser(user);
		searchAnchorPane.setVisible(true);
		commentAnchorPane.setVisible(false);
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

	public void searchArticleButtonClicked(ActionEvent event) {
		if (getCurrentArticleList() == null) {
			System.out.println("Article list is null!");
			return;
		}

		articlelistView.getItems().clear();
		String searchWords = searchBar.getText().toLowerCase(); // Get search keywords
		List<Article> searchArticles = getCurrentArticleList().searchByArticleName(searchWords);

		ObservableList<String> targetArticlesTitles = FXCollections.observableArrayList();
		for (Article article : searchArticles) {
			targetArticlesTitles.add(article.getTitle());
		}
		articlelistView.setItems(targetArticlesTitles);
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

	@FXML
	public void commentSortButtonClicked(ActionEvent event) throws SQLException {
		try {
			// Get the current list of comments
			CommentList commentList = getCurrentCommentList();
			List<Comment> comments = commentList.getAllComments();

			// Toggle the sorting order
			ascendingOrder = !ascendingOrder; // Toggle the sorting order

			// Sort the comments based on the current order
			comments = commentList.sort(ascendingOrder);

			// Show sorted comments on screen
			ObservableList<String> sortedComments = FXCollections.observableArrayList();
			for (Comment comment : comments) {
				sortedComments.add(comment.getFormattedComment());
			}
			commentListView.setItems(sortedComments);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Event handler for the "Comment" button
	@FXML
	public void commentButtonClicked(ActionEvent event) {
		try {
			// Get the selected board and article
			String selectedBoardID = getCurrentBoardList()
					.getBoardByIndex(boardListView.getSelectionModel().getSelectedIndex()).getBoardId();

			// Retrieve content of the comment
			String content = commentTextArea.getText();

			// Insert the comment into the database
			Connection connection = DatabaseConnector.getDBConnection();
			String authorID = getCurrentUser().getId();

			Comment newComment = new Comment(getCurrentArticle().getArticleId(), authorID, content);
			CommentList commentList = DatabaseConnector.getAllCommentsForArticle(connection, getCurrentArticle().getArticleId());
			setCurrentCommentList(commentList);

			commentList.push(newComment);

			// Reload comments for the selected article
			loadCommentsForArticle(selectedBoardID, getCurrentArticle().getArticleId());

			// Clear the comment TextArea
			commentTextArea.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadCommentsForArticle(String boardID, String articleID) {
		try {
			Connection connection = DatabaseConnector.getDBConnection();
            BoardList boardList = DatabaseConnector.getAllBoards(connection);
            System.out.println(boardID);
			setCurrentArticleList(boardList.getBoardByID(boardID).getArticleList());

			// Get the selected article
			Article selectedArticle = getCurrentBoardList().getBoardByID(boardID).getArticleList()
					.getArticleByID(articleID);

			// Get the comments for the selected article
			CommentList commentList = DatabaseConnector.getAllCommentsForArticle(connection,
					selectedArticle.getArticleId());
			setCurrentCommentList(commentList);

			// Show comment on screen
			ObservableList<String> comments = FXCollections.observableArrayList();
			for (Comment comment : commentList.getAllComments()) {
				comments.add(comment.getFormattedComment());
			}
			commentListView.setItems(comments);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	    Connection connection = null;

	    try {
	        // Initialize articleList
	        articleList = new ArticleList();

	        // Get a database connection
	        connection = DatabaseConnector.getDBConnection();

	        // Retrieve all boards and populate boardListView
	        BoardList boardList = DatabaseConnector.getAllBoards(connection);
	        ObservableList<String> boardNames = FXCollections.observableArrayList();

	        for (Board board : boardList.getAllBoards()) {
	            boardNames.add(board.getBoardName());
	        }

	        boardListView.setItems(boardNames);

	        // Handle board selection
	        boardListView.setOnMouseClicked(event -> {
	            String selectedBoard = boardListView.getSelectionModel().getSelectedItem();

	            if (selectedBoard != null) {
	                try {
	                    // Show/hide panes
	                    searchAnchorPane.setVisible(true);
	                    commentAnchorPane.setVisible(false);

	                    // Retrieve articles by selected board name
	            		Connection thisconnection = DatabaseConnector.getDBConnection();

	                    ArticleList articles = DatabaseConnector.getArticlesByBoardName(thisconnection, selectedBoard);
	                    setCurrentArticleList(articles);

	                    // Populate articleListView with article titles
	                    ObservableList<String> articleTitles = FXCollections.observableArrayList();

	                    for (Article article : articles.getAllArticles()) {
	                        articleTitles.add(article.getTitle());
	                    }

	                    articlelistView.setItems(articleTitles);

	                    // Handle article selection
	                    articlelistView.setOnMouseClicked(articleEvent -> {
	                        String selectedArticleTitle = articlelistView.getSelectionModel().getSelectedItem();

	                        if (selectedArticleTitle != null) {
	                            Article selectedArticle = getCurrentArticleList().getArticleByTitle(selectedArticleTitle);

	                            if (selectedArticle != null) {
	                                // Display article details
	                                articletopic.setText(selectedArticle.getTitle());
	                                articlecontent.setText(selectedArticle.getContent());

	                                try {
	                                    // Get author's username based on author ID
	                                    String authorId = selectedArticle.getAuthorId();
	                                    String authorName = DatabaseConnector.getUserByID(thisconnection, authorId).getUsername();
	                                    articleauthor.setText(authorName);
	                                } catch (SQLException e) {
	                                    e.printStackTrace();
	                                }

	                                // Load comments for the selected article
	                                try {
	            	            		Connection newConnection = DatabaseConnector.getDBConnection();

										loadCommentsForArticle(DatabaseConnector.getBoardIDbyBoardName(newConnection, selectedBoard), selectedArticle.getArticleId());
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
	                            }
	                        }
	                    });

	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        });

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        // Close the database connection in the finally block
	        if (connection != null) {
	            try {
	                connection.close();
	            } catch (SQLException e) {
	                e.printStackTrace(); // Handle or log the exception appropriately
	            }
	        }
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

		String selectedTitle = articlelistView.getSelectionModel().getSelectedItem();
		if (commentAnchorPane != null) {
			searchAnchorPane.setVisible(false);
			commentAnchorPane.setVisible(true);
		} else {
			// Handle the case where articleAnchorPane is null
			System.out.println("Article anchor pane is null!");
		}
	}

}

