package xyz.sample;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class Four_arithmetic_operations {

	public static void main(String args[]) {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		List<String> formula = new LinkedList<String>();

		System.out.println("計算する数字の個数を入力↓");
		int loopCnt = 0;
		loopCnt = Integer.parseInt(scanner.next());

		// 数式も合わせた桁数に変換
		loopCnt = loopCnt + (loopCnt - 1);

		// インプット情報
		for (int i = 0; i < loopCnt; i++) {
			formula.add(scanner.next());
		}

		List<Integer> plastic_surgery = new LinkedList<Integer>();

		// 先頭数値取得
		int temp_num = Integer.parseInt(formula.get(0));

		// 先に積算除算を行う。
		for (int j = 1; j < loopCnt - 1; j = j + 2) {
			if (formula.get(j).equals("*")) {
				temp_num = temp_num * Integer.parseInt(formula.get(j + 1));
			} else if (formula.get(j).equals("/")) {
				temp_num = temp_num / Integer.parseInt(formula.get(j + 1));
			} else {
				plastic_surgery.add(temp_num);
				if (formula.get(j).equals("+")) {
					plastic_surgery.add(0);
				} else {
					plastic_surgery.add(1);
				}
				temp_num = Integer.parseInt(formula.get(j + 1));
			}
		}
		plastic_surgery.add(temp_num);

		int calc_pos = 0;
		int calc = 0;		// 0=+ / 1=-
		int sum = 0;
		for (Integer num : plastic_surgery) {
			System.out.print((num));
			if (calc_pos % 2 != 0) {
				calc = num;
			} else {
				if (temp_num > 0) {
					if (calc == 0) {
						sum = sum + num;
					} else {
						sum = sum - num;
					}
				} else {
					sum = num;
				}
			}
			calc_pos++;
		}
		System.out.println("計算結果：" + sum);
	}
}
