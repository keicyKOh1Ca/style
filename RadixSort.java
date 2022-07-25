package sort.radixsort;

import java.util.Arrays;

/**
 * 基数ソート<br>
 * ※同数時にも対応<br>
 * </pre>
 */
public class Sorter
{
	/**
	 * メイン<br>
	 *
	 * @param args
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args)
	{
		int[] nums = {45, 96, 24, 11, 31, 60, 72, 88}; // ソート対象の数値
		
		// 基数準備
		int radix = 0;
		String str = "";
		
		System.out.println(str.valueOf(nums[1]).substring(1, 2));
		int len = 0;
		
		for (int i = 0; i < nums.length; i++)
		{
			len = str.valueOf(nums[i]).length();
			
			if (radix < len || radix == 0)
			{
				radix = len;
			}
		}
		
		Integer[][] radixNum = new Integer[10][radix];
		//System.out.println(max);
		
		int sort_1 = 0;
		int sort_2 = 0;
		
		for (int j = 0; j < nums.length;)
		{
			
			for (int k = radix - 1; k >= 0; k--)
			{
				
				if (k > str.valueOf(nums[j]).length())
				{
					sort_1 = 0;
				}
				else
				{
					sort_1 = Integer.parseInt(str.valueOf(nums[j]).substring(k, k + 1));
				}
			}
			
			if (radixNum[sort_1][sort_2] == null)
			{
				sort_2 = 0;
			}
			else
			{
				sort_2++;
			}
			radixNum[sort_1][sort_2] = nums[j];
			j++;
		}
		println(radixNum);
	}
	
	/**
	 * ソート後の中身を出力<br>
	 */
	@SuppressWarnings("unused")
	private static void println(int[] nums)
	{
		System.out.println(Arrays.toString(nums));
	}
	
	/**
	 * ソート後の中身を出力<br>
	 */
	private static void println(Integer[][] nums)
	{
		
		for (Integer[] num : nums)
		{
			
			for (Integer n : num)
			{
				
				if (n != null)
				{
					System.out.print(n + " | ");
				}
			}
		}
	}
}
