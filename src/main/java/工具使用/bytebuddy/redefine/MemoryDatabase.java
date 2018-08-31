package 工具使用.bytebuddy.redefine;

import java.util.Arrays;
import java.util.List;

public class MemoryDatabase {
    public  List<String> load(String info) {
        return Arrays.asList(info + ": foo", info + ": bar");
    }
}