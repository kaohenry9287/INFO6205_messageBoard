package database;

import application.ArticleList;
import application.Article;
import application.BoardList;
import application.Board;
import application.CommentList;
import application.UnreadComment;
import application.User;
import application.Comment;

import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;

public class DatabaseConnector {

	private static DatabaseConnector instance;
	private static Connection connection;

	static String jdbcUrl = "jdbc:mysql://localhost:3306/MessageBoard";
	static String username = "root";
	static String password = "123qweasd";

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			// Register MySQL JDBC Driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Establish connection
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			if (connection != null) {
				System.out.println("Connected to the database.");
			}
		} catch (ClassNotFoundException e) {
			System.out.println("MySQL JDBC Driver not found.");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Connection failed. Check output console.");
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
	public static Connection getConnection() {
		return connection;
	}

	public static Connection getDBConnection() {
		try {
			connection = DriverManager.getConnection(jdbcUrl, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	public static void Usersignup(Connection connection, String userName, String password) throws SQLException {
		String sqlSelect = "SELECT MAX(CAST(userID AS UNSIGNED)) FROM User";
		String sqlInsert = "INSERT INTO User (userID, userName, password) VALUES (?, ?, ?)";
		String sqlCheckUsername = "SELECT * FROM User WHERE userName = ?";
		try (PreparedStatement selectStatement = connection.prepareStatement(sqlSelect);
				PreparedStatement insertStatement = connection.prepareStatement(sqlInsert);
				PreparedStatement checkStatement = connection.prepareStatement(sqlCheckUsername)) {

			ResultSet resultSet = selectStatement.executeQuery();

			// Check if the username already exists
			checkStatement.setString(1, userName);
			ResultSet existingUserResultSet = checkStatement.executeQuery();
			if (existingUserResultSet.next()) {
				System.out.println("Username already exists. Please choose a different username.");
				return;
			}

			// Hash password and insert the new user
			User newUser = new User(userName, password);
			String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

			insertStatement.setString(1, newUser.getId().toString());
			insertStatement.setString(2, userName);
			insertStatement.setString(3, hashedPassword);
			insertStatement.executeUpdate();
		}
	}

	// Check if username exists in the database
	public static boolean usernameExists(Connection connection, String username) throws SQLException {
		String sql = "SELECT * FROM User WHERE userName = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, username);
			ResultSet resultSet = statement.executeQuery();
			// Return true if username exists, false otherwise
			return resultSet.next();
		}
	}

	// Get user data from the database based on username and password
	public static User Userlogin(Connection connection, String username, String password) throws SQLException {
		String sqlSelect = "SELECT * FROM User WHERE userName = ?";
		String sqlUpdateLoginDate = "UPDATE User SET loginDate = ? WHERE userId = ?";

		try (PreparedStatement selectStatement = connection.prepareStatement(sqlSelect);
				PreparedStatement updateStatement = connection.prepareStatement(sqlUpdateLoginDate)) {

			selectStatement.setString(1, username);
			ResultSet resultSet = selectStatement.executeQuery();

			if (resultSet.next()) {
				String hashPassword = resultSet.getString("password");
				if (BCrypt.checkpw(password, hashPassword)) {
					String userId = resultSet.getString("userId");

					// Update loginDate for the logged-in user
					Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
					updateStatement.setTimestamp(1, currentTimestamp);
					updateStatement.setString(2, userId);
					updateStatement.executeUpdate();

					return new User(userId, username, password);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}

		return null;
	}

	// User logout
	public static void Userlogout(Connection connection, String userId) throws SQLException {
		String sqlUpdateLogoutDate = "UPDATE User SET logoutDate = ? WHERE userId = ?";

		try (PreparedStatement updateStatement = connection.prepareStatement(sqlUpdateLogoutDate)) {
			// Set current timestamp as the logout date
			Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

			updateStatement.setTimestamp(1, currentTimestamp);
			updateStatement.setString(2, userId);

			// Execute the update statement
			int rowsAffected = updateStatement.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Logout successfully.");
			} else {
				System.out.println("User with userId " + userId + " not found. Logout date not updated.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	// Method to get unread comments as a queue
	public static UnreadComment getUnreadComments(Connection connection, String userId) throws SQLException {
		UnreadComment unreadComments = new UnreadComment(100000);

		// SQL query to retrieve unread comments
		String sql = "SELECT commentID, articleID, authorID, content, createDate FROM Comment "
				+ "WHERE createDate > (SELECT logoutDate FROM User WHERE userId = ?)";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, userId);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				String commentID = resultSet.getString("commentID");
				String articleID = resultSet.getString("articleID");
				String authorID = resultSet.getString("authorID");
				String content = resultSet.getString("content");
				Timestamp createDate = resultSet.getTimestamp("createDate");

				// Create Comment object and enqueue it into the queue
				Comment comment = new Comment(commentID, articleID, authorID, content, createDate);
				unreadComments.enqueue(comment);
			}
		} catch (SQLException e) {
			// Handle SQL exceptions
			e.printStackTrace();
			throw e;
		}

		return unreadComments;
	}

	// Method to retrieve a User object by userID
	public static User getUserByID(Connection connection, String userID) throws SQLException {
		String sql = "SELECT * FROM User WHERE userID = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, userID);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					String username = resultSet.getString("userName");
					String password = resultSet.getString("password");
					return new User(userID, username, password);
				}
			}
		}
		// If no user found with the given userID, return null
		return null;
	}

	// Insert Board
	public static void insertBoardData(Connection connection, String boardName) throws SQLException {
		String sqlSelect = "SELECT MAX(CAST(boardID AS UNSIGNED)) FROM Board";
		String sqlInsert = "INSERT INTO Board (boardID, boardName) VALUES (?, ?)";
		String sqlCheckBoardname = "SELECT * FROM Board WHERE boardName = ?";
		try (PreparedStatement selectStatement = connection.prepareStatement(sqlSelect);
				PreparedStatement insertStatement = connection.prepareStatement(sqlInsert);
				PreparedStatement checkStatement = connection.prepareStatement(sqlCheckBoardname)) {

			Board newBoard = new Board(boardName);

			// Insert the new board
			insertStatement.setString(1, newBoard.getBoardId().toString());
			insertStatement.setString(2, boardName);
			insertStatement.executeUpdate();
		}
	}

	// Get all boards in database
	public static BoardList getAllBoards(Connection connection) throws SQLException {
		String sql = "SELECT * FROM Board";
		BoardList boardList = new BoardList();
		try (PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				String boardId = resultSet.getString("boardId");
				String boardName = resultSet.getString("boardName");

				// Add the board to the list
				Board board = new Board(boardId, boardName);
				boardList.addBoard(board);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error retrieving boards from database.", e);
		}

		return boardList;
	}

	// Get all Articles in database
	public static ArticleList getAllArticles(Connection connection) throws SQLException {
		String sql = "SELECT * FROM Article";
		ArticleList articleList = new ArticleList();
		try (PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				String articleID = resultSet.getString("articleID");
				String boardID = resultSet.getString("boardID");
				String authorID = resultSet.getString("authorID");
				String title = resultSet.getString("title");
				String content = resultSet.getString("content");
				Timestamp createDate = resultSet.getTimestamp("createDate");
				int commentCount = resultSet.getInt("commentCount");

				// Add the board to the list
				Article article = new Article(articleID, boardID, authorID, title, content, createDate, commentCount);
				articleList.addArticle(article);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error retrieving boards from database.", e);
		}

		return articleList;
	}

	// Insert Article
	public static void insertArticleData(Connection connection, String articleID, String boardID, String authorID,
			String title, String content) throws SQLException {

		// Insert article data
		String sql = "INSERT INTO Article (articleID, boardID, authorID, title, content, createDate, commentCount) VALUES (?, ?, ?, ?, ?, NOW(), 0)";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, articleID);
			statement.setString(2, boardID);
			statement.setString(3, authorID);
			statement.setString(4, title);
			statement.setString(5, content);
			statement.executeUpdate();
		}
	}

	// Get boardID by boardName
	public static String getBoardIDbyBoardName(Connection connection, String boardName) throws SQLException {
		String sql = "SELECT boardId FROM Board WHERE boardName = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, boardName); // Set the boardName parameter
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				// Extract boardId from the result set
				String boardId = resultSet.getString("boardId");
				return boardId;
			} else {
				return null;
			}
		}
	}

	// Insert Comment
	public static void insertCommentData(Connection connection, String commentID, String articleID, String authorID,
			String content) throws SQLException {
		String sql = "INSERT INTO Comment (commentID, articleID, authorID, content, createDate) VALUES (?, ?, ?, ?, NOW())";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, commentID);
			statement.setString(2, articleID);
			statement.setString(3, authorID);
			statement.setString(4, content);

			// Execute the SQL statement
			statement.executeUpdate();

			System.out.println("Insert comment success.");
		}

	}

	// Add a method to retrieve all comments for a given article
	public static CommentList getAllCommentsForArticle(Connection connection, String articleID) throws SQLException {
		String sql = "SELECT * FROM Comment WHERE articleID = ?";
		CommentList commentList = new CommentList();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, articleID);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					String commentID = resultSet.getString("commentID");
					String authorID = resultSet.getString("authorID");
					String content = resultSet.getString("content");
					Timestamp createDate = resultSet.getTimestamp("createDate");

					Comment comment = new Comment(commentID, articleID, authorID, content, createDate);
					commentList.addComment(comment);
				}
			}
		}
		return commentList;
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

	public static ArticleList getArticlesByBoardName(Connection connection, String boardName) throws SQLException {
		ArticleList articles = new ArticleList();
		String sql = "SELECT * FROM Article WHERE boardID IN (SELECT boardID FROM Board WHERE boardName = ?)";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, boardName);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String articleID = resultSet.getString("articleID");
				String boardID = resultSet.getString("boardID");
				String authorID = resultSet.getString("authorID");
				String title = resultSet.getString("title");
				String content = resultSet.getString("content");
				Timestamp createDate = resultSet.getTimestamp("createDate");
				int commentCount = resultSet.getInt("commentCount");
				Article article = new Article(articleID, boardID, authorID, title, content, createDate, commentCount);
				articles.addArticle(article);
			}
		}
		return articles;
	}

	// Get articleList by boardID
	public static ArticleList getArticlesByBoardId(Connection connection, String boardId) throws SQLException {
		ArticleList articles = new ArticleList();
		String sql = "SELECT * FROM Article WHERE boardId IN (SELECT boardId FROM Board WHERE boardId = ?)";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, boardId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String articleID = resultSet.getString("articleID");
				String boardID = resultSet.getString("boardID");
				String authorID = resultSet.getString("authorID");
				String title = resultSet.getString("title");
				String content = resultSet.getString("content");
				Timestamp createDate = resultSet.getTimestamp("createDate");
				int commentCount = resultSet.getInt("commentCount");
				Article article = new Article(articleID, boardID, authorID, title, content, createDate, commentCount);
				articles.addArticle(article);
			}
		}
		return articles;
	}
}
