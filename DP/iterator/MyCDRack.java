package iterator;

public class MyCDRack implements Aggregate
{
	
	private MyCD[] mycds;
	private int max = 0;
	
	public MyCDRack(int maxsize)
	{
		this.mycds = new MyCD[maxsize];
	}
	
	public MyCD getMyCd(int idx)
	{
		return mycds[idx];
	}
	
	public void setMyCd(MyCD mycd)
	{
		this.mycds[max++] = mycd;
	}
	
	public int getCount()
	{
		return this.max;
	}
	
	@Override
	public MyIterator iterator()
	{
		return new MyCdRackUterator(this);
	}
}
