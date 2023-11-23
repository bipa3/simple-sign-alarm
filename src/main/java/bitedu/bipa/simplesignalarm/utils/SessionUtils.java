package bitedu.bipa.simplesignalarm.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 세션을 받아 올 때
 * 1. userId -> int userId = (int) SessionUtils.getAttribute("userId");
 * 2. userName -> String userName = (String) SessionUtils.getAttribute("userName");
 * */
public class SessionUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public static void addAttribute(String name, Object value){
        RequestContextHolder.getRequestAttributes().setAttribute(name, value, RequestAttributes.SCOPE_SESSION);
    }

    public static Object getAttribute(String name){
        return RequestContextHolder.getRequestAttributes().getAttribute(name,RequestAttributes.SCOPE_SESSION);
    }


}
