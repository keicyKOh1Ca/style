package stack;

public class Stack
{
	private static final int MAX = 20;
	private String[] val;
	private int element = 0;
	
	public Stack()
	{
		val = new String[MAX];
		
		// 初期化
		for (int i = 0; i < MAX; i++)
		{
			val[i] = "";
		}
	}
	
	public void push(String str)
	{
		
		if (this.element == MAX - 1)
		{
			
			if (val[this.element].equals(""))
			{
				val[this.element] = str;
				System.out.println("full stack!");
			}
			else
			{
				System.out.println("full stack! not empty");
			}
		}
		else
		{
			val[this.element++] = str;
		}
	}
	
	public String pop()
	{
		String result = "";
		
		if (this.element == 0)
		{
			
			if (val[this.element].equals(""))
			{
				System.out.println("no data....");
			}
			else
			{
				result = val[this.element];
			}
		}
		else
		{
			result = val[this.element--];
		}
		return result;
	}
}
