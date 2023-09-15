package com.phincon.laza.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phincon.laza.model.dto.response.CreditCardResponse;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.security.userdetails.CurrentUser;
import com.phincon.laza.security.userdetails.SysUserDetails;
import com.phincon.laza.service.CreditCardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cc")
@RequiredArgsConstructor
public class CreditCardController {

    @Autowired
    private final CreditCardService service; 

    @GetMapping
    public ResponseEntity<DataResponse<List<CreditCardResponse>>> getAllCreditCards(@CurrentUser SysUserDetails currentUser) {
        var lists = service.getAll(currentUser.getId());
        var response = lists.stream().map(CreditCardResponse::fromEntity).collect(Collectors.toList());
        return DataResponse.ok(response);
    }
}
