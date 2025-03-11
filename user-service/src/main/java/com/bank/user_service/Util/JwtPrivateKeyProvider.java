package com.bank.user_service.Util;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;

@Component
@Getter
public class JwtPrivateKeyProvider {
    private final PrivateKey privateKey;
    //private final PublicKey publicKey;

    public JwtPrivateKeyProvider() throws Exception {
        byte[] keyBytes = Files.readAllBytes(new ClassPathResource("school-private.pem").getFile().toPath());
        String privateKeyPEM = new String(keyBytes)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }

}

