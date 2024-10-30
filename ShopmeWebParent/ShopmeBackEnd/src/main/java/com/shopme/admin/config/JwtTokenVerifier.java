package com.shopme.admin.config;

import com.shopme.admin.constants.CommonConstant;
import com.shopme.admin.exception.BizException;
import com.shopme.admin.pojo.response.BaseResponseEnum;
import com.shopme.common.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            String authorizationHeader = request.getHeader(CommonConstant.AUTHORIZATION_HEADER);
            if (Strings.isEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
                request.setAttribute(CommonConstant.AUTH_ERROR_MESSAGE, "Missing Token!");
                filterChain.doFilter(request, response);
                return;
            }

            String token = authorizationHeader.replace("Bearer ", "");
            User userLogin = jwtUtil.validateToken(token);
            if (userLogin == null) {
                throw new BizException(BaseResponseEnum.UN_AUTHORIZE, "Invalid token");
            }
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(userLogin.getId(), null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ex) {
            log.error("Authentication failed: {} - {}", ex.getMessage(), request.getRequestURI());
            log.debug("Authentication failed: ", ex);

            request.setAttribute(CommonConstant.AUTH_ERROR_MESSAGE, ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
