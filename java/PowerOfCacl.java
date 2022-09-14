public class PowerOfCacl {

	public static void main(String[] args) {
		funcPowerOfCalc(32, 2);
		legacy(32, 2);
	}

	private static void funcPowerOfCalc(int power_of_num, long express) {
		System.out.println("start:" + System.currentTimeMillis());

		int t = 1;

		long tmp = 0;
		while (true) {
			t = t * 2;
			if (t < power_of_num) {
				express = express * express;
			} else {
				break;
			}
			tmp = power_of_num - t;
		}
		
		while(tmp > 0) {
			express = express * 2;
			tmp--;
		}

		System.out.println(express);
		System.out.println("end  :" + System.currentTimeMillis());
	}

	private static void legacy(int power_of_num, long express) {
		System.out.println("start:" + System.currentTimeMillis());

		long sum = express;
		int i = 1;
		while (i < power_of_num) {
			sum = sum * express;
			i++;
		}
		System.out.println(sum);

		System.out.println("end  :" + System.currentTimeMillis());
	}
}
