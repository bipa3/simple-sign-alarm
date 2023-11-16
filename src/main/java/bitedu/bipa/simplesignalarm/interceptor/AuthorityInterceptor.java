    package bitedu.bipa.simplesignalarm.interceptor;

    import bitedu.bipa.simplesignalarm.mapper.AuthorityMapper;
    import bitedu.bipa.simplesignalarm.utils.SessionUtils;
    import bitedu.bipa.simplesignbackend.mapper.AuthorityMapper;
    import bitedu.bipa.simplesignbackend.model.dto.RoleRequestDTO;
    import bitedu.bipa.simplesignbackend.utils.SessionUtils;
    import bitedu.bipa.simplesignbackend.validation.CustomErrorCode;
    import bitedu.bipa.simplesignbackend.validation.RestApiException;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Component;
    import org.springframework.web.method.HandlerMethod;
    import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;

    @Component
    public class AuthorityInterceptor extends HandlerInterceptorAdapter {
        @Autowired
        private AuthorityMapper authorityMapper;

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

            int authorityCode = (SessionUtils.getAttribute("authorityCode") != null) ? (int) SessionUtils.getAttribute("authorityCode") : 3;
            System.out.println("url:" + request.getRequestURI() + "  authorityCode:" + authorityCode);

            return true;
        }
    }
