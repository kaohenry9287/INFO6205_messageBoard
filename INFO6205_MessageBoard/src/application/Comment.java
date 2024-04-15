package application;

import java.util.UUID;
import java.sql.Timestamp;
import java.time.LocalDate;

public class Comment {
	private UUID commentID;
	private String articleID;
	private String authorID;
	private String content;
	private Timestamp createDate;

	// Constructor for new comment
	public Comment(String articleID, String authorID, String content, Timestamp createDate) {
		this.commentID = UUID.randomUUID();
		this.articleID = articleID;
		this.authorID = authorID;
		this.content = content;
		this.createDate = createDate;
	}

	// Constructor for existed comment
	public Comment(String commentID, String articleID, String authorID, String content, Timestamp createDate) {
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

	public Timestamp getCreateDate() {
		return this.createDate;
	}

}
