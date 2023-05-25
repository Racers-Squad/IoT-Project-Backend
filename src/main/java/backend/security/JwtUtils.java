package backend.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

import static io.jsonwebtoken.lang.Strings.hasText;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-time}")
    private int jwtExpirationTime;

    public String generateToken(String email) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtExpirationTime))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature! " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token! " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired! " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported! " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty! " + e.getMessage());
        }
        return false;
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        if (hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    public String restructJwtHeader(String header){
        if (hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
