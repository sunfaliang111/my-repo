package com.cy.store.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    /**
     * @param request
     * @param response
     * @param handler
     * @return 戻り値はTRUEの場合、当該リクエストを許可する、Falseの場合、拒否する
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        Object obj = request.getSession().getAttribute("uid");
        if (obj == null) {
            response.sendRedirect("/web/login.html");
            return  false;
        }
        return true;
    }
}
