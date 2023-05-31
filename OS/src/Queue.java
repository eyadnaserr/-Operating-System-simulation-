public class Queue
{
	private int maxsize;
	private int front;
	private int rear;
	private int nItems;
	private Object [] elements;

	public Queue(int maxSize) 
	{
		this.maxsize = maxSize;
		front = 0;
		rear = -1;
		nItems = 0;
		elements = new Object[maxsize];
	}

	public void enqueue(Object x)
	{
		if(rear == maxsize - 1)
			rear = -1;

		elements[++rear] = x;
		nItems++;
	}

	public Object dequeue()
	{
		Object result = elements[front];
		front++;

		if(front == maxsize)
			front = 0;

		nItems--;
		return result;
	}

	public Object peek()
	{
		return elements[front];
	}

	public boolean isEmpty()
	{
		return (nItems == 0);
	}

	public boolean isFull()
	{
		return (nItems == maxsize);
	}

	public int size()
	{
		return nItems;
	}

	public void printQueue() {
		if(nItems == 0){
			System.out.println("The queue is empty!");
			return;
		}
		for (int i = 0;i < nItems;i++) {
			System.out.print(elements[(front + i)%maxsize] + "  ");
		}
		System.out.println();
	}

	public boolean contains(int process_ID) {
		int b = this.size();
		while(b>0) {
		if(process_ID== (int)this.peek()) {
			return true ;
		}
		Object a = this.dequeue();
		this.enqueue(a);
		b--;
	}
	return false;
}
}
