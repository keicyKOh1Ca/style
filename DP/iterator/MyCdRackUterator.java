package iterator;

public class MyCdRackUterator implements MyIterator
{
	private MyCDRack myCDRack;
	private int idx;
	
	public MyCdRackUterator(MyCDRack myCdRack) {
		this.myCDRack = myCdRack;
		this.idx = 0;
	}
	@Override
	public boolean hashNext()
	{
		return idx < myCDRack.getCount() ? true : false;
	}
	
	@Override
	public Object next()
	{
		MyCD mycd = myCDRack.getMyCd(idx++);
		return mycd;
	}
}
