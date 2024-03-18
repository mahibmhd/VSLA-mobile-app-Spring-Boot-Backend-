package vsla.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vsla.userManager.user.Users;

import java.util.Date;
import java.util.List;

@Service
public class JwtTokenUtil {

    //TODO: update expiration time
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000 * 5L; // five week
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 30L * 24L * 60L * 60L * 1000L; // 1 month
    public static String SECRET_KEY;
    private static CustomUserDetailsService customUserDetailsService;

    public JwtTokenUtil(@Value("${JWT_SECRET_KEY}") String privateKey, CustomUserDetailsService customUserDetailsService) {
        JwtTokenUtil.SECRET_KEY = privateKey;
        JwtTokenUtil.customUserDetailsService = customUserDetailsService;
    }

    public static String generateAccessToken(UserDetails userDetails) {
        String username = userDetails.getUsername();

        customUserDetailsService.updateLastLogin(username);
        Users user = customUserDetailsService.getByUsername(username);

        List<String> role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String hasGroup = user.getGroup() != null ? "Yes" : "No";
        Long groupId = user.getGroup() != null ? user.getGroup().getGroupId() : null;

        Long orgId = user.getOrganization().getOrganizationId();

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .withClaim("role", role)
                .withClaim("has-group", hasGroup)
                .withClaim("groupId", groupId)
                .withClaim("org_id", orgId)
                .withIssuer("Vsla App")
                .sign(Algorithm.HMAC256(SECRET_KEY.getBytes()));
    }

    public static String generateRefreshToken(UserDetails user, HttpServletRequest request) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .withIssuer("Store Management App")
                .sign(Algorithm.HMAC256(SECRET_KEY.getBytes()));
    }

    public static String getSecretKey() {
        return SECRET_KEY;
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}

