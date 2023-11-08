package com.pet.healthwave.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * This class helps to work with jwt token. It generates token using user information, extracts user data
 * from the token, checks if token is valid.
 * @author askerovvvv
 */

@Service
public class JwtService {

    /**
     * this simbols need to sign our token when we generate token
     */
    private static final String SECRET_KEY = "f065f63c91e35cf6e5bdbca7df3bfea34ff06031082f55f662621c63d0e427da";

    /**
     * if we don't have additional data we just create empty Map and invoke method which is responsible to
     * generate token
     * @param userDetails user
     * @return other method that generates token
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * method to generate token using user data and some additional data if it exists
     * with expiration time(users has 24 hours)
     *
     * @param extraClaims additional data
     * @param userDetails user data
     * @return jwt token
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiredAt = new Date(System.currentTimeMillis() + 1000 * 60 * 24);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expiredAt)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * key to sign our token
     * @return key
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * from token gets all data about user
     * @param token token from request
     * @return user data
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * from token gets certain information
     * @param token token from request
     * @param claimsResolver method to get that information from claims
     * @return certain information
     * @param <T>
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     *  gets username from token
     * @param token token from request
     * @return username
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * checks if username from token and username from userDetails are same, and token has not expired
     * @param token token from request
     * @param userDetails user data
     * @return true if everything is good
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * checks token has expired or not
     * @param token token from request
     * @return true if everything is good
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * from token gets expiration time
     * @param token token from request
     * @return expiration data
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
