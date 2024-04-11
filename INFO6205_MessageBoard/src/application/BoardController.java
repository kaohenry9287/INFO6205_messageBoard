package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
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
import javafx.scene.control.TextField;
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
	private ListView<String> boardListView;
	
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
	


	
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
	        Connection connection = DatabaseConnector.getDBConnection();
            BoardList boardList = DatabaseConnector.getAllBoards(connection);
            ObservableList<String> boardNames = FXCollections.observableArrayList();
            for (Board board : boardList.getAllBoards()) {
                boardNames.add(board.getBoardName());
            }
            boardListView.setItems(boardNames);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
