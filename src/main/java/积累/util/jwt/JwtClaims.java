package 积累.util.jwt;

/**
 * 项目：kuaifu-shop-agweb
 * 包名：com.fshows.kuaifu.shop.agweb.common.jwt
 * 功能：
 * 时间：2016-10-20
 * 作者：inory
 */

public class JwtClaims {

    private String username;

    private String password;

    private Integer userId;

    private String userIdStr;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }
}
