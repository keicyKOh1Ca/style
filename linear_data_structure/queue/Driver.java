package queue;

public class Driver
{
	
	public static void main(String[] args)
	{
		Queue queue = new Queue();
		
		for (int i = 0; i < 20; i++)
		{
			queue.enQueue("test" + i);
		}
		System.out.println("\n-----------");
		
		for (int i = 0; i < 20; i++)
		{
			System.out.println(queue.deQueue());
		}
		
		System.out.println("***********************************");
		
		for (int i = 0; i < 5; i++)
		{
			queue.enQueue("q_test" + i);
		}
		System.out.println("");
		
		for (int i = 0; i < 3; i++)
		{
			System.out.println(queue.deQueue());
		}
		System.out.println("***********************************");
		
		for (int i = 0; i < 2; i++)
		{
			queue.enQueue("q1_test" + i);
		}
		System.out.println("");
		
		for (int i = 0; i < 5; i++)
		{
			System.out.println(queue.deQueue());
		}
	}
}
