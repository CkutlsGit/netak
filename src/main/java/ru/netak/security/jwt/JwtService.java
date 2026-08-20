package ru.netak.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.netak.security.dto.TokensDto;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.token.access.life}")
    private int tokenAccessExpirationSeconds;

    @Value("${jwt.token.refresh.life}")
    private int tokenRefreshExpirationSeconds;

    public TokensDto getTokens(String email) {
        return new TokensDto(generateAccessToken(email), generateRefreshToken(email));
    }

    public TokensDto getRefreshedAccessToken(String email, String refreshToken) {
        return new TokensDto(generateAccessToken(email), refreshToken);
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey(jwtSecret))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean checkValidateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey(jwtSecret))
                    .build()
                    .parseClaimsJws(token);

            return true;
        }
        catch (JwtException | IllegalArgumentException e) {
            log.error("Jwt Exception", e);
        }

        return false;
    }

    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        try {
            Claims claims = getAllClaims(token);

            return getEmailFromToken(token)
                    .equals(userDetails.getUsername()) &&
                    !isTokenExpired(claims) &&
                    "access".equals(claims.get("type"));
        }
        catch (Exception e) {
            log.error("Validation access token failed", e);
            return false;
        }
    }

    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        try {
            Claims claims = getAllClaims(token);

            return getEmailFromToken(token)
                    .equals(userDetails.getUsername()) &&
                    !isTokenExpired(claims) &&
                    "refresh".equals(claims.get("type"));
        }
        catch (Exception e) {
            log.error("Validation refresh token failed", e);
            return false;
        }
    }

    private boolean isTokenExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey(jwtSecret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String generateAccessToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(generateExpirationDate(tokenAccessExpirationSeconds))
                .claim("type", "access")
                .signWith(getSignKey(jwtSecret))
                .compact();
    }

    private String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(generateExpirationDate(tokenRefreshExpirationSeconds))
                .claim("type", "refresh")
                .signWith(getSignKey(jwtSecret))
                .compact();
    }

    private Date generateExpirationDate(int seconds) {
        return Date.from(LocalDateTime.now()
                .plusSeconds(seconds)
                .atZone(ZoneId.systemDefault())
                .toInstant()
        );
    }

    private Key getSignKey(String jwtSecret) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
