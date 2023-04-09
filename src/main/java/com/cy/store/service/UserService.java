package com.cy.store.service;

import com.cy.store.entity.User;

/** 处理用户数据的业务层接口 */
public interface UserService {
    /**
     * 用户注册
     * @param user 用户数据 */
    void reg(User user);

    /**
     * ユーザログイン
     * @param username　ユーザ名
     * @param password　パスワード
     * @return
     */
    User login(String username,String password);


    /**
     * パスワードを更新する
     * @param uid ユーザID
     * @param username ユーザ名
     * @param oldPassword　元パスワード
     * @param newPassword 新しいパスワード
     */
    void changePassword(Integer uid,
                        String username,
                        String oldPassword,
                        String newPassword);

    /**
     * ユーザIDによりユーザ情報を取得する
     * @param uid ユーザID
     * @return ユーザ情報
     */
    User getByUid(Integer uid);

    /**
     * ユーザ情報を更新する
     * @param uid ユーザID
     * @param username ユーザ名
     * @param user ユーザ情報
     */
    void updateInfo(Integer uid,String username, User user);
}
