package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BoardController extends InitialData {

	
    @FXML
    private Text usernameText;

	@FXML
	private Button logoutButton;
	
	@FXML
	private Button addBoardButton;
	
    @FXML
    private TextField boardnameField;
    
    @FXML
    private TextField searchBar;
    
	@FXML
	private Button createButton;
	
	@FXML
    private ListView<String> listView;
	private ArticleList articleList;
	
	public void initData(User user) {
		setCurrentUser(user);
        this.articleList = articleList;
	}
	
    public void setUsername(String username) {
        usernameText.setText(username);
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
		
	    // Close the current stage
	    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    currentStage.close();
	    
	    // Show the login screen
		Scene newScene = new Scene(root);
		Stage newStage = new Stage();
		newStage.setScene(newScene);
		newStage.show();
	}
	
	public void createButtonClicked(ActionEvent event) {
		String boardname = boardnameField.getText();

		// Check if boardname is not empty
		if (boardname.isEmpty()) {
			System.out.println("Board name cannot be empty.");
			return;
		}
		
	}
	
	
	public void handleBack() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Board.fxml"));
		Parent root = (Parent) loader.load();
		BoardController controller = loader.getController();
		Scene newScene = new Scene(root);
		Stage newStage = new Stage();
		newStage.setScene(newScene);
		newStage.show();
		
		// Close the current stage
        Stage currentStage = (Stage) createButton.getScene().getWindow();
        currentStage.close();
		
	}

	public void search(ActionEvent event) {
        String boardID = searchBar.getText(); // Assuming boardID is entered in the searchBar
        listView.getItems().clear();
        listView.getItems().addAll(searchList(boardID)); // Search and display articles for the specified board
    }

    private List<String> searchList(String boardID) {
        List<Article> articles = articleList.searchArticlesByBoard(boardID);
        List<String> searchResults = new ArrayList<>();
        for (Article article : articles) {
            searchResults.add(article.getTitle());
        }
        return searchResults;
    }
	
	
}
