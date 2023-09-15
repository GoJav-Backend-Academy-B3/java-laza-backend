package com.phincon.laza.controller;

import com.midtrans.httpclient.error.MidtransError;
import com.phincon.laza.model.dto.midtrans.gopay.GoPayCallbackRequest;
import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.service.MidtransCallbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
public class MidtransCallbackController {

    @Autowired
    private MidtransCallbackService midtransCallbackService;

    @PostMapping("/midtrans/gopay-callback")
    public ResponseEntity<DataResponse<String>> callbackGopay(@RequestBody GoPayCallbackRequest request) throws MidtransError, NoSuchAlgorithmException {
        midtransCallbackService.callbackGopay(request);

        return DataResponse.ok("callback accepted");
    }

}
