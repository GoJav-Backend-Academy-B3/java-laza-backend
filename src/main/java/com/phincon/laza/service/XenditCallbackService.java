package com.phincon.laza.service;

import com.phincon.laza.model.dto.xendit.ewallet.EwalletCallbackRequest;
import com.xendit.exception.XenditException;

public interface XenditCallbackService {
    void callbackEwallet(EwalletCallbackRequest ewalletCallbackRequest) throws XenditException;
}
