package com.cas;

import org.apache.shiro.crypto.hash.ConfigurableHashService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;
/**
 * @Project casspringboot
 * @Package PACKAGE_NAME
 * @ClassName com.cas.zzTest
 * @Descripition 模拟盐加密
 * @Author able
 * @Date 2019/6/13 14:30
 * @Version 1.0
 **/
public class zzTest {

    private String staticSalt = ".";
    private String algorithmName = "MD5";
    private String encodedPassword = "123456";
    private String dynaSalt = "test";

    @Test
    public void test() throws Exception {

        ConfigurableHashService hashService = new DefaultHashService();
        hashService.setPrivateSalt(ByteSource.Util.bytes(this.staticSalt));
        hashService.setHashAlgorithmName(this.algorithmName);
        hashService.setHashIterations(2);
        HashRequest request = new HashRequest.Builder()
                .setSalt(dynaSalt)
                .setSource(encodedPassword)
                .build();
        String res =  hashService.computeHash(request).toHex();
        System.out.println(res);
    }

}
