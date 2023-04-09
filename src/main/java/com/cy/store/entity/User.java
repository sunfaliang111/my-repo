package com.cy.store.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/** 用户数据的实体类 */
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity implements Serializable {

    private Integer uid;
    private String username;
    private String password;
    private String salt;
    private String phone;
    private String email;
    private Integer gender;
    private String avatar;
    private Integer isDelete;

}
