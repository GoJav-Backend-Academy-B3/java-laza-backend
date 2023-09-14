package com.phincon.laza.security.oauth2;

import com.phincon.laza.model.entity.*;
import com.phincon.laza.repository.ProviderRepository;
import com.phincon.laza.repository.RoleRepository;
import com.phincon.laza.repository.UserRepository;
import com.phincon.laza.security.oauth2.user.OAuth2UserInfo;
import com.phincon.laza.security.oauth2.user.OAuth2UserInfoFactory;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.validator.AuthValidator;
import com.phincon.laza.validator.ProviderValidator;
import com.phincon.laza.validator.RoleValidator;
import com.phincon.laza.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class SysOAuth2UserService extends DefaultOAuth2UserService {
    private final AuthValidator authValidator;
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final RoleRepository roleRepository;
    private final RoleValidator roleValidator;
    private final ProviderRepository providerRepository;
    private final ProviderValidator providerValidator;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        return processOAuth2User(oAuth2UserRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        authValidator.validateAuthEmailIsNull(oAuth2UserInfo.getEmail());

        Optional<User> findUser = userRepository.findByEmail(oAuth2UserInfo.getEmail());

        User user = findUser.orElseGet(() -> register(oAuth2UserRequest, oAuth2UserInfo));

        findUser.ifPresent(existingUser -> {
            userValidator.validateUserNotEqualProvider(findUser, oAuth2UserRequest.getClientRegistration().getRegistrationId());
            update(existingUser, oAuth2UserRequest, oAuth2UserInfo);
        });

        return SysUserDetails.create(user, oAuth2User.getAttributes());
    }

    private User register(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        Set<Provider> listProvider =  new HashSet<>();
        Optional<Provider> findProvider = providerRepository.findByName(EProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()));
        providerValidator.validateProviderNotFound(findProvider);
        listProvider.add(findProvider.get());

        Set<Role> listRole = new HashSet<>();
        Optional<Role> findRole = roleRepository.findByName(ERole.USER);
        roleValidator.validateRoleNotFound(findRole);
        listRole.add(findRole.get());

        User user = new User();
        user.setUsername(oAuth2UserInfo.getId());
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        user.setVerified(true);
        user.setProviders(listProvider);
        user.setRoles(listRole);

        userRepository.save(user);
        log.info("User id={} is saved with OAuth2 {}", user.getId(), oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());
        return user;
    }

    private User update(User user, OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        Set<Provider> listProvider =  user.getProviders();

        if (user.getProviders().stream().noneMatch(provider -> provider.getName().name().equalsIgnoreCase(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
            Optional<Provider> findProvider = providerRepository.findByName(EProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()));
            providerValidator.validateProviderNotFound(findProvider);
            listProvider.add(findProvider.get());
        }

        user.setName(oAuth2UserInfo.getName());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        user.setProviders(listProvider);

        userRepository.save(user);
        log.info("User id={} is updated with OAuth2 {}", user.getId(), oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());
        return user;
    }
}