package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
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
	private Button commentButton;

	@FXML
	private ListView<String> boardListView;

	@FXML
	private TextArea commentTextArea;

	@FXML
	private ListView<String> commentListView;

	public void setUsername(String username) {
		usernameText.setText(username);
	}

	public BoardController() {
	}

	public void initData(User user, BoardList boardList) {
		setCurrentUser(user);
		setCurrentBoardList(boardList);

	}

	public void logoutButtonClicked(ActionEvent event) throws IOException {
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

		System.out.println("Logout successfully!");
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

	// Event handler for the "Comment" button
	@FXML
	public void commentButtonClicked(ActionEvent event) {
		try {
			// Get the selected board and article
			String selectedBoardID = getCurrentBoardList()
					.getBoardByIndex(boardListView.getSelectionModel().getSelectedIndex()).getBoardId();
			String tempArticleID = "96fd3370-f9bf-11ee-8d65-5f7b57ad9a0e"; // Temporary article ID

			// Retrieve content of the comment
			String content = commentTextArea.getText();

			// Insert the comment into the database
			Connection connection = DatabaseConnector.getDBConnection();
			String authorID = getCurrentUser().getId();

			Comment newComment = new Comment(tempArticleID, authorID, content);
			CommentList commentList = DatabaseConnector.getAllCommentsForArticle(connection, tempArticleID);
			commentList.push(newComment);

			// Reload comments for the selected article
			loadCommentsForArticle(selectedBoardID, tempArticleID);

			// Clear the comment TextArea
			commentTextArea.clear();
		} catch (SQLException e) {
			e.printStackTrace();
			// Handle SQL exception
		}
	}

	private void loadCommentsForArticle(String boardID, String articleID) {
		try {
			Connection connection = DatabaseConnector.getDBConnection();

			setCurrentArticleList(getCurrentBoardList().getBoardByID(boardID).getArticleList());

			// Get the selected article
			Article selectedArticle = getCurrentBoardList().getBoardByID(boardID).getArticleList()
					.getArticleByID(articleID);

			// Get the comments for the selected article
			CommentList commentList = DatabaseConnector.getAllCommentsForArticle(connection,
					selectedArticle.getArticleId());

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

		// temp articleID
		String tempArticleID = "96fd3370-f9bf-11ee-8d65-5f7b57ad9a0e";

		try {
			Connection connection = DatabaseConnector.getDBConnection();
			BoardList boardList = DatabaseConnector.getAllBoards(connection);
			ObservableList<String> boardNames = FXCollections.observableArrayList();
			for (Board board : boardList.getAllBoards()) {
				boardNames.add(board.getBoardName());
			}
			boardListView.setItems(boardNames);

			// Add listener for the boardListView to load comments for the selected article
			boardListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
				int selectedBoardIndex = boardListView.getSelectionModel().getSelectedIndex();
				String selectedBoardID = getCurrentBoardList().getBoardByIndex(selectedBoardIndex).getBoardId();

				loadCommentsForArticle(selectedBoardID, tempArticleID);
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
