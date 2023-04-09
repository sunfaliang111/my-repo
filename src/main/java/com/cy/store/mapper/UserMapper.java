package com.cy.store.mapper;

import com.cy.store.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

/** 处理用户数据操作的持久层接口 */

//@Mapper
public interface UserMapper {

    /**
     * 插入用户数据
     * @param user 用户数据
     * @return 受影响的行数
     **/
    Integer insert(User user);

    /**
     * 根据用户名查询用户数据
     * @param username 用户名
     * @return 匹配的用户数据，如果没有匹配的数据，则返回null
     **/
    User findByUsername(String username);

    /**
     * パスワード更新
     * @param uid ユーザID
     * @param password 新しいパスワード
     * @param modifiedUser 更新者
     * @param modifiedTime　更新時間
     * @return
     */
    Integer updatePasswordByUid(Integer uid,
                                String password,
                                String modifiedUser,
                                Date modifiedTime);

    /**
     * ユーザIDによりユーザを取得
     * @param uid　ユーザID
     * @return
     */
    User findByUid(Integer uid);

    /**
     * ユーザ情報更新
     * @param user　ユーザ情報
     * @return 更新結果
     */
    Integer updateInfoByUid(User user);

}
