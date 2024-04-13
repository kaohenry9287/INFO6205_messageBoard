package application;

public class InitialData {
	
	private User user;
    private BoardList boardList;
    private ArticleList articleList;
    private CommentList commentList;

	
	public void initData(User user, BoardList boardList, ArticleList articleList, CommentList commentList) {
		this.user = user;
		this.boardList = boardList;
		this.articleList = articleList;
		this.commentList = commentList;
	}
	
	public User getCurrentUser() {
		return this.user;
	}
	
	public void setCurrentUser(User user) {
		this.user = user;
	}
	
	public BoardList getCurrentBoardList() {
		return this.boardList;
	}
	
	public void setCurrentBoardList(BoardList boardList) {
		this.boardList = boardList;
	}
	
	public ArticleList getCurrentArticleList() {
		return this.articleList;
	}
	
	public void setCurrentArticleList(ArticleList articleList) {
		this.articleList = articleList;
	}
	
	public CommentList getCurrentCommentList() {
		return this.commentList;
	}
	
	public void setCurrentCommentList(CommentList commentList) {
		this.commentList = commentList;
	}
	
}
