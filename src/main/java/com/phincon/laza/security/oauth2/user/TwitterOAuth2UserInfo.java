package com.phincon.laza.security.oauth2.user;


import java.util.Map;

public class TwitterOAuth2UserInfo extends OAuth2UserInfo {

    public TwitterOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }
    @Override
    public String getId() {
        Map<String, Object> data = (Map<String, Object>) attributes.get("data");

        if (data == null) {
            return null;
        }

        return (String) data.get("id_str");
    }

    @Override
    public String getName() {
        Map<String, Object> data = (Map<String, Object>) attributes.get("data");

        if (data == null) {
            return null;
        }

        return (String) data.get("name");
    }

    @Override
    public String getEmail() {
        Map<String, Object> data = (Map<String, Object>) attributes.get("data");

        if (data == null) {
            return null;
        }

        return (String) data.get("email");
    }

    @Override
    public String getImageUrl() {
        Map<String, Object> data = (Map<String, Object>) attributes.get("data");
        if (data == null) {
            return null;
        }

        return (String) data.get("profile_image_url");    }
}
