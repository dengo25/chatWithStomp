package org.dengo.chat_backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

import static org.dengo.chat_backend.util.JWTFilter.secretKey;

@Component
public class JWTTokenProvider {
  
  private int expiration = 3000;
  private Key SECRET_KEY;
  
  public JWTTokenProvider() {
    this.SECRET_KEY = new SecretKeySpec(
        java.util.Base64.getDecoder().decode(JWTFilter.secretKey),
        SignatureAlgorithm.HS512.getJcaName()
    );
  }
  
  public String createToken(String email, String role) {
    Claims claims = Jwts.claims().setSubject(email);
    claims.put("role", role);
    Date now = new Date();
    String token = Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + expiration * 60 * 1000L))
        .signWith(SECRET_KEY)
        .compact();
    
    return token;
  }
}
