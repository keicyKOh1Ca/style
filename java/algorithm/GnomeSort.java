package sort.gnomeesort;

import java.util.Arrays;
import java.util.Date;

/**
 * ノームソート<br>
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
		int i = 0;
		while (i < nums.length - 1)
		{
			System.out.println(i + "回目");
			if (nums[i] >= nums[i + 1])
			{
				tmp_num		= nums[i];
				nums[i]		= nums[i + 1];
				nums[i + 1]		= tmp_num;
				i--;
				if (i < 0) {
					i = 0;
				}
			} else {
				i++;
			}
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
