package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
	private Button createButton;
	
	public void initData(User user) {
		setCurrentUser(user);
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
}
