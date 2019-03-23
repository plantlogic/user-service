package edu.csumb.spring19.capstone.security;

import edu.csumb.spring19.capstone.dto.auth.TokenDTO;
import edu.csumb.spring19.capstone.dto.user.UserInfoSend;
import edu.csumb.spring19.capstone.models.PLUser;
import edu.csumb.spring19.capstone.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private long validityInMilliseconds = 1 * 60 * 60 * 1000; // 1h
    @Autowired
    public UserService userRepository;

    private Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String createToken(PLUser user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("auth", new UserInfoSend(user));

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
              .setClaims(claims)
              .setIssuedAt(now)
              .setExpiration(validity)
              .signWith(secretKey)
              .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userRepository.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) throws Exception {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new Exception("Expired or invalid JWT token.");
        }
    }
}
