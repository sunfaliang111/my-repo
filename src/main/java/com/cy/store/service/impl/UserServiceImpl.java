package com.cy.store.service.impl;

import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.UserService;
import com.cy.store.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.UUID;

/**
 * ユーザ情報処理サービス
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void reg(User user) {

        String username = user.getUsername();
        // 调用持久层的User findByUsername(String username)方法，
        // 根据用户名查询用户数据
        User result = userMapper.findByUsername(username);
        // 判断查询结果是否不为null
        if (result != null) {
            // 是:表示用户名已被占用，则抛出UsernameDuplicateException异常
            throw new UsernameDuplicateException("尝试注册的用户名[" + username + "]已经被占用");
        }
        // 创建当前时间对象
        Date now = new Date();
        // 补全数据:加密后的密码
        String salt = UUID.randomUUID().toString().toUpperCase();
        String md5Password = getMd5Password(user.getPassword(), salt);
        user.setPassword(md5Password);
        // 补全数据:盐值
        user.setSalt(salt);
        // 补全数据:isDelete(0)
        user.setIsDelete(0);
        // 补全数据:4项日志属性
        user.setCreatedUser(username);
        user.setCreatedTime(now);
        user.setModifiedUser(username);
        user.setModifiedTime(now);
        // 表示用户名没有被占用，则允许注册
        // 调用持久层Integer insert(User user)方法，执行注册并获取返回值(受影响的行数)
        Integer rows = userMapper.insert(user);
        // 判断受影响的行数是否不为1
        if (rows != 1) {
            // 是:插入数据时出现某种错误，则抛出InsertException异常
            throw new InsertException("ユーザを登録する際に、不明なエラーが発生しました。");
        }
    }

    /**
     * ユーザログイン
     *
     * @param username 　ユーザ名
     * @param password 　パスワード
     * @return
     */
    @Override
    public User login(String username, String password) {

        //ユーザ名により、ユーザが存在するかチェックする
        User result = userMapper.findByUsername(username);
        if (result == null) {
            throw new UserNotFoundException("該当ユーザが存在しません。");
        }

        String salt = result.getSalt();
        // 入力したパスワードを暗号化する
        String newMd5Password = getMd5Password(password, salt);
        //　暗号化したパスワードと取得したパスワードと一致するかチェックする
        if (!result.getPassword().equals(newMd5Password)) {
            throw new PasswordNotMatchException("入力したパスワードが不正です。");
        }

        // ユーザが論理削除された場合、存在しないエラーをスローする
        if (result.getIsDelete() == 1) {
            throw new UserNotFoundException("該当ユーザが存在しません。");
        }
        // 创建新的User对象
        User user = new User();
        // 将查询结果中的uid、username、avatar封装到新的user对象中
        user.setUid(result.getUid());
        user.setUsername(result.getUsername());
        user.setAvatar(result.getAvatar());
        // 返回新的user对象
        return user;
    }

    /**
     * パスワードを暗号化する
     *
     * @param password 元パスワード
     * @param salt     塩値
     * @return 暗号化されたパスワード
     */
    private String getMd5Password(String password, String salt) {
        /*
         * 加密规则:
         * 1、无视原始密码的强度
         * 2、使用UUID作为盐值，在原始密码的左右两侧拼接
         * 3、循环加密3次 */
        for (int i = 0; i < 3; i++) {
            password = DigestUtils.md5DigestAsHex((salt + password +
                    salt).getBytes()).toUpperCase();
        }
        return password;
    }

    /**
     * パスワードを更新する
     *
     * @param uid         ユーザID
     * @param username    ユーザ名
     * @param oldPassword 　元パスワード
     * @param newPassword 新しいパスワード
     */
    @Override
    public void changePassword(Integer uid,
                               String username,
                               String oldPassword,
                               String newPassword) {

        // ユーザIDによりユーザ情報を取得する
        User user = userMapper.findByUid(uid);
        // ユーザ情報取得結果がない場合
        if (user == null || user.getIsDelete() == 1) {
            throw new UserNotFoundException("該当ユーザが存在しません。");
        }
        // 元パスワードが正しいかをチェックする
        String oldMd5Pasword = getMd5Password(oldPassword, user.getSalt());
        // 入力した元パスワードが正しくない場合
        if (!oldMd5Pasword.equals(user.getPassword())) {
            throw new PasswordNotMatchException("元パスワードが正しくありません。");
        }
        // 新しいパスワードを暗号化してから更新を行う
        String newMd5Password = getMd5Password(newPassword, user.getSalt());
        Integer cnt = userMapper.updatePasswordByUid(uid, newMd5Password, username, new Date());
        if (cnt != 1) {
            throw new UpdateException("パスワードの更新に失敗しました。");
        }
    }

    /**
     * ユーザIDによりユーザ情報を取得する
     *
     * @param uid ユーザID
     * @return 取得したユーザ情報
     */
    @Override
    public User getByUid(Integer uid) {

        // ユーザIDによりユーザ情報を取得する
        User user = userMapper.findByUid(uid);

        // 取得結果がない場合
        if (user == null || user.getIsDelete() == 1) {
            throw new UserNotFoundException("該当ユーザが存在しません。");
        }

        return user;
    }

    /**
     * ユーザ情報を更新する
     *
     * @param uid      ユーザID
     * @param username ユーザ名
     * @param user     ユーザ情報
     */
    @Override
    public void updateInfo(Integer uid, String username, User user) {

        // ユーザIDによりユーザ情報を取得する
        User result = userMapper.findByUid(uid);

        // 取得結果がない場合
        if (result == null || result.getIsDelete() == 1) {
            throw new UserNotFoundException("該当ユーザが存在しません。");
        }

        user.setUid(uid);
        user.setModifiedUser(username);
        user.setModifiedTime(new Date());

        Integer cnt = userMapper.updateInfoByUid(user);
        if (cnt != 1) {
            throw new UpdateException("ユーザ情報の更新に失敗しました。");
        }
    }


}

