package application;

import java.util.UUID;

import database.DatabaseConnector;

import java.sql.SQLException;
import java.time.LocalDate;


public class Comment {
	private UUID commentID;
	private String articleID;
	private String authorID;
	private String content;
	private LocalDate createDate;

	// Constructor for new comment
	public Comment(String articleID, String authorID, String content) {
		this.commentID = UUID.randomUUID();
		this.articleID = articleID;
		this.authorID = authorID;
		this.content = content;
	}

	// Constructor for existed comment
	public Comment(String commentID, String articleID, String authorID, String content, LocalDate createDate) {
		this.commentID = UUID.fromString(commentID);
		this.articleID = articleID;
		this.authorID = authorID;
		this.content = content;
		this.createDate = createDate;

	}

	public String getCommentId() {
		return this.commentID.toString();
	}

	public String getArticleId() {
		return this.articleID;
	}

	public String getAuthorId() {
		return this.authorID;
	}

	public String getContent() {
		return this.content;
	}
	
	public void setContent(String newContent) {
		this.content = newContent;
	}

	public LocalDate getCreateDate() {
		return this.createDate;
	}
	
	public String getFormattedComment() throws SQLException {
	    // Retrieve the User object based on authorID
	    User author = DatabaseConnector.getUserByID(DatabaseConnector.getInstance().getConnection(), authorID); // Assuming a method to retrieve user by ID

	    // Check if the author exists
	    if (author != null) {
	        // Use the username instead of authorID
	        StringBuilder formattedComment = new StringBuilder();
	        formattedComment.append(author.getUsername()).append("\n");
	        formattedComment.append(createDate).append("\n\n");
	        formattedComment.append(content).append("\n");
	        return formattedComment.toString();
	    } else {
	        // If author is not found, return a placeholder
	        return "Unknown Author\n" + createDate + "\n\n" + content + "\n";
	    }
	}

}
