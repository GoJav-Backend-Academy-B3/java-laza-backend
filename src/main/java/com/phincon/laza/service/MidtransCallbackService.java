package com.phincon.laza.service;

import com.midtrans.httpclient.error.MidtransError;
import com.phincon.laza.model.dto.midtrans.gopay.GoPayCallbackRequest;

import java.security.NoSuchAlgorithmException;

public interface MidtransCallbackService {
    void callbackGopay(GoPayCallbackRequest goPayCallbackRequest) throws NoSuchAlgorithmException, MidtransError;
}
