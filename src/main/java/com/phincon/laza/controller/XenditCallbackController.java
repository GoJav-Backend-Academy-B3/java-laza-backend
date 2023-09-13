package com.phincon.laza.controller;


import com.phincon.laza.model.dto.response.DataResponse;
import com.phincon.laza.model.dto.xendit.ewallet.EwalletCallbackRequest;
import com.phincon.laza.model.dto.xendit.fva.FVACallbackRequest;
import com.phincon.laza.service.XenditCallbackService;
import com.xendit.exception.XenditException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "callback")
public class XenditCallbackController {

    @Value("${xendit.callback.token}")
    private String XENDIT_CALLBACK_TOKEN;

    @Autowired
    private XenditCallbackService xenditCallbackService;

    @PostMapping("/ewallet")
    public ResponseEntity<DataResponse<String>> callbackEwallet(HttpServletRequest request, @RequestBody EwalletCallbackRequest body) throws XenditException {

        if (request.getHeader("X-CALLBACK-TOKEN").equals(XENDIT_CALLBACK_TOKEN)) {
            ResponseEntity.badRequest();
        }

        xenditCallbackService.callbackEwallet(body);

        return DataResponse.ok("callback accepted");
    }

    @PostMapping("/fva/paid")
    public ResponseEntity<DataResponse<String>> callbackPaidFVA(HttpServletRequest request, @RequestBody FVACallbackRequest body) throws XenditException {

        if (request.getHeader("X-CALLBACK-TOKEN").equals(XENDIT_CALLBACK_TOKEN)) {
            ResponseEntity.badRequest();
        }

        xenditCallbackService.callbackFVA(body);

        return DataResponse.ok("callback accepted");
    }

}
