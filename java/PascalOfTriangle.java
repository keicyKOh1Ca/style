package pascale;

public class PascalOfTriangle
{
	public static void main(String args[])
	{
		Integer[][] nums = new Integer[20][20];	// 初期値null
		nums[0][0] = 1;
		nums[1][0] = 1;
		nums[1][1] = 1;
		
		int calc_count = 0;
		int before_pos = 0;
		int j = 0;
		for (int i = 0; i < nums.length; i++)
		{
			if (i > 1) {
				calc_count = 0;
				before_pos = 0;
				// 左端
				nums[i][calc_count] = 1;
				calc_count++;
				while (i - 1 >= calc_count)
				{
					nums[i][calc_count] = nums[i - 1][before_pos] + nums[i - 1][before_pos + 1];
					before_pos++;
					calc_count++;
				}
				// 右端
				nums[i][calc_count] = 1;
			}
			j = 0;
			while (j < nums.length && nums[i][j] != null) { // 右辺と左辺を逆転するとNG（Null判定でExceptionとなる）
				System.out.print(nums[i][j] + " ");
				j++;
			}
			System.out.print("\n");
		}
	}
}
