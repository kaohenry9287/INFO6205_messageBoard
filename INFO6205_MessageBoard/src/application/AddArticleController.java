package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import database.DatabaseConnector;
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
    
    private String selectedBoard;

    public void initData(User user, BoardList boardList, String selectedBoard) {
        setCurrentUser(user);
        setCurrentBoardList(boardList);
        this.selectedBoard = selectedBoard;
    }

    public void createButtonClicked(ActionEvent event) throws SQLException {
        String topic = topicfield.getText();
        String content = contentfield.getText();

        // Check if topic or content is empty
        if (topic.isEmpty() || content.isEmpty()) {
            System.out.println("Topic and content cannot be empty.");
            return;
        }

        try {
            // Check if the board list is empty
        	// ???
            BoardList boardList = getCurrentBoardList();
            if (boardList.isEmpty()) {
                System.out.println("No boards available. Please create a board first.");
                return;
            }

        	// Get boardID and create new article
            Connection connection = DatabaseConnector.getDBConnection();
    		String boardId = DatabaseConnector.getBoardIDbyBoardName(connection, selectedBoard);
            // Push the new article onto the stack
            Article newArticle = new Article(boardId, getCurrentUser().getId(), topic, content);
            ArticleList articles = DatabaseConnector.getArticlesByBoardName(connection, selectedBoard);
            System.out.println(articles.getAllArticles());
            System.out.println(newArticle.getAuthorId());
            articles.push(newArticle);

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
