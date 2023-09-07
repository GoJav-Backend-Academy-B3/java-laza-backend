package com.phincon.laza.service;

import com.phincon.laza.model.dto.request.ChangePasswordRequest;
import com.phincon.laza.model.dto.request.RoleRequest;
import com.phincon.laza.model.dto.request.UserRequest;
import com.phincon.laza.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    Page<User> getAll(Pageable pageable);
    User getByUsername(String username);
    User update(String username, UserRequest request) throws Exception;
    void changePassword(String username, ChangePasswordRequest request);
    void updateRole(String username, RoleRequest request);
}
