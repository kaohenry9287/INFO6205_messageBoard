package application;

import java.io.IOException;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddArticleController extends InitialData {

    @FXML
    private TextField topicfield;

    @FXML
    private TextArea contentfield;

    @FXML
    private Button createButton;

    private ArticleList articleList;

    public void initData(User user, BoardList boardList) {
        setCurrentUser(user);
        setCurrentBoardList(boardList);
        this.articleList = new ArticleList();
    }

    public void createButtonClicked(ActionEvent event) {
        String topic = topicfield.getText();
        String content = contentfield.getText();
        LocalDate createDate = LocalDate.now(); // Assuming article creation date is current date

        // Check if topic or content is empty
        if (topic.isEmpty() || content.isEmpty()) {
            System.out.println("Topic and content cannot be empty.");
            return;
        }

        try {
            // Check if the board list is empty
            BoardList boardList = getCurrentBoardList();
            if (boardList.isEmpty()) {
                System.out.println("No boards available. Please create a board first.");
                return;
            }

            // Push the new article onto the stack
            String boardId = boardList.peek().getBoardId(); // Get the ID of the latest board
            Article newArticle = new Article(boardId, getCurrentUser().getId(), topic, content, createDate);
            articleList.push(newArticle);

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

            System.out.println("Article created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error creating new article.");
        }
    }


    @FXML
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
