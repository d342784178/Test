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
public class BB {
    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary)
                Native.loadLibrary((Platform.isWindows() ? "msvcrt" :CLibrary.class.getResource("/libIflySpkDia.so").getPath()),
                        CLibrary.class);

        int SpkDiaInitialize(String engine, Object reserved);
    }

    public static void main(String[] args) {
//        URL resource = CLibrary.class.getResource("/libIflySpkDia.so");
//        System.out.println(resource);
        int i = CLibrary.INSTANCE.SpkDiaInitialize("Hello, World\n", null);
        System.out.println(i);
    }
}
