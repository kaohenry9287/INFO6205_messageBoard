package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DatabaseConnector;

public class LoginController extends InitialData {

	@FXML
	private TextField usernameField;

	@FXML
	private TextField passwordField;

	@FXML
	private Button registerButton;
	    
	@FXML
	void registerButtonClicked(ActionEvent event) {
		try {
			// Load the Register.fxml file
			Parent registerRoot = FXMLLoader.load(getClass().getResource("Register.fxml"));

			// Create a new stage
			Stage registerStage = new Stage();

			// Set the scene with the loaded Register.fxml
			Scene registerScene = new Scene(registerRoot);
			registerStage.setScene(registerScene);

			// Show the new stage
			registerStage.show();

			// Close the current stage (optional, depending on your requirements)
			Stage currentStage = (Stage) registerButton.getScene().getWindow();
			currentStage.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void loginButtonClicked(ActionEvent event) throws IOException {
		String username = usernameField.getText();
		String password = passwordField.getText();

		// Check if username and password are not empty
		if (username.isEmpty() || password.isEmpty()) {
			System.out.println("Username and password cannot be empty.");
			return;
		}
		
		try {
	        // Obtain database connection
	        Connection connection = DatabaseConnector.getDBConnection();

	        if (connection == null) {
	            System.out.println("Failed to establish connection to the database.");
	            return;
	        }

	        // Retrieve user data from the database
	        User currentUser = DatabaseConnector.Userlogin(connection, username, password);

	        // Check if user exists and password matches
	        if (currentUser == null) {
	            System.out.println("Login failed. Incorrect username or password.");
	        } else {
	            // Retrieve all boards from the database
	            BoardList boardList = DatabaseConnector.getAllBoards(connection);
	            UnreadComment unreadComments = new UnreadComment(100000);
	        	// Load Board.fxml
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("Board.fxml"));
	            Parent boardRoot = loader.load();

	            // Pass the currentUser to the BoardController if needed
	            BoardController boardController = loader.getController();
                boardController.initData(currentUser, boardList, unreadComments);
	            boardController.setUsername(username);

	            // Create a new scene with Board.fxml
	            Scene boardScene = new Scene(boardRoot);
	            
	            // Create a new stage and set the scene
	            Stage boardStage = new Stage();
	            boardStage.setScene(boardScene);
	            boardStage.show();
	        	
	            // Close the current stage
	            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	            currentStage.close();

	            System.out.println("User logged in successfully.");
	        }

	        // Close the database connection
	        connection.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Error logging in user.");
	    }
	}
}
