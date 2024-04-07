package application;

import database.DatabaseConnector;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentList implements StackList<Comment> {
	private List<Comment> stack;
	private Connection connection;

	// Constructor
	public CommentList() {
		stack = new ArrayList<>();
		connection = DatabaseConnector.getInstance().getConnection();
	}

	@Override
	public void push(Comment comment) {
		stack.add(comment);
		try {
			DatabaseConnector.insertCommentData(connection, comment.getCommentId(), comment.getArticleId(),
					comment.getAuthorId(), comment.getContent()); // Insert comment data
		} catch (SQLException e) {
			e.printStackTrace(); // Handle exception appropriately
		}
	}

	@Override
	public Comment pop() {
		if (isEmpty())
			return null;

		return stack.remove(stack.size() - 1);
	}

	@Override
	public Comment peek() {
		if (isEmpty())
			return null;
		return stack.get(stack.size() - 1);
	}

	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	@Override
	public void sort() {
		// Implement sorting logic for comments if needed
	}

	// Method to get all comments
	public List<Comment> getAllComments() {
		return new ArrayList<>(stack);
	}

	// Method to list all comment data as a 2D array
	public String[][] listAsArray() {
		String[][] CommentData = new String[stack.size()][4];
		for (int i = 0; i < stack.size(); i++) {
			Comment comment = stack.get(i);
			CommentData[i][0] = comment.getCommentId();
			CommentData[i][1] = comment.getArticleId();
			CommentData[i][2] = comment.getAuthorId();
			CommentData[i][3] = comment.getContent();
			CommentData[i][4] = comment.getCreateDate().toString();
		}
		return CommentData;
	}

	// Method to delete a comment
	public void deleteComment(String commentID) {
		try {
			DatabaseConnector.deleteCommentData(connection, commentID); // Delete comment from database
			// Also remove from the stack list
			stack.removeIf(comment -> comment.getCommentId().equals(commentID));
		} catch (SQLException e) {
			e.printStackTrace(); // Handle exception appropriately
		}
	}

	// Method to update a comment
	public void updateComment(String commentID, String content) {
		try {
			DatabaseConnector.updateCommentData(connection, commentID, content); // Update comment in database
			// Also update in the stack list
			for (Comment comment : stack) {
				if (comment.getCommentId().equals(commentID)) {
					comment.setContent(content);
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Handle exception appropriately
		}
	}
}