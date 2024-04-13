package application;

public class InitialData {
	
	private User user;
    private BoardList boardList;
    private UnreadComment unreadComments;

	
	public void initData(User user, BoardList boardList, UnreadComment unreadComments) {
		this.user = user;
		this.boardList = boardList;
		this.unreadComments = unreadComments;
		
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
	
	public UnreadComment getCurrentUnreadComments() {
		return this.unreadComments;
	}
	
	public void setCurrentUnreadComments(UnreadComment unreadComments) {
		this.unreadComments = unreadComments;
	}
}
