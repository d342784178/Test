package logic.最高分是多少;

import java.util.Scanner;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-02-19
 * Time: 18:54
 */
public class Solution {

    private int      n;
    private int      m;
    private String[] grades;

    private Solution() {
        init();
    }

    private void init() {
        Scanner  scanner = new Scanner(System.in);
        String[] split   = scanner.nextLine().split(" ");
        n = Integer.valueOf(split[0]);
        m = Integer.valueOf(split[1]);
        String[] grades = scanner.nextLine().split(" ");
        if (grades.length != n) {
            System.out.println("成绩个数不正确");
            grades = scanner.nextLine().split(" ");
        } else {
            this.grades = grades;
        }

        int step = 0;
        while (step < m) {
            if (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(" ");
                if (line.length != 3) {
                    System.out.println("输入错误请重新输入");
                    continue;
                }
                String method = line[0];
                int    left   = Integer.valueOf(line[1]);
                int    right  = Integer.valueOf(line[2]);
                switch (method) {
                    case "Q":
                        if (left - 1 >= 0 && left - 1 < n && right - 1 > 0 && right - 1 < n) {
                            if (left > right) {
                                int temp = 0;
                                temp = left;
                                left = right;
                                right = temp;
                            }
                            int max = findMax(left - 1, right - 1);
                            System.out.println(max);
                            step++;
                        } else {
                            System.out.println("输入错误请重新输入");
                        }
                        break;
                    case "U":
                        if (left - 1 >= 0 && left - 1 < n) {
                            grades[left - 1] = String.valueOf(right);
                            step++;
                        } else {
                            System.out.println("输入错误请重新输入");
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private int findMax(int start, int end) {
        int max = Integer.valueOf(grades[start]);
        for (int i = start; i <= end; i++) {
            Integer integer = Integer.valueOf(grades[i]);
            if (max < integer) {
                max = integer;
            }
        }
        return max;

    }

    public static void main(String args[]) {
//        new Solution();
        System.out.println(0b111110100);
    }
}
