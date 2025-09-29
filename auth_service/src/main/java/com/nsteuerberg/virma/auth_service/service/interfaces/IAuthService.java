package com.nsteuerberg.virma.auth_service.service.interfaces;

import com.nsteuerberg.virma.auth_service.presentation.dto.requests.SignInRequest;
import com.nsteuerberg.virma.auth_service.presentation.dto.requests.SignUpRequest;
import com.nsteuerberg.virma.auth_service.presentation.dto.responses.TokenAuthenticationResponse;

public interface IAuthService {
    TokenAuthenticationResponse login(SignInRequest signInRequest, String deviceId);

    TokenAuthenticationResponse register(SignUpRequest signUpRequest, String deviceId);

    TokenAuthenticationResponse refreshTokens(String refreshToken, String deviceId);
}
