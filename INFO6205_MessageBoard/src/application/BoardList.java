package application;

import database.DatabaseConnector;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoardList implements StackList<Board> {
	private List<Board> stack;
	private Connection connection;

	// Constructor
	public BoardList() {
		stack = new ArrayList<>();
		connection = DatabaseConnector.getInstance().getConnection();
	}

	@Override
	public void push(Board board) {
		stack.add(board);
		try {
			connection = DatabaseConnector.getDBConnection();
			DatabaseConnector.insertBoardData(connection, board.getBoardName()); // Insert board

		} catch (SQLException e) {
			e.printStackTrace(); // Handle exception appropriately
		}
	}

	public void addBoard(Board board) {
		stack.add(board);
	}

	@Override
	public Board pop() {
		if (isEmpty())
			return null;

		return stack.remove(stack.size() - 1);
	}

	@Override
	public Board peek() {
		if (isEmpty())
			return null;
		return stack.get(stack.size() - 1);
	}

	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	@Override
	public List<Board> sort(boolean ascending) {
		return null;
		// Implement sorting logic for boards if needed
	}

	// Method to get all boards
	public List<Board> getAllBoards() {
		return new ArrayList<>(stack);
	}

	// Method to list all board data as a 2D array
	public String[][] listAsArray() {
		String[][] boardData = new String[stack.size()][2];
		for (int i = 0; i < stack.size(); i++) {
			Board board = stack.get(i);
			boardData[i][0] = board.getBoardId();
			boardData[i][1] = board.getBoardName();
		}
		return boardData;
	}

	// Method to delete a board
	public void deleteBoard(String boardID) {
		try {
			DatabaseConnector.deleteBoardData(connection, boardID); // Delete board from database
			// Also remove from the stack list
			stack.removeIf(board -> board.getBoardId().equals(boardID));
		} catch (SQLException e) {
			e.printStackTrace(); // Handle exception appropriately
		}
	}

	// Method to update a board
	public void updateBoard(String boardID, String boardName) {
		try {
			DatabaseConnector.updateBoardData(connection, boardID, boardName); // Update board in database
			// Also update in the stack list
			for (Board board : stack) {
				if (board.getBoardId().equals(boardID)) {
					board.setBoardName(boardName);
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Handle exception appropriately
		}
	}

	// Method to get board from given BoardID
	public Board getBoardByID(String givenID) {
		for (Board board : stack) {
			if (board.getBoardId().equals(givenID)) {
				return board;
			}
		}
		return null;
	}

	// Method to get board from given BoardID
	public Board getBoardByIndex(int givenIndex) {
		int count = 0;
		for (Board board : stack) {
			if (count == givenIndex) {
				return board;
			}else {
				count++;
			}
		}
		return null;
	}

}
