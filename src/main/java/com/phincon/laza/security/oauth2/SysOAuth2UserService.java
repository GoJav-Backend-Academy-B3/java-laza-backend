package com.phincon.laza.security.oauth2;

import com.phincon.laza.model.entity.EProvider;
import com.phincon.laza.model.entity.ERole;
import com.phincon.laza.model.entity.Role;
import com.phincon.laza.model.entity.User;
import com.phincon.laza.repository.RoleRepository;
import com.phincon.laza.repository.UserRepository;
import com.phincon.laza.security.oauth2.user.OAuth2UserInfo;
import com.phincon.laza.security.oauth2.user.OAuth2UserInfoFactory;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.validator.AuthValidator;
import com.phincon.laza.validator.RoleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SysOAuth2UserService extends DefaultOAuth2UserService {
    private final AuthValidator authValidator;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleValidator roleValidator;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        return processOAuth2User(oAuth2UserRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        authValidator.validateAuthEmailNull(oAuth2UserInfo.getEmail());

        Optional<User> findUser = userRepository.findByEmail(oAuth2UserInfo.getEmail());

        User user = findUser.orElseGet(() -> register(oAuth2UserRequest, oAuth2UserInfo));

        findUser.ifPresent(existingUser -> {
            authValidator.validateAuthProvider(existingUser.getProvider(), oAuth2UserRequest.getClientRegistration().getRegistrationId());
            update(existingUser, oAuth2UserInfo);
        });

        return SysUserDetails.create(user, oAuth2User.getAttributes());
    }

    private User register(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        List<Role> listRole = new ArrayList<>();
        Optional<Role> findRole = roleRepository.findByName(ERole.USER);
        roleValidator.validateRoleNotFound(findRole);
        listRole.add(findRole.get());

        User user = new User();
        user.setUsername(oAuth2UserInfo.getId());
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        user.setVerified(true);
        user.setProvider(EProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()));
        user.setRoles(listRole);

        return userRepository.save(user);
    }

    private User update(User user, OAuth2UserInfo oAuth2UserInfo) {
        user.setName(oAuth2UserInfo.getName());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());

        return userRepository.save(user);
    }
}