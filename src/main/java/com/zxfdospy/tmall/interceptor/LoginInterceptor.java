package com.zxfdospy.tmall.interceptor;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zxfdospy.tmall.util.CookieUtil;
import com.zxfdospy.tmall.util.JsonUtil;
import com.zxfdospy.tmall.util.RedisPoolUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.zxfdospy.tmall.pojo.Category;
import com.zxfdospy.tmall.pojo.OrderItem;
import com.zxfdospy.tmall.pojo.User;
import com.zxfdospy.tmall.service.CategoryService;
import com.zxfdospy.tmall.service.OrderItemService;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    CategoryService categoryService;
    @Autowired
    OrderItemService orderItemService;

    /**
     * 在业务处理器处理请求之前被调用
     * 如果返回false
     * 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
     * 如果返回true
     * 执行下一个拦截器,直到所有的拦截器都执行完毕
     * 再执行被拦截的Controller
     * 然后进入拦截器链,
     * 从最后一个拦截器往回执行所有的postHandle()
     * 接着再从最后一个拦截器往回执行所有的afterCompletion()
     */
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        String contextPath = session.getServletContext().getContextPath();
        String[] noNeedAuthPage = new String[]{
                "home",
                "checkLogin",
                "register",
                "loginAjax",
                "login",
                "product",
                "category",
                "search"};

        String uri = request.getRequestURI();
        uri = StringUtils.remove(uri, contextPath);
//        System.out.println(uri);

        String method = StringUtils.substringAfterLast(uri, "/fore");
        if (!Arrays.asList(noNeedAuthPage).contains(method)) {
            User user = null;
            String usertoken = CookieUtil.readLoginToken(request, CookieUtil.COOKIE_NAME_USER);
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(usertoken)) {
                String userstr = RedisPoolUtil.get(usertoken);
                user = JsonUtil.string2Obj(userstr, User.class);
            }
//            User user = (User) session.getAttribute("user");
            if (null == user) {
                response.sendRedirect("loginPage");
                return false;
            }else {
                RedisPoolUtil.expire(usertoken,60*30);
                return true;
            }
        }
        User user = null;
        String usertoken = CookieUtil.readLoginToken(request, CookieUtil.COOKIE_NAME_USER);
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(usertoken)) {
            String userstr = RedisPoolUtil.get(usertoken);
            user = JsonUtil.string2Obj(userstr, User.class);
        }
        if(user!=null){
            RedisPoolUtil.expire(usertoken,60*30);
            user.setPassword("");
            session.setAttribute("user",user);
        }
        return true;

    }

    /**
     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
     * 可在modelAndView中加入数据，比如当前时间
     */

    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    /**
     * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
     * <p>
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
     */

    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }

}