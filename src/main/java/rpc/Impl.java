package rpc;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2020-12-27
 * Time: 11:27
 */
public class Impl implements Intf {
    @Override
    public String invoke(String a, String b) {
        return a + b;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}
