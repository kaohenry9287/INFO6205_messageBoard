package application;

import database.DatabaseConnector;
import application.Article;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticleList implements StackList<Article> {
	private List<Article> stack;
	private Connection connection;

	// Constructor
	public ArticleList() {
		stack = new ArrayList<>();
		connection = DatabaseConnector.getInstance().getConnection();
	}

	@Override
	public void push(Article article) {
	    stack.add(article);
	    try {
	        DatabaseConnector.insertArticleData(connection, article.getArticleId(), article.getBoardId(), article.getAuthorId(),
	                article.getTitle(), article.getContent());
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void addArticle(Article article) {
	    stack.add(article);
	}

	@Override
	public Article pop() {
		if (isEmpty())
			return null;

		return stack.remove(stack.size() - 1);
	}

	@Override
	public Article peek() {
		if (isEmpty())
			return null;
		return stack.get(stack.size() - 1);
	}

	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	@Override
	public List<Article> sort(boolean ascending) {
		return null;

	}

	// Method to get all articles
	public List<Article> getAllArticles() {
		return new ArrayList<>(stack);
	}

	// Method to list all article data as a 2D array
	public String[][] listAsArray() {
		String[][] ArticleData = new String[stack.size()][6];
		for (int i = 0; i < stack.size(); i++) {
			Article article = stack.get(i);
			ArticleData[i][0] = article.getArticleId();
			ArticleData[i][1] = article.getAuthorId();
			ArticleData[i][2] = article.getBoardId();
			ArticleData[i][3] = article.getTitle();
			ArticleData[i][4] = article.getContent();
			ArticleData[i][5] = article.getCreateDate().toString();
			ArticleData[i][6] = Integer.toString(article.getCommentCount());
		}
		return ArticleData;
	}

	// Method to delete an article
	public void deleteArticle(String articleID) {
		try {
			DatabaseConnector.deleteArticleData(connection, articleID);
			stack.removeIf(article -> article.getArticleId().equals(articleID));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Method to update an article
	public void updateArticle(String articleID, String title, String content) {
		try {
			DatabaseConnector.updateArticleData(connection, articleID, title, content);
			for (Article article : stack) {
				if (article.getArticleId().equals(articleID)) {
					article.setTitle(title);
					article.setContent(content);
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
