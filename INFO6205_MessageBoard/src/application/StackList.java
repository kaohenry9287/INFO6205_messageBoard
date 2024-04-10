package application;

import java.util.List;

interface StackList<T> {
	void push(T item);

	T pop();

	T peek();

	boolean isEmpty();

	List<T> sort(boolean ascending);
}
