package util.encryp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

/**
 * Created by lxy on 16/8/2.
 */
public class MD5Util {
    public static String MD5Encode(String origin, String digest) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (digest == null || "".equals(digest)) {
                resultString = toHexString(md.digest(resultString
                        .getBytes()));
            } else {
                resultString = toHexString(md.digest(resultString
                        .getBytes(digest)));
            }
        } catch (Exception exception) {
        }
        return resultString;
    }

    public final static String MD5UpCase(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        return MD5(s, hexDigits);
    }


    public final static String MD5LowCase(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        return MD5(s, hexDigits);
    }

    private static String MD5(String s, char[] hexDigits) {
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int  j     = md.length;
            char str[] = new char[j * 2];
            int  k     = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * nio 内存映射方式
     * 小文件效率高很多
     *
     * @param file
     * @param startPosition
     * @param length
     *
     * @return
     *
     * @exception Exception
     */
    public static String getMd5ByFileNIO(File file, long startPosition, long length) throws Exception {
        String          value = null;
        FileInputStream in    = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, startPosition, length <=
                    0 ? file.length() : length);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    /**
     * io方式
     *
     * @param file
     * @param startPosition
     * @param length
     *
     * @return
     *
     * @exception Exception
     */
    public static String getMD5ByFileIO(File file, long startPosition, long length) throws Exception {
        int DEFAULT_BUFFER_SIZE = 4096;
        //如果lenth小于缓冲区默认大小 则bufferSize=length 否则 bufferSize=DEFAULT_BUFFER_SIZE
        int bufferSize = length < DEFAULT_BUFFER_SIZE && length > 0 ? Integer.valueOf(String.valueOf
                (length)) : DEFAULT_BUFFER_SIZE;
        byte[]           buf = new byte[bufferSize];
        RandomAccessFile fis = new RandomAccessFile(file, "r");
        MessageDigest    md  = MessageDigest.getInstance("MD5");
        fis.seek(startPosition);
        int read;
        while ((read = fis.read(buf)) > 0) {
            md.update(buf, 0, read);
            int buferrSize = getBuferrSize(length, read, bufferSize);
            length -= bufferSize;
            buf = new byte[buferrSize];
        }
        byte[] digest = md.digest();
        return toHexString(digest);
    }


    protected static String toHexString(byte[] b) {
        char[]        hexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuilder sb      = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * 获取buffer大小
     *
     * @param totalSize  总大小
     * @param read       当前缓冲区已读入的大小
     * @param bufferSize 缓冲区初始大小
     *
     * @return
     */
    public static int getBuferrSize(Long totalSize, int read, int bufferSize) {
        int size;
        if (totalSize > 0) {
            totalSize = totalSize - read;
            if (totalSize > bufferSize) {
                size = bufferSize;
            } else {
                size = Integer.valueOf(String.valueOf(totalSize));
            }
        } else {
            size = bufferSize;
        }
        return size;
    }

}

