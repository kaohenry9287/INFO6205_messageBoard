package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
import database.DatabaseConnector;

public class RegisterController {

	@FXML
	private TextField usernameField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private PasswordField passwordConfirmField;
	
	@FXML
	private Button registerButton;
	
	@FXML
	private Button backBtn;

	@FXML
	void registerButtonClicked(ActionEvent event) {
		String username = usernameField.getText();
		String password = passwordField.getText();
		String passwordConfirm = passwordConfirmField.getText();
		
		// You can perform validation checks here before creating the user

		// Check if username, password, and passwordconfirm are not empty
		if (username.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
			System.out.println("Username, password, confirm password cannot be empty.");
			return;
		}
		
		// Check if passwords match
	    if (!password.equals(passwordConfirm)) {
	        System.out.println("Passwords do not match.");
	        return;
	    }

	    try {
	        // Obtain database connection
	        Connection connection = DatabaseConnector.getDBConnection();

	        if (connection == null) {
	            System.out.println("Failed to establish connection to the database.");
	            return;
	        }
	        
	        // Check if the username already exists
	        if (DatabaseConnector.usernameExists(connection, username)) {
	            System.out.println("Username already exists. Please choose a different username.");
	            
	            // Close the connection and clear input
	            connection.close(); 
	            usernameField.clear();
	            passwordField.clear();
	            passwordConfirmField.clear();
	            return;
	        }
	        
	        // Insert user data
	        DatabaseConnector.Usersignup(connection, username, password);
	        
	        // Close the connection after use
	        connection.close();

	        // Navigate to the next page
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
	        Parent setupRoot = loader.load();
	        //SetupController setupController = loader.getController();
	        //setupController.initData(username, null); // Pass username or any necessary data to SetupController

	        Scene setupScene = new Scene(setupRoot);
	        Stage setupStage = new Stage();
	        setupStage.setScene(setupScene);
	        setupStage.show();

	        // Close the current stage
	        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        currentStage.close();

	        System.out.println("User registered successfully!");
	    } catch (SQLException | IOException e) {
	        e.printStackTrace();
	        System.out.println("Error registering user.");
		}

	}
	
	public void handleBack() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
		Parent root = (Parent) loader.load();
		Scene newScene = new Scene(root);
		Stage newStage = new Stage();
		newStage.setScene(newScene);
		newStage.show();
		
		// Close the current stage
        Stage currentStage = (Stage) registerButton.getScene().getWindow();
        currentStage.close();
		
	}

}
