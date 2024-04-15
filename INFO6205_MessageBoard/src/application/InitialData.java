package application;

public class InitialData {

 private User user;

 private BoardList boardList;
 private Board board;

 private ArticleList articleList;
 private Article article;

 private CommentList commentList;
 private Comment comment;

 private UnreadComment unreadComments;

 public void initData(User user, BoardList boardList, Board board, ArticleList articleList, Article article,
   CommentList commentList, Comment comment, UnreadComment unreadComments) {
  this.user = user;

  this.boardList = boardList;
  this.board = board;

  this.articleList = articleList;
  this.article = article;

  this.commentList = commentList;
  this.comment = comment;

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

 public Board getCurrentBoard() {
  return board;
 }

 public void setCurrentBoard(Board board) {
  this.board = board;
 }

 public ArticleList getCurrentArticleList() {
  return articleList;
 }

 public void setCurrentArticleList(ArticleList articleList) {
  this.articleList = articleList;
 }

 public Article getCurrentArticle() {
  return article;
 }

 public void setCurrentArticle(Article article) {
  this.article = article;
 }

 public CommentList getCurrentCommentList() {
  return commentList;
 }

 public void setCurrentCommentList(CommentList commentList) {
  this.commentList = commentList;
 }

 public Comment getCurrentComment() {
  return comment;
 }

 public void setCurrentComment(Comment comment) {
  this.comment = comment;
 }
}