package com.phincon.laza.security.oauth2.user;

import com.phincon.laza.exception.custom.NotProcessException;
import com.phincon.laza.model.entity.EProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        switch (EProvider.valueOf(registrationId.toUpperCase())) {
            case GOOGLE:
                return new GoogleOAuth2UserInfo(attributes);
            case FACEBOOK:
                return new FacebookOAuth2UserInfo(attributes);
            default:
                throw new NotProcessException(String.format("Sorry! Login with %s is not supported yet.", registrationId));
        }
    }
}
