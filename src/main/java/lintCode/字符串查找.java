package lintCode;

/**
 * Desc:
 * Author: DLJ
 * Date: 2017-02-19
 * Time: 21:05
 */
public class 字符串查找 {
    /**
     * Returns a index to the first occurrence of target in source,
     * or -1  if target is not part of source.
     * @param source string to be scanned.
     * @param target string containing the sequence of characters to match.
     */
    public int strStr(String source, String target) {
        if (source == null || target == null) {
            return -1;
        }
        if (target.equals("")) {
            return 0;
        }
        for (int i = 0; i < source.length(); i++) {
            int     temp = i;
            boolean ok   = false;
            while (temp - i < target.length() && temp < source.length() && source.charAt(temp) == target.charAt(temp
                    - i)) {
                if (temp++ - i == target.length() - 1) {
                    ok = true;
                    break;
                }
            }
            if (ok) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String args[]) {
        System.out.println(new 字符串查找().strStr("abcde", "e"));
    }
}
