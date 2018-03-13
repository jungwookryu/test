//package com.ht.connected.home.backend.config.security;
//
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
//import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
//
//public class HtLogoutSuccessHandler  extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {
//
//    public HtLogoutSuccessHandler() {
//        super();
//    }
//
//    // API
//
//    @Override
//    public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {
//        final String refererUrl = request.getHeader("Referer");
//        request.getSession().invalidate();
//        System.out.println(refererUrl);
//
//        super.onLogoutSuccess(request, response, authentication);
//    }
//}
