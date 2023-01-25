package sort.bubblesort;

import java.util.Arrays;
import java.util.Date;

/**
 * バブルソート<br>
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
		int[] nums    = { 8, 1, 2, 6, 7, 5, 3 }; // ソート対象の数値
		int   tmp_num = 0;                       // 一時保管用の変数

		println(nums);
		System.out.println("---------- start:" + new Date());
		int i = nums.length - 1;
		while (i > 0)
		{
			System.out.println(i + "回目");
			for (int j = 0; j < i; j++)
			{
				if (nums[j] >= nums[j + 1])
				{
					tmp_num = nums[j];
					nums[j] = nums[j + 1];
					nums[j + 1] = tmp_num;
				}
				println(nums);
			}
			i--;
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
