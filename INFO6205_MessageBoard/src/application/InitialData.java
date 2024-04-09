package application;

public class InitialData {
	private User user;
	
	public void initData(User user) {
		this.user = user;
	}
	
	public User getCurrentUser() {
		return this.user;
	}
	
	public void setCurrentUser(User user) {
		this.user = user;
	}
}
