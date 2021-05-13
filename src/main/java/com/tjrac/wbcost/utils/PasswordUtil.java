package com.tjrac.wbcost.utils;

import com.tjrac.wbcost.configs.Const;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * @author myd
 */
public class PasswordUtil {
    private String pwdstr;

    public PasswordUtil(String pwdstr) {
        this.pwdstr = pwdstr;
    }

    public String getEncryptedPwd() {
        return Hex.encodeToString(new SimpleHash("MD5", this.pwdstr, Const.MD5_PWD_SALT).getBytes());
    }
}
