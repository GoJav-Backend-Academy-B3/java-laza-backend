package com.phincon.laza.security.oauth2.user;

import lombok.Data;

import java.util.Map;

@Data
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public abstract String getId();
    public abstract String getName();
    public abstract String getEmail();
    public abstract String getImageUrl();

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
