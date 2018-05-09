package jdk特性.基本使用.jna;


import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;


/**
 * Desc:
 * Author: ljdong2
 * Date: 2017-11-29
 * Time: 16:26
 */
public class JnaTest {
    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary)
                Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"),
                        CLibrary.class);

        void printf(String format, Object... args);
    }

    public static void main(String[] args) {
        CLibrary.INSTANCE.printf("Hello, World\n");
        for (int i = 0; i < args.length; i++) {
            CLibrary.INSTANCE.printf("Argument %d: %s\n", i, args[i]);
        }
    }
}
