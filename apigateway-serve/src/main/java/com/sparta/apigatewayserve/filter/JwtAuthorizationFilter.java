package com.sparta.apigatewayserve.filter;
import com.sparta.apigatewayserve.utils.JwtUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;



@Slf4j
@Component
public class JwtAuthorizationFilter extends AbstractGatewayFilterFactory<JwtAuthorizationFilter.Config> {


    private final RedisTemplate<String, String> redisTemplate;

    private final JwtUtil jwtUtil;

    public JwtAuthorizationFilter(RedisTemplate<String, String> redisTemplate, JwtUtil jwtUtil) {
        super(Config.class);
        this.redisTemplate = redisTemplate;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String accessToken = request.getHeaders().getFirst("Authorization");

            log.info("Authorization 헤더: {}", accessToken);

            if (accessToken == null || !accessToken.startsWith("Bearer ")) {
                return onError(exchange, "올바른 형식의 토큰이 아닙니다.", HttpStatus.UNAUTHORIZED);
            }

            String token = accessToken.substring(7);
            log.info("token: {}", token);



            //블랙리스트 체크
            if(token!=null && redisTemplate.hasKey(token)) {
                return onError(exchange,"로그아웃된 토큰입니다. 다시 로그인하세요.",HttpStatus.UNAUTHORIZED);
            }

            if(!jwtUtil.validateToken(token)) {
                return onError(exchange,"유효하지 않은 토큰입니다.",HttpStatus.UNAUTHORIZED);
            }

            String userId= jwtUtil.getUserIdFromJwt(token);

            if (userId != null) {
                // 헤더에 userId 추가
                ServerWebExchange modifiedExchange = exchange.mutate()
                        .request(r -> r.headers(headers -> headers.add("userId", userId)))
                        .build();

                // 필터 체인에 modifiedExchange 전달
                return chain.filter(modifiedExchange);
            }

            return onError(exchange,"잘못된 형식의 토큰입니다.",HttpStatus.UNAUTHORIZED);

        };
}

    // Mono, Flux -> Spring 5.0 에서 추가된 WebFlux = 클라이언트 요청이 들어왔을때 반환하는값 단일, 다중값을 비동기로 처리함
    private Mono<Void> onError(ServerWebExchange exchange,
                               String err,
                               HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(httpStatus);

        log.error(err);
        return response.setComplete();
    }


    @Data
    public static class Config {
        private boolean preLogger;
        private boolean postLogger;
    }
}
