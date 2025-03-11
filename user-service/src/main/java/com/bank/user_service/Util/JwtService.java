package com.bank.user_service.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private  final JwtPrivateKeyProvider jwtPrivateKeyProvider;
    private final JwtPublicKeyProvider jwtPublicKeyProvider;

    public JwtService( JwtPrivateKeyProvider jwtPrivateKeyProvider,
                       JwtPublicKeyProvider jwtPublicKeyProvider){
        this.jwtPrivateKeyProvider=jwtPrivateKeyProvider;
        this.jwtPublicKeyProvider=jwtPublicKeyProvider;
    }


    //extracts username from token using helper function
    public String extractUsername (String token){
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    //extract claims
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(jwtPublicKeyProvider.getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    //validity
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }



    //token generation
    public long getExpirationTime() {
        return jwtExpiration;
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    //build token
    public String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                //.claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(jwtPrivateKeyProvider.getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }


}

