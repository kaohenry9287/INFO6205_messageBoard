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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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
    private Button nextButton;
	
	@FXML
    private Button addarticle;
	
	@FXML
    private Text topicuser;

    @FXML
    private TextArea topicarticle;

    @FXML
    private Text topic;
	
	@FXML
    private ListView<String> listView;
	private ArticleList articleList;
	private AnchorPane currentAnchorPane;
    private AnchorPane searchAnchorPane; 
    private AnchorPane articleAnchorPane; 
    private AnchorPane commentAnchorPane;
	
	public void initData(User user) {
		setCurrentUser(user);
        currentAnchorPane = searchAnchorPane;
        articleAnchorPane.setVisible(false);
        commentAnchorPane.setVisible(false);
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

    @FXML
    void search(ActionEvent event) {
        listView.getItems().clear();
        String searchWords = searchBar.getText().toLowerCase(); // Get search keywords
        List<String> searchResults = searchList(searchWords, articleList.getAllArticles());
        listView.getItems().addAll(searchResults);
    }

    private List<String> searchList(String searchWords, List<Article> listOfArticles) {
        List<String> searchResults = new ArrayList<>();

        for (Article article : listOfArticles) {
            if (article.getTitle().toLowerCase().contains(searchWords)) {
                searchResults.add(article.getTitle());
            }
        }

        return searchResults;
    }
    
    @FXML
    void addArticleButtonClicked(ActionEvent event) {
        // Open the AddArticle.fxml for users to input article details
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddArticle.fxml"));
            Parent root = (Parent) loader.load();
            AddArticleController controller = loader.getController();
            controller.initData(currentUser); // Pass the current user to the AddArticleController
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Show the Add Article window and wait for it to close
            // After the Add Article window closes, refresh the article list
            refreshArticleList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshArticleList() {
        // Update the article list view to reflect the changes
        listView.getItems().clear();
        listView.getItems().addAll(articleList.getAllArticles().stream().map(Article::getTitle).collect(Collectors.toList()));
    }

    
    public void handleListViewClick() {
        nextButton.setDisable(false); // 启用按钮
    }

    public void goToNextAnchorPane(ActionEvent event) {
        if (currentAnchorPane == searchAnchorPane) {
            currentAnchorPane.setVisible(false);
            articleAnchorPane.setVisible(true);
            currentAnchorPane = articleAnchorPane;
        }
    }
    @FXML
    void displayArticleDetails(ActionEvent event) {
        String selectedTitle = listView.getSelectionModel().getSelectedItem();
        if (selectedTitle != null) {
            for (Article article : articleList.getAllArticles()) {
                if (article.getTitle().equals(selectedTitle)) {
                    topic.setText(article.getTitle());
                    topicuser.setText(article.getAuthorId()); // Assuming authorId is the user's username
                    topicarticle.setText(article.getContent());
                    break;
                }
            }
        }
    }	
}