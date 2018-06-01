package 积累.util;

import com.google.common.collect.Lists;
import com.google.common.primitives.Booleans;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author lishupeng
 * @create 2017-05-03 下午 5:39
 **/
public class ByteUtils {
    public static byte[] getBytes(short a) {
        return new byte[]{
                (byte) (a >> 8),
                (byte) a
        };
    }

    public static byte[] getBytes(char a) {
        return new byte[]{
                (byte) (a >> 8),
                (byte) a
        };
    }

    public static byte[] getBytes(int a) {
        return new byte[]{
                (byte) (a >> 24),
                (byte) (a >> 16),
                (byte) (a >> 8),
                (byte) a
        };
    }

    public static byte[] getBytes(long a) {
        return new byte[]{
                (byte) (a >> 56),
                (byte) (a >> 48),
                (byte) (a >> 40),
                (byte) (a >> 32),
                (byte) (a >> 24),
                (byte) (a >> 16),
                (byte) (a >> 8),
                (byte) a
        };
    }

    public static byte[] getBytes(List<Boolean> array) {
        if (array != null && array.size() > 0) {
            if (array.size() % 8 != 0) {
                throw new IllegalArgumentException("长度必须为8的倍数");
            }
            int size = array.size() / 8;
            byte[] bytes = new byte[size];
            for (int j = 0; j < size; j++) {
                byte b = 0;
                //利用补码正负相加的原理
                for (int i = 0; i <= 7; i++) {
                    if (array.get(i + (j * 8))) {
                        b += (1 << (7 - i));
                    }
                }
                bytes[j] = b;
            }
            return bytes;
        }
        throw new IllegalArgumentException("array 必须非空");
    }

    public static byte[] getBytes(float data) {
        int intBits = Float.floatToIntBits(data);
        return getBytes(intBits);
    }

    public static byte[] getBytes(double data) {
        long intBits = Double.doubleToLongBits(data);
        return getBytes(intBits);
    }

    public static byte[] subBytes(byte[] bytes, int offset, int length) {
        byte[] newBytes = new byte[length];
        System.arraycopy(bytes, offset, newBytes, 0, length);
        return newBytes;
    }

    public static byte[] concat(byte[] arg1, byte[] arg2) {
        byte[] result = Arrays.copyOf(arg1, arg1.length + arg2.length);
        System.arraycopy(arg2, 0, result, arg1.length, arg2.length);
        return result;
    }

    public static byte[] concatAll(byte[]... args) {
        if (args.length == 0) {
            return new byte[]{};
        }
        if (args.length == 1) {
            return args[0];
        }
        byte[] cur = concat(args[0], args[1]);
        for (int i = 2; i < args.length; i++) {
            cur = concat(cur, args[i]);
        }
        return cur;
    }

    public static byte[][] split(byte[] source, int c) {
        if (source == null || source.length == 0) {
            return new byte[][]{};
        }
        List<byte[]> bytes = new ArrayList<byte[]>();
        int offset = 0;
        for (int i = 0; i <= source.length; i++) {
            if (i == source.length) {
                bytes.add(Arrays.copyOfRange(source, offset, i));
                break;
            }
            if (source[i] == c) {
                bytes.add(Arrays.copyOfRange(source, offset, i));
                offset = i + 1;
            }
        }
        return bytes.toArray(new byte[bytes.size()][]);
    }

    public static byte[][] mergeArrays(byte[] firstArray, byte[]... additionalArrays) {
        Assert.notNull(firstArray, "first array must not be null");
        Assert.notNull(additionalArrays, "additional arrays must not be null");

        byte[][] result = new byte[additionalArrays.length + 1][];
        result[0] = firstArray;
        System.arraycopy(additionalArrays, 0, result, 1, additionalArrays.length);
        return result;
    }

    //==============//==============//==============//==============//==============//==============//==============
    public static short getShort(byte[] bytes) {
        return (short) ((bytes[1] & 0xff) |
                (((bytes[0] & 0xff) << 8)));
    }

    public static char getChar(byte[] bytes) {
        return (char) (((char) (bytes[0] << 8)) | ((char) bytes[1]));
    }

    public static int getInt(byte[] bytes) {


        return getInt(bytes, 0);

    }

    public static int getInt(byte[] bytes, int offset) {
        //&0xff 确保只有低位参与位运算
        //因为b[i]在进行<<运算时会向上转型成int型
        //示例: b[i]=-1 二进制表示为11111111 转成int后变为 11111111111111}64个
        return (bytes[3 + offset] & 0xff) |
                ((bytes[2 + offset] & 0xff) << 8) |
                ((bytes[1 + offset] & 0xff) << 16) |
                ((bytes[0 + offset] & 0xff) << 24);
    }

    public static boolean[] getBoolean(byte[] bytes) {
        boolean[] booleans = new boolean[bytes.length * 8];
        for (int i = 0; i < bytes.length; i++) {
            byte aByte = bytes[i];
            booleans[0 + i * 8] = (aByte & 0x80) >> 7 == 1;
            booleans[1 + i * 8] = (aByte & 0x40) >> 6 == 1;
            booleans[2 + i * 8] = (aByte & 0x20) >> 5 == 1;
            booleans[3 + i * 8] = (aByte & 0x10) >> 4 == 1;
            booleans[4 + i * 8] = (aByte & 0x08) >> 3 == 1;
            booleans[5 + i * 8] = (aByte & 0x04) >> 2 == 1;
            booleans[6 + i * 8] = (aByte & 0x02) >> 1 == 1;
            booleans[7 + i * 8] = (aByte & 0x01) >> 0 == 1;
        }
        return booleans;

    }

    public static long getLong(byte[] bytes) {
        return (long) (((bytes[0] & 0xff) << 56) |
                ((bytes[1] & 0xff) << 48) |
                ((bytes[2] & 0xff) << 40) |
                ((bytes[3] & 0xff) << 32) |
                ((bytes[4] & 0xff) << 24) |
                ((bytes[5] & 0xff) << 16) |
                ((bytes[6] & 0xff) << 8) |
                ((bytes[7] & 0xff)));
    }

    public static float getFloat(byte[] bytes) {
        return Float.intBitsToFloat(getInt(bytes));
    }

    public static double getDouble(byte[] bytes) {
        long l = getLong(bytes);
        return Double.longBitsToDouble(l);
    }

    //==============//==============//==============//==============//==============//==============//==============
    public static String getString(byte[] bytes, String charsetName) {
        return new String(bytes, Charset.forName(charsetName));
    }

    public static String getString(byte[] bytes) {
        return getString(bytes, "utf-8");
    }

    public static String getString(byte[] bytes, int offset, int length) {
        return getString(subBytes(bytes, offset, length), "utf-8");
    }


    public static void main(String args[]) {
        byte[] bytes = null;

        short a = 128;
        bytes = getBytes(a);
        System.out.println(Arrays.toString(bytes));
        org.junit.Assert.assertEquals(a, getShort(bytes));
        System.out.println(getShort(bytes));

        long b = 128;
        bytes = getBytes(b);
        System.out.println(Arrays.toString(bytes));
        org.junit.Assert.assertEquals(a, getLong(bytes));

        char c = 'a';
        bytes = getBytes(c);
        System.out.println(Arrays.toString(bytes));
        org.junit.Assert.assertTrue(Objects.equals(c, getChar(bytes)));


        int d = 128;
        bytes = getBytes(d);
        System.out.println(Arrays.toString(bytes));
        org.junit.Assert.assertEquals(a, getInt(bytes));


        float e = 128f;
        bytes = getBytes(e);
        System.out.println(Arrays.toString(bytes));
        org.junit.Assert.assertTrue(Math.abs(getFloat(bytes) - e) < 0.001f);

        double f = 128D;
        bytes = getBytes(f);
        System.out.println(Arrays.toString(bytes));
        double aDouble = getDouble(bytes);
        System.out.println(aDouble);
        //org.junit.Assert.assertTrue(Math.abs(aDouble - e) < 0.001f);


        ArrayList<Boolean> g = Lists.newArrayList(true, false, false, false, false, false, false, true);
        bytes = getBytes(g);
        System.out.println(Arrays.toString(bytes));
        org.junit.Assert.assertTrue(Objects.equals(g, Booleans.asList(getBoolean(bytes))));


        byte[] h = {-1};
        boolean[] aBoolean = getBoolean(h);
        System.out.println(Arrays.toString(aBoolean));
        org.junit.Assert.assertTrue(Arrays.equals(h, getBytes(Booleans.asList(aBoolean))));


    }

}