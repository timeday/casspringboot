
package com.cas.authentication;


import com.cas.passwordEncode.MyEncoder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.dao.DataAccessException;
import org.apereo.cas.authentication.Credential;
import org.apereo.cas.authentication.HandlerResult;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.apereo.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.apereo.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.security.auth.login.AccountNotFoundException;
import java.security.GeneralSecurityException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @Project casspringboot
 * @Package com.cas
 * @ClassName MyUserNamePasswordAuthenticationHandler
 * @Descripition TODO
 * @Author able
 * @Date 2019/6/13 15:55
 * @Version 1.0
 **/

public  class MyUserNamePasswordAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {

    /**
     * 为了符合cacheService key的名称定义格式。
     */
    public static final String CSUFFIX_USERLOGIN = "_2ae941e8-21e5-4013-9568-4dcee975333a";

    /**
     * 根据用户名和密码查询sql语句
     */
    @Value("${user.password.query.sql}")
    private String userQuerySql;

    /**
     * 根据ukey查询sql语句
     */
//    @Value("${user.ukey.query.sql}")
//    private String uKeyQuerySql;

    /**
     * jdbc模板
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public MyUserNamePasswordAuthenticationHandler(String name, ServicesManager servicesManager, PrincipalFactory principalFactory, Integer order) {
        super(name, servicesManager, principalFactory, order);
    }

    @Override
    protected HandlerResult authenticateUsernamePasswordInternal(UsernamePasswordCredential credential, String originalPassword) throws GeneralSecurityException, PreventedException {

        if("admin".equals(credential.getUsername())){
            List<Userinfo> list = new ArrayList();
             jdbcTemplate.query(userQuerySql, new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, credential.getUsername());
                }
            }, new ResultSetExtractor() {
                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {

                    while (rs.next()) {

                        Userinfo u = new Userinfo();

                        u.setId(rs.getInt("id"));

                        u.setUsername(rs.getString("username"));

                        u.setPassword(rs.getString("password"));

                        u.setName(rs.getString("name"));
                        list.add(u);
                    }
                    return list;
                }
            });

             if(CollectionUtils.isNotEmpty(list)){

                 Userinfo o = list.get(0);

                 System.out.println(o);


             }
            //TODO 相关业务胜率
            return createHandlerResult(credential,
                    this.principalFactory.createPrincipal(credential.getUsername()),
                    new ArrayList<>(0));
        }else{
            throw new AccountNotFoundException("必须是admin用户才允许通过");
        }
    }
}
