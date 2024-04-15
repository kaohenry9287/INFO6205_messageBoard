package application;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import database.DatabaseConnector;

public class Board {

	private UUID boardId;
	private String boardName;
	private ArticleList articleList;
	private Connection connection;

	// Constructor for new board
	public Board(String boardName) {
		this.boardId = UUID.randomUUID();
		this.boardName = boardName;
		this.articleList = new ArticleList(); // Initialize articleList
	}

	// Constructor for existed board
	public Board(String boardId, String boardName) throws SQLException {
		this.boardId = UUID.fromString(boardId);
		this.boardName = boardName;
		this.connection = DatabaseConnector.getInstance().getConnection();
		this.articleList = DatabaseConnector.getAllArticles(connection); // Initialize articleList
	}

	public String getBoardId() {
		return this.boardId.toString();
	}

	public String getBoardName() {
		return this.boardName;
	}

	public void setBoardName(String newBoardName) {
		this.boardName = newBoardName;
	}

	public ArticleList getArticleList() {
		return this.articleList;
	}

}