package iterator;

public class Driver
{
	
	public static void main(String[] args)
	{
		MyCDRack myCDRack = new MyCDRack(5);
		myCDRack.setMyCd(new MyCD("Blood Sugar Sex Magik"));
		myCDRack.setMyCd(new MyCD("One Hot Minute"));
		myCDRack.setMyCd(new MyCD("Californication"));
		myCDRack.setMyCd(new MyCD("By The Way"));
		myCDRack.setMyCd(new MyCD("Stadium Arcadium"));

		MyIterator myItarator = myCDRack.iterator();
		
		while(myItarator.hashNext()) {
			System.out.println(((MyCD)myItarator.next()).getName());
		}
	}
}
