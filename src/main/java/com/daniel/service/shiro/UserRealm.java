package com.daniel.service.shiro;

import com.daniel.pojo.User;
import com.daniel.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 授权逻辑
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 当前需求仅提及身份验证，授权这里直接返回一个空的 SimpleAuthorizationInfo
        return new SimpleAuthorizationInfo();
    }

    /**
     * 认证逻辑
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取用户输入的用户名（在本项目中为 studentid 学号）
        String studentid = (String) token.getPrincipal();

        // 通过学号查询数据库中的用户对象
        User user = userService.getByStudentid(studentid);

        // 如果用户不存在，抛出未知账号异常
        if (user == null) {
            throw new UnknownAccountException("账号不存在！");
        }

        // 获取数据库中保存的明文密码
        String passwordInDb = user.getPassword();

        // 直接将明文密码放入 SimpleAuthenticationInfo，Shiro 底层会自动与 token 中的密码进行比对
        // getName() 返回当前 Realm 的名字
        return new SimpleAuthenticationInfo(
                user.getStudentid(), // principal: 认证的主体，这里是学号
                passwordInDb,        // credentials: 数据库中的明文密码
                getName()            // realm name
        );
    }
}
