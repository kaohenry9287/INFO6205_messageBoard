package database;

import application.ArticleList;
import application.Article;
import application.BoardList;
import application.Board;
import application.CommentList;
import application.Comment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class DatabaseConnector {

	private static DatabaseConnector instance;
	private static Connection connection;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String jdbcUrl = "jdbc:mysql://localhost:3306/MessageBoard";
		String username = "root";
		String password = "henry0208";

		try {
			// Register MySQL JDBC Driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Establish connection
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			if (connection != null) {
				System.out.println("Connected to the database!");
			}
		} catch (ClassNotFoundException e) {
			System.out.println("MySQL JDBC Driver not found!");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Connection failed! Check output console.");
			e.printStackTrace();
		}

	}

	// Method to get the singleton instance
	public static synchronized DatabaseConnector getInstance() {
		if (instance == null) {
			instance = new DatabaseConnector();
		}
		return instance;
	}

	// Method to get the database connection
	public Connection getConnection() {
		return connection;
	}
	
	// Method to close the database connection
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	// Insert User
    public static void insertUserData(Connection connection, String userID, String userName, String password)
			throws SQLException {
		String sql = "INSERT INTO User (userID, userName, password) VALUES (?, ?, ?)";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, userID);
			statement.setString(2, userName);
			statement.setString(3, password);
			statement.executeUpdate();
		}
	}

	// Insert Board
	public static void insertBoardData(Connection connection, String boardID, String boardName) throws SQLException {
		String sql = "INSERT INTO Board (boardID, boardName) VALUES (?, ?)";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, boardID);
			statement.setString(2, boardName);
			statement.executeUpdate();
		}
	}

	// Insert Article
	public static void insertArticleData(Connection connection, String articleID, String boardID, String authorID,
			String title, String content) throws SQLException {
		String sql = "INSERT INTO Article (articleID, boardID, aurhorID, content, createDate, commentCount) VALUES (?, ?, ?, ?, NOW(), 0)";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, articleID);
			statement.setString(2, boardID);
			statement.setString(3, authorID);
			statement.setString(4, title);
			statement.setString(5, content);
			statement.executeUpdate();
		}
	}

	// Insert Comment
	public static void insertCommentData(Connection connection, String commentID, String articleID, String authorID,
			String content) throws SQLException {
		String sql = "INSERT INTO Comment (commentID, articleID, authorID, contnent, createDate) VALUES (?, ?, ?, ?, NOW())";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, commentID);
			statement.setString(2, articleID);
			statement.setString(3, authorID);
			statement.setString(4, content);

		}

	}

	// Update article
	public static void updateArticleData(Connection connection, String articleID, String title, String content)
			throws SQLException {
		String sql = "UPDATE Article SET title = ?, content = ? WHERE articleID = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, title);
			statement.setString(2, content);
			statement.setString(3, articleID);
			statement.executeUpdate();
		}
	}

	// Update board
	public static void updateBoardData(Connection connection, String boardID, String boardName) throws SQLException {
		String sql = "UPDATE Board SET boardName = ? WHERE boardID = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, boardName);
			statement.setString(2, boardID);
			statement.executeUpdate();
		}
	}

	// Update comment
	public static void updateCommentData(Connection connection, String commentID, String content) throws SQLException {
		String sql = "UPDATE Comment SET content = ? WHERE commentID = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, content);
			statement.setString(2, commentID);
			statement.executeUpdate();
		}
	}

	// Delete article
	public static void deleteArticleData(Connection connection, String articleID) throws SQLException {
		String sql = "DELETE FROM Article WHERE articleID = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, articleID);
			statement.executeUpdate();
		}
	}

	// Delete board
	public static void deleteBoardData(Connection connection, String boardID) throws SQLException {
		String sql = "DELETE FROM Board WHERE boardID = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, boardID);
			statement.executeUpdate();
		}
	}

	// Delete comment
	public static void deleteCommentData(Connection connection, String commentID) throws SQLException {
		String sql = "DELETE FROM Comment WHERE commentID = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, commentID);
			statement.executeUpdate();
		}
	}

}
