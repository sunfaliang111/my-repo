package com.cy.store.controller;

import com.cy.store.service.ex.*;
import com.cy.store.util.JsonResult;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.PasswordAuthentication;

/**
 * 控制器类的基类
 */
public class BaseController {

    /**
     * 処理が正常する場合
     */
    public static final int OK = 200;

    /**
     * @ExceptionHandler用于统一处理方法抛出的异常
     */
    @ExceptionHandler(ServiceException.class)
    public JsonResult<Void> handleException(Throwable e) {

        JsonResult<Void> result = new JsonResult<Void>(e);
        if (e instanceof UsernameDuplicateException) {
            result.setState(4000);
            result.setMessage("該当ユーザ名がすでに使われました。");
        } else if (e instanceof UserNotFoundException) {
            result.setState(5001);
            result.setMessage("該当ユーザが存在しません。");
        } else if (e instanceof PasswordNotMatchException) {
            result.setState(5002);
            result.setMessage("入力したパスワードが不正です。");
        } else if (e instanceof InsertException) {
            result.setState(5000);
            result.setMessage("登録するさに、不明なエラーが発生しました。");
        } else if (e instanceof UpdateException) {
            result.setState(5001);
            result.setMessage("更新に失敗しました。");
        }

        return result;
    }

    /**
     * ユーザIDをセッションから取得
     *
     * @param session 　セッションオブジェクト
     * @return ユーザID
     */
    protected final Integer getuidFromSession(HttpSession session) {
        return Integer.valueOf(session.getAttribute("uid").toString());
    }

    /**
     * ユーザ名をセッションから取得
     *
     * @param session セッションオブジェクト
     * @return ユーザ名
     */
    protected final String getUsernameFromSession(HttpSession session) {
        return session.getAttribute("username").toString();
    }

}
