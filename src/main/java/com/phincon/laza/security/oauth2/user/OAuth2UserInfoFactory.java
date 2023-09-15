package com.phincon.laza.security.oauth2.user;

import com.phincon.laza.exception.custom.OAuth2ProcessingException;
import com.phincon.laza.model.entity.EProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        switch (EProvider.valueOf(registrationId.toUpperCase())) {
            case GOOGLE:
                return new GoogleOAuth2UserInfo(attributes);
            case FACEBOOK:
                return new FacebookOAuth2UserInfo(attributes);
            case TWITTER:
                return new TwitterOAuth2UserInfo(attributes);
            default:
                throw new OAuth2ProcessingException(String.format("Sorry! Login with %s is not supported yet.", registrationId));
        }
    }
}
