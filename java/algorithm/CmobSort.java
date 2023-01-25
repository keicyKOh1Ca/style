public class CmobSort {

	private final double GAP = 1.3; // ランダムリストの並び替えでの検証結果で一番効率がよい縮小係数
	public static void main(String[] args) {
		// 適当な配列要素数
		final int ARRAY_EL_CNT = 20;
		int[] num = new int[ARRAY_EL_CNT];
		int count;
		
		count = 0;
		while(count < ARRAY_EL_CNT)
		{
			num[count] = new Random().nextInt(100);
			count++;
		}
		
		CmobSort bs = new CmobSort();
		bs.cmobsort(num);
	}
	
	/**
	 * 
	 * @param num
	 */
	private void cmobsort(int[] num) {
		long startMl = System.currentTimeMillis();
		int gap;
		int tmp;
		int count;
		
		gap = num.length;
		
		tmp = 0;
		count = 1;
		boolean swapped = true;

		while (gap != 1 || swapped == true)
		{
			gap = (int) (gap / GAP);
			
			if (gap < 1) 
			{
				gap = 1;
			}
			
			swapped = false;
			
			debug(count + "回目");
			for (int i = 0; i < num.length - gap; i++)
			{
				debug(num);
				debug(num[i], num[i + gap]);
				if (num[i] > num[i + gap]) 
				{
					tmp = num[i + gap];
					num[i + gap] = num[i];
					num[i] = tmp;
					swapped = true;
				}
			}
			count++;
		}
		long endMl = System.currentTimeMillis();
		System.out.println("処理時間：" +  (endMl - startMl) + " ms");
		debug(num);
	}

	/**
	 * 
	 * @param num
	 */
	private void debug(int[] num) {
		for (int i = 0; i < num.length; i++)
		{
			System.out.print(num[i]);
			System.out.print(" | ");
		}
		System.out.println("");
	}

	/**
	 * 
	 * @param num1
	 * @param num2
	 */
	private void debug(int num1, int num2) {
		System.out.print("    ");
		System.out.print(num1);
		System.out.print(" > ");
		System.out.print(num2);
		System.out.println("");
	}

	/**
	 * 
	 * @param arg
	 */
	private void debug(String arg) {
		System.out.print(arg);
		System.out.println("");
	}
}
