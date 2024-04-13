package application;

import java.util.UUID;

public class User {

	private UUID userId;
	private String username;
	private String password;

	// Constructor for user register
	public User(String username, String password) {
		this.userId = UUID.randomUUID();
		this.username = username;
		this.password = password;

	}

	// Constructor for user login
	public User(String userId, String username, String password) {
		this.userId = UUID.fromString(userId);
		this.username = username;
		this.password = password;

	}
	
	public String getId() {
		return this.userId.toString();
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
}
