package sort.quicksort;

import java.util.Arrays;
import java.util.Date;

/**
 * クイックソート<br>
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
		int[] nums = {8, 1, 2, 6, 7, 5, 3}; // ソート対象の数値
		
		println(nums);
		System.out.println("---------- start:" + new Date());
		
		new Sorter().q_sort(nums, 0, nums.length - 1);
		System.out.println("-----------------------------------------");
		println(nums);
		System.out.println("---------- end  :" + new Date());
	}
	
	private void q_sort(int[] nums, int left, int right)
	{
		int left_pos = left;
		int right_pos = right;
		
		int pivot = nums[left_pos];
		
		int temp_num = 0;
		
		while (left_pos < right_pos)
		{
			
			if (nums[left_pos] < pivot)
			{
				left_pos++;
			}
			
			if (nums[right_pos] > pivot)
			{
				right_pos--;
			}
			
			if (left_pos >= right_pos)
			{
				break;
			}
			
			// swap
			temp_num = nums[left_pos];
			nums[left_pos] = nums[right_pos];
			nums[right_pos] = temp_num;
		}
		
		if (left < left_pos - 1)
		{
			q_sort(nums, left, left_pos - 1);
		}
		
		if (right > right_pos + 1)
		{
			q_sort(nums, right_pos + 1, right);
		}
	}
	
	/**
	 * ソート後の中身を出力<br>
	 */
	private static void println(int[] nums)
	{
		System.out.println(Arrays.toString(nums));
	}
}
