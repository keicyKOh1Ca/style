package sort.shellsort;

import java.util.Arrays;
import java.util.Date;

/**
 * シェルソート<br>
 * </pre>
 */
public class Sorter
{
	/**
	 * メイン<br>
	 *
	 * @param args
	 */
	public static void main(String[] args)
	{
		int[] nums = {8, 1, 2, 6, 7, 5, 3, 9}; // ソート対象の数値
		int tmp_num = 0;                       // 一時保管用の変数
		
		println(nums);
		System.out.println("---------- start:" + new Date());
		
		int interval = nums.length / 2;
		int cnt = 1;
		
		for (; interval > 0;)
		{
			System.out.println(cnt + "回目");
			
			for (int k = 0; k < nums.length; k++)
			{
				int i = k;
				
				while (i >= interval && (nums[i - interval] > nums[i]))
				{
					tmp_num = nums[i];
					nums[i] = nums[i - interval];
					nums[i - interval] = tmp_num;
					println(nums);
					i--;
				}
			}
			cnt++;
			
			if (interval / 2 != 0)
			{
				interval = interval / 2;
			}
			else if (interval == 1)
			{
				interval = 0;
			}
			else
			{
				interval = 1;
			}
		}
		System.out.println("-----------------------------------------");
		println(nums);
		System.out.println("---------- end  :" + new Date());
	}
	
	/**
	 * ソート後の中身を出力<br>
	 */
	private static void println(int[] nums)
	{
		System.out.println(Arrays.toString(nums));
	}
}
