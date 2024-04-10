package application;

public class InitialData {
	
	private User user;
    private BoardList boardList;

	
	public void initData(User user, BoardList boardList) {
		this.user = user;
		this.boardList = boardList;
	}
	
	public User getCurrentUser() {
		return this.user;
	}
	
	public void setCurrentUser(User user) {
		this.user = user;
	}
	
	public BoardList getBoardList() {
		return this.boardList;
	}
	
}
