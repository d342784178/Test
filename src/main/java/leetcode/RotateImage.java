package leetcode;

/**
 * Desc: 旋转图片(矩阵)
 * Author: DLJ
 * Date: 2017-03-02
 * Time: 15:03
 */
public class RotateImage {
    //    矩阵顺时针旋转90度
    public void rotate(int[][] matrix) {
        int[][] ints = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                ints[j][matrix[i].length - 1 - i] = matrix[i][j];
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = ints[i];
        }

    }

    public static void main(String args[]) {
        int matrix[][] = {{1, 2, 3, 4}, {3, 4, 5, 6}, {7, 8, 9, 5}, {4, 3, 2, 5}};
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
        new RotateImage().rotate(matrix);
        System.out.println();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
