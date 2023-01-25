package sort.selectionsort;

import java.util.Arrays;
import java.util.Date;

/**
 * 選択ソート<br>
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
		int[] nums    = { 8, 2, 6, 7, 5, 1, 3 }; // ソート対象の数値
		int   tmp_num = 0;                       // 一時保管用の変数

		println(nums);
		System.out.println("---------- start:" + new Date());
		for (int i = 0; i < nums.length; i++)
		{
			int min = i;
			System.out.println(i + "回目");
			for (int j = i + 1; j < nums.length; j++)
			{
				if (nums[min] >= nums[j])
				{
					min = j;
				}
			}
			tmp_num		= nums[i];
			nums[i]		= nums[min];
			nums[min]	= tmp_num;
			println(nums);
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
