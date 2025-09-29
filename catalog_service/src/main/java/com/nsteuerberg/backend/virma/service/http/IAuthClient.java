package com.nsteuerberg.backend.virma.service.http;

import com.nsteuerberg.backend.virma.service.http.responses.security.RsaPublicKeyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "authentication",
        url = "http://localhost:8081/api/auth"
)
public interface IAuthClient {
    @GetMapping("public-key")
    RsaPublicKeyResponse getPublicKey();
}