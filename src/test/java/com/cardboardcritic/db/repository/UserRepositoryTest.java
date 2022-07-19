package com.cardboardcritic.db.repository;

import io.quarkus.elytron.security.common.BcryptUtil;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    @Test
    public void hash() {
        final byte[] salt = "justATouchOfSalt".getBytes(StandardCharsets.UTF_8);
        final String password = "admin";
        final String passwordHash = BcryptUtil.bcryptHash(password, 10, salt);
        System.out.println(passwordHash);
        assertTrue(BcryptUtil.matches(password, passwordHash));
    }
}