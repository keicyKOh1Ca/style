package queue;

public class Queue
{
	private static final int MAX = 20;	// MAXの値をリングバッファの基準とする。
	private String[] val;
	private int first_pos = 0;
	private int end_pos = 0;
	
	public Queue()
	{
		val = new String[MAX];
		
		// 初期化
		for (int i = 0; i < MAX; i++)
		{
			val[i] = "";
		}
	}
	
	public void enQueue(String str)
	{
		
		//if ((this.end_pos + 1) % MAX != this.first_pos)
		if ((this.end_pos + 1) % MAX == this.end_pos)
		{
			return;
		}
		
		val[this.end_pos] = str;
		System.out.print("    ◆[e_pos:" + this.end_pos + "|" + "f_pos:" + this.first_pos + "]");
		
		this.end_pos = (this.end_pos + 1) % MAX;
	}
	
	public String deQueue()
	{
		String result = "";
		System.out.print("    〇[e_pos:" + this.end_pos + "|" + "f_pos:" + this.first_pos + "]");
		
		if (this.first_pos == MAX)
		{
			return "";
		}
		result = val[this.first_pos];
		val[this.first_pos] = "";
		this.first_pos = (this.first_pos + 1) % MAX;
		return result;
	}
}
