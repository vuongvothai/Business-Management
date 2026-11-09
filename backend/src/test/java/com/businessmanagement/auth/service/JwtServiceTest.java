package com.businessmanagement.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsonwebtoken.Claims;
import java.time.Duration;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private static final String SECRET = "01234567890123456789012345678901";

    @Test
    void issuesAndParsesTokenWithSubjectRoleAndExpiration() {
        JwtService service = new JwtService(SECRET, Duration.ofHours(8));

        String token = service.issueToken("admin", "ADMIN");
        Claims claims = service.parse(token);

        assertThat(claims.getSubject()).isEqualTo("admin");
        assertThat(claims.get("role", String.class)).isEqualTo("ADMIN");
        assertThat(claims.getExpiration()).isAfter(claims.getIssuedAt());
        assertThat(service.expiresInSeconds()).isEqualTo(8 * 60 * 60);
    }

    @Test
    void rejectsShortSigningSecret() {
        assertThatThrownBy(() -> new JwtService("too-short", Duration.ofHours(8)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least 32 characters");
    }
}
