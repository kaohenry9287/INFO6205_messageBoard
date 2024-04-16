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
import javafx.stage.Stage;

public class AddBoardController extends InitialData {

    @FXML
    private TextField boardnameField;
    
	@FXML
	private Button createButton;
	
	public void initData(User user, BoardList boardList) {
		setCurrentUser(user);
		setCurrentBoardList(boardList);
				
	}
	
	public void createButtonClicked(ActionEvent event) {
		String boardname = boardnameField.getText();

		// Check if boardname is not empty
		if (boardname.isEmpty()) {
			System.out.println("Board name cannot be empty.");
			return;
		}
		
	    try {
            BoardList boardList = getCurrentBoardList();
            if (boardList == null) {
                boardList = new BoardList();
              }

			// Check if the boardname already exists
            for (Board board : boardList.getAllBoards()) {
                if (board.getBoardName().equals(boardname)) {
                    System.out.println("Board name already exists. Please choose a different board name.");
                    boardnameField.clear();
                    return;
                }
	        }            	
            

	        // Push the new board onto the stack
	        Board newBoard = new Board(boardname);
	        boardList.push(newBoard);

	        // Navigate to the next page
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("Board.fxml"));
	        Parent setupRoot = loader.load();
	        BoardController boardController = loader.getController();
	        boardController.initData(getCurrentUser(), getCurrentBoardList(), getCurrentUnreadComments());
			String username = getCurrentUser().getUsername();
            boardController.setUsername(username);

	        Scene setupScene = new Scene(setupRoot);
	        Stage setupStage = new Stage();
	        setupStage.setScene(setupScene);
	        setupStage.show();

	        // Close the current stage
	        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        currentStage.close();

	        System.out.println("Board created successfully.");
	    } catch (IOException e) {
	        e.printStackTrace();
	        System.out.println("Error creating new board.");
		}
	}
	
	
	public void handleBack() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Board.fxml"));
		Parent root = (Parent) loader.load();
		BoardController controller = loader.getController();
		controller.initData(getCurrentUser(), getCurrentBoardList(), getCurrentUnreadComments());
		String username = getCurrentUser().getUsername();
		controller.setUsername(username);
		
		Scene newScene = new Scene(root);
		Stage newStage = new Stage();
		newStage.setScene(newScene);
		newStage.show();
		
		// Close the current stage
        Stage currentStage = (Stage) createButton.getScene().getWindow();
        currentStage.close();
		
	}
}