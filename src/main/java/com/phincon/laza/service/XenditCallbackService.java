package com.phincon.laza.service;

import com.phincon.laza.model.dto.xendit.ewallet.EwalletCallbackRequest;
import com.phincon.laza.model.dto.xendit.fva.FVACallbackCreated;
import com.phincon.laza.model.dto.xendit.fva.FVACallbackRequest;
import com.xendit.exception.XenditException;

public interface XenditCallbackService {
    void callbackEwallet(EwalletCallbackRequest ewalletCallbackRequest) throws XenditException;

    void callbackFVAPaid(FVACallbackRequest fvaCallbackRequest);

    void callbackFVACreate(FVACallbackCreated fvaCallbackCreated);
}
