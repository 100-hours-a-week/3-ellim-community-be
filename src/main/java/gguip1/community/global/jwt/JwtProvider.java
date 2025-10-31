package gguip1.community.global.jwt;

import gguip1.community.global.exception.ErrorCode;
import gguip1.community.global.exception.ErrorException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {
    private final Key key = Keys.hmacShaKeyFor(
        Base64.getDecoder().decode("YWRhcHRlcnphZGFwdGVyemFkYXB0ZXJ6YWRhcHRlcnphZGFwdGVyeg==")
    );

    public String createAccessToken(Long userId) {
        long accessTtlSec = 3; // 15분 Expiration
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(Date.from(java.time.Instant.now().plusSeconds(accessTtlSec)))
                .signWith(key)
                .compact();
    }

    private Jws<Claims> validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public Long getUserId(String token) {
        try {
            log.info(token);
            Jws<Claims> claimsJws = validateToken(token);
            log.info(claimsJws.toString());
            Claims claims = claimsJws.getBody();
            log.info(String.valueOf(claims));
            log.info(claims.getSubject());
            return Long.valueOf(claims.getSubject());
        } catch (Exception e) {
            throw new ErrorException(ErrorCode.UNAUTHORIZED);
        }
    }
}
