package stack;

public class Driver
{
	
	public static void main(String[] args)
	{
		Stack stack = new Stack();
		
		for (int i = 0; i < 21; i++)
		{
			stack.push("test" + i);
		}
		
		for (int i = 0; i < 20; i++)
		{
			System.out.println(stack.pop());
		}
		
		for (int i = 0; i < 5; i++)
		{
			stack.push("s_test" + i);
		}
		
		for (int i = 0; i < 5; i++)
		{
			System.out.println(stack.pop());
		}
	}
}
