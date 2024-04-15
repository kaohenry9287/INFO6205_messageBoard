package application;

import java.util.UUID;
import java.time.LocalDate;
import java.sql.Timestamp;

public class Article {
	
	private UUID articleID;
	private String boardID;
	private String authorID;
	private String title;
	private String content;
	private Timestamp createDate;
	private int commentCount;
	
	// Constructor for new article
	public Article (String boardID, String authorID, String title, String content, Timestamp createDate) {
		this.articleID = UUID.randomUUID();
		this.boardID = boardID;
		this.authorID = authorID;
		this.title = title;
		this.content = content;
		this.createDate = createDate;
	}
	
	// Constructor for existed article
	public Article (String articleID, String boardID, String authorID, String title, String content, Timestamp createDate, int commentCount) {
		this.articleID = UUID.fromString(articleID);
		this.boardID = boardID;
		this.authorID = authorID;
		this.title = title;
		this.content = content;
		this.createDate = createDate;
		this.commentCount = commentCount;
	}
	
	public String getArticleId() {
		return this.articleID.toString();
	}

	public String getAuthorId() {
		return this.authorID;
	}
	
	public String getBoardId() {
		return this.boardID;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String newTitle) {
		this.title = newTitle;
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
	
	public int getCommentCount() {
		return this.commentCount;
	}

}
