package com.cy.store.controller;

import com.cy.store.entity.User;
import com.cy.store.form.UserForm;
import com.cy.store.service.UserService;
import com.cy.store.util.JsonResult;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@Controller
//@RestController //=@Controller+@ResponseBody
@Controller
@RequestMapping("/users")
@SessionAttributes("userForm")
@Slf4j
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    /**
     * アカウント登録
     *
     * @param user 　ユーザ情報
     * @return 登録結果
     */
    @RequestMapping("reg")
    @ResponseBody
    public JsonResult<Void> reg(User user) {
        // サービス処理を呼び出す
        userService.reg(user);
        return new JsonResult<>(OK);

    }

    /**
     * ログイン処理
     *
     * @param username 　ユーザ名
     * @param password 　パスワード
     * @param session  セッションオブジェクト
     * @return ログイン結果
     */
    @RequestMapping(value = "login")
    @ResponseBody
    public JsonResult<User> login(String username, String password, HttpSession session) {
        User user = userService.login(username, password);
        session.setAttribute("uid", user.getUid());
        session.setAttribute("username", user.getUsername());

        System.out.println(getuidFromSession(session));
        System.out.println(getUsernameFromSession(session));
        return new JsonResult<>(OK, user);
    }

    /**
     * パスワードを更新する
     *
     * @param oldPassword 元パスワード
     * @param newPassword 新しいパスワード
     * @param session     セッション情報
     * @return 更新結果
     */
    @RequestMapping("change_password")
    @ResponseBody
    public JsonResult<Void> changePassword(String oldPassword,
                                           String newPassword,
                                           HttpSession session) {

        // セッションからユーザIDとユーザ名を取得する
        Integer uid = getuidFromSession(session);
        String username = getUsernameFromSession(session);
        // パスワードを更新する
        userService.changePassword(uid,
                username,
                oldPassword,
                newPassword);
        return new JsonResult<Void>(OK);
    }

    /**
     * ユーザIDによりユーザ情報を取得
     *
     * @param session セッション情報
     * @return 取得したユーザ情報
     */
    @RequestMapping(value = "update_info", method = RequestMethod.GET)
    public String getByUid(UserForm userForm, BindingResult result, Model model, HttpSession session) {
        Integer uid = getuidFromSession(session);
        User user = userService.getByUid(uid);

        copyPropertiesIgnoreNull(user, userForm);
        model.addAttribute("userForm", userForm);
       log.info("");
        return "userdata";
    }


    /**
     * ユーザ情報を更新する
     *
     * @param user    ユーザ情報
     * @param session セッション情報
     * @return 更新結果
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String updateInfo(@Validated UserForm userForm,
                             BindingResult result,
                             Model model,
                             User user,
                             HttpSession session) {
        if (result.hasErrors()) {
            List<String> errorList = new ArrayList<String>();
            for (ObjectError error : result.getAllErrors()) {
                errorList.add(error.getDefaultMessage());
            }
            model.addAttribute("errorList", errorList);

            return "userdata";
        }

        Integer uid = getuidFromSession(session);
        String username = getUsernameFromSession(session);
        userService.updateInfo(uid, username, user);
        return "userdata";
//        return new JsonResult<Void>(OK);
    }

    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyPropertiesIgnoreNull(Object src, Object target){
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

}
