package com.nsteuerberg.virma.auth_service.service.implementation;

import com.nsteuerberg.virma.auth_service.persistance.entity.RefreshTokenEntity;
import com.nsteuerberg.virma.auth_service.persistance.entity.RoleEntity;
import com.nsteuerberg.virma.auth_service.persistance.entity.UserEntity;
import com.nsteuerberg.virma.auth_service.persistance.repository.IRefreshRepository;
import com.nsteuerberg.virma.auth_service.persistance.repository.IRoleRepository;
import com.nsteuerberg.virma.auth_service.persistance.repository.IUserRepository;
import com.nsteuerberg.virma.auth_service.presentation.dto.requests.SignInRequest;
import com.nsteuerberg.virma.auth_service.presentation.dto.requests.SignUpRequest;
import com.nsteuerberg.virma.auth_service.presentation.dto.responses.TokenAuthenticationResponse;
import com.nsteuerberg.virma.auth_service.service.exception.BadRegisterException;
import com.nsteuerberg.virma.auth_service.service.exception.RefreshTokenException;
import com.nsteuerberg.virma.auth_service.service.interfaces.IAuthService;
import com.nsteuerberg.virma.auth_service.service.security.CustomUserDetails;
import com.nsteuerberg.virma.auth_service.util.constants.Roles;
import com.nsteuerberg.virma.auth_service.util.date.ExpiredDate;
import com.nsteuerberg.virma.auth_service.util.token.JwtProvider;
import com.nsteuerberg.virma.auth_service.util.token.RefreshTokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements IAuthService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IRefreshRepository refreshRepository;
    private final UserDetailServiceImpl userDetailService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenProvider refreshProvider;
    private final ExpiredDate expiredDate;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(IUserRepository userRepository, IRoleRepository roleRepository, IRefreshRepository refreshRepository, UserDetailServiceImpl userDetailService, JwtProvider jwtProvider, RefreshTokenProvider refreshProvider, ExpiredDate expiredDate, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshRepository = refreshRepository;
        this.userDetailService = userDetailService;
        this.jwtProvider = jwtProvider;
        this.refreshProvider = refreshProvider;
        this.expiredDate = expiredDate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public TokenAuthenticationResponse login(SignInRequest signInRequest, String deviceId) {
        CustomUserDetails userDetails = (CustomUserDetails) userDetailService.loadUserByUsername(signInRequest.username());
        if (!passwordEncoder.matches(signInRequest.password(), userDetails.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getId().toString(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        TokenAuthenticationResponse tokens = generateTokens(authentication);
        saveOrUpdateToken(tokens, deviceId, userDetails.getId());

        return tokens;
    }

    @Override
    public TokenAuthenticationResponse register(SignUpRequest signUpRequest, String deviceId) {

        UserEntity newUser = createUserIfNoExist(signUpRequest.username(), signUpRequest.password(), Set.of(Roles.MEMBER));

        // generate tokens
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                newUser.getId().toString(),
                newUser.getPassword(),
                userDetailService.getAuthorities(newUser.getRoleEntitySet())
        );

        TokenAuthenticationResponse tokens = generateTokens(authentication);
        saveOrUpdateToken(tokens, deviceId, newUser.getId());
        return tokens;
    }

    @Override
    public TokenAuthenticationResponse refreshTokens(String refreshToken, String deviceId) {

        RefreshTokenEntity refreshTokenEntity = refreshRepository.findByTokenAndDeviceId(
                refreshProvider.hashToken(refreshToken),
                deviceId
        )
                .orElseThrow(() -> new RefreshTokenException("Refresh token doesn't existe or else is revoked"));
        UserEntity userEntity = userRepository.findById(refreshTokenEntity.getUserId()).orElseThrow(() -> new BadCredentialsException("User doesn't exist, probably is deleted"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userEntity.getId().toString(),
                userEntity.getPassword(),
                userDetailService.getAuthorities(userEntity.getRoleEntitySet())
        );

        TokenAuthenticationResponse tokens = generateTokens(authentication);
        saveOrUpdateToken(tokens, deviceId, userEntity.getId());
        return tokens;
    }

    private UserEntity createUserIfNoExist(String username, String password, Set<Roles> rolesSet) {
        if (userRepository.findUserEntityByUsername(username).isPresent()){
            throw new BadRegisterException("User already exist with the username: " + username);
        }
        // si no hay roles agregamos uno nuevo
        if (rolesSet.isEmpty()) rolesSet.add(Roles.MEMBER);
        Set<RoleEntity> roleEntities = new HashSet<>();
        rolesSet.forEach(role ->
                roleEntities.add(roleRepository.findRoleEntityByRole(role).orElseThrow(() ->
                        new BadRegisterException(role.name() + " don't exists")
                ))
        );

        UserEntity createUser = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roleEntitySet(roleEntities)
                .build();
        return userRepository.save(createUser);
    }

    private TokenAuthenticationResponse generateTokens (Authentication authentication) {
        String accessToken = jwtProvider.createToken(authentication);
        String refreshToken = refreshProvider.generateToken();
        return new TokenAuthenticationResponse(refreshToken, accessToken);
    }

    private void saveOrUpdateToken(TokenAuthenticationResponse tokens, String deviceId, Long userId) {
        refreshRepository.findByUserIdAndDeviceId(userId, deviceId).ifPresentOrElse(
                token -> {
                    token.setToken(refreshProvider.hashToken(tokens.refreshToken()));
                    token.setExpiredDate(expiredDate.getExpiredDate());
                    refreshRepository.save(token);
                },
                () -> {
                    RefreshTokenEntity refreshEntity = RefreshTokenEntity.builder()
                            .deviceId(deviceId)
                            .userId(userId)
                            .token(refreshProvider.hashToken(tokens.refreshToken()))
                            .expiredDate(expiredDate.getExpiredDate())
                            .build();
                    refreshRepository.save(refreshEntity);
                }
        );
    }
}
