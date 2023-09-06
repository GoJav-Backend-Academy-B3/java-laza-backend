package com.phincon.laza.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.phincon.laza.model.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponse {
    private String id;
    private String fullName;
    private String username;
    private String email;

    public UserResponse (User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    public List<UserResponse> list(List<User> users) {
        List<UserResponse> list = new ArrayList<>();
        for (User v : users) {
            list.add(new UserResponse(v));
        }
        return list;
    }
}
