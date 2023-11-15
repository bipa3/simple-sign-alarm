    package bitedu.bipa.simplesignalarm.interceptor;

    import bitedu.bipa.simplesignalarm.mapper.AuthorityMapper;
    import bitedu.bipa.simplesignalarm.model.dto.RoleRequestDTO;
    import bitedu.bipa.simplesignalarm.service.RedisService;
    import bitedu.bipa.simplesignalarm.utils.SessionUtils;
    import bitedu.bipa.simplesignalarm.validation.CustomErrorCode;
    import bitedu.bipa.simplesignalarm.validation.RestApiException;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Component;
    import org.springframework.web.method.HandlerMethod;
    import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import javax.servlet.http.HttpSession;
    import java.util.Arrays;
    import java.util.Collections;
    import java.util.List;
    import java.util.stream.Collectors;

    @Component
    public class AuthorityInterceptor extends HandlerInterceptorAdapter {
        @Autowired
        private AuthorityMapper authorityMapper;

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            //int authorityCode = 1; //1은 master, 2는 부서관리자, 3은 일반사용자 필요에 따라 바꿔 사용하세요

            int authorityCode = (SessionUtils.getAttribute("authorityCode") != null) ? (int) SessionUtils.getAttribute("authorityCode") : 3;
            List<String> cookieHeaderValues = Collections.list(request.getHeaders("Cookie"))
                    .stream()
                    .flatMap(cookieHeader -> Arrays.stream(cookieHeader.split(";")))
                    .map(String::trim)
                    .collect(Collectors.toList());

            //System.out.println(cookieHeaderValues.get(0) + cookieHeaderValues.get(1) + cookieHeaderValues.get(2));
            for(String value: cookieHeaderValues) {
                if(value.contains("JSESSIONID")) {
                    String JSESSIONID = value.substring(value.indexOf("=")+1);
                    System.out.println(JSESSIONID);
                }
            }
            //SessionUtils sessionUtils = new SessionUtils();
            //System.out.println(sessionUtils.getSession(JSESSIONID));
            //System.out.println("url:" + request.getRequestURI() + "  authorityCode:" + authorityCode + " headers: " + cookieHeaderValues);


            //handler 종류 확인 -> HandlerMethod 타입인지 체크
            if(handler instanceof HandlerMethod == false){
                return true;
            }

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Authority authority = handlerMethod.getMethodAnnotation(Authority.class);

            //method에 @authority가 없으면 인증이 필요 X
            if(authority == null){
                return true;
            }

            //세션에서 orgUserId
            int userId = (int) SessionUtils.getAttribute("orgUserId");
            if(userId == 0){
                throw  new RestApiException(CustomErrorCode.UNAUTHORIZED_USER);
            }

            //DB에 해당 userId 권한 조회
            RoleRequestDTO roleRequestDTO = new RoleRequestDTO(userId, authorityCode);
            int roleCount = authorityMapper.findAuthority(roleRequestDTO);

            if(roleCount ==0) {
                throw  new RestApiException(CustomErrorCode.INACTIVE_USER);
            }
            String authorityName = authorityMapper.getAuthorityName(authorityCode);

            // 권한 제크
            for(Authority.Role role: authority.role()) {
                if(role.toString().equals(authorityName)) {
                    return true;
                }
            }

            throw  new RestApiException(CustomErrorCode.INACTIVE_USER);
        }
    }
