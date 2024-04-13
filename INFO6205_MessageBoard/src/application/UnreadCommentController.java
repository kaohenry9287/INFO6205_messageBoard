package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UnreadCommentController extends InitialData{

	@FXML
	private Button nextButton;
	
    @FXML
    private Text contentText;
    
	public void initData(User user, BoardList boardList, UnreadComment unreadcomments) {
		setCurrentUser(user);
		setCurrentBoardList(boardList);
		setCurrentUnreadComments(unreadcomments);
	}
	
    public void setContent(String content) {
    	contentText.setText(content);
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
        Stage currentStage = (Stage) nextButton.getScene().getWindow();
        currentStage.close();
		
	}
	
	public void nextButtonClicked(ActionEvent event) throws IOException {
		UnreadComment unreadComments = getCurrentUnreadComments();
		if (unreadComments.isEmpty()) {
			setContent("No unread");
		} else {
			setContent(unreadComments.peek().getContent());
			unreadComments.dequeue();
		}
	}
}
