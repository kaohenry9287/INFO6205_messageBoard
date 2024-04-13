package application;



public class UnreadComment implements Queue<Comment>{
	
	private Comment[] queue;
	private int front;
    private int rear;
    private int size;
    private int capacity;
	
	// Constructor
	public UnreadComment(int capacity) {
		this.queue = new Comment[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
        this.capacity = capacity;
    }
	
	@Override
	public void enqueue(Comment item) {
        if (size == capacity) {
            throw new IllegalStateException("Queue is full");
        }
        rear = (rear + 1) % capacity;
        queue[rear] = item;
        size++;
    }		

	@Override
	public Comment dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        Comment removedElement = queue[front];
        front = (front + 1) % capacity;
        size--;
        return removedElement;
	}
	
	@Override
	public Comment peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return queue[front];
	}
	
	@Override
	public boolean isEmpty() {
        return size == 0;

	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public void clear() {
        front = 0;
        rear = -1;
        size = 0;		
	}

}
