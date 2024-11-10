package com.bangtalboys.BTS_Backend.interceptor;


import com.bangtalboys.BTS_Backend.config.error.exception.ForbiddenException;
import com.bangtalboys.BTS_Backend.config.error.exception.NotFoundException;
import com.bangtalboys.BTS_Backend.member.domain.Member;
import com.bangtalboys.BTS_Backend.member.repository.MemberRepository;
import com.bangtalboys.BTS_Backend.oauth.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.http.HttpHeaders;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    /**
     * Http 요청이 들어온 경우, 가장 처음 만나는 메서드
     * 여기서 AccessToken 이 유효한지, 유효하지 않다면 RefreshToken 은 유효한지, 검증해야 함
     *
     * 1) AccessToken 이 유효한 경우 : 그냥 return true
     * 2) AccessToken 이 유효하지 않아서 RefreshToken 이 유효한지 검증했는데 유효한 경우 : 다시 AccessToken 발급해주고 그거 리턴
     * 3) AccessToken, RefreshToken 둘 다 유효하지 않은 경우
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ForbiddenException {
        // React와의 연동으로 인한 CORS 정책 판단 조건
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        // JWT 여부 확인
        String accessToken = "";
        try {
            accessToken = request.getHeader("access_token");
        } catch (NullPointerException e) {
            throw new ForbiddenException();
        }

        Long memberIdx = jwtUtil.getId(accessToken);
        Member member = memberRepository.findById(memberIdx).orElseThrow(NotFoundException::new);

        // AccessToken 이 유효한 경우
        if (!jwtUtil.isExpired(accessToken)) {
            return true;
        }
        throw new ForbiddenException();
    }
}
