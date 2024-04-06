package application;

import java.util.UUID;

public class Board {

	private UUID boardId;
	private String boardName;

	// Constructor for new board
	public Board(String boardName) {
		this.boardId = UUID.randomUUID();
		this.boardName = boardName;
	}

	// Constructor for existed board
	public Board(String boardId, String boardName) {
		this.boardId = UUID.fromString(boardId);
		this.boardName = boardName;
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
	
}
