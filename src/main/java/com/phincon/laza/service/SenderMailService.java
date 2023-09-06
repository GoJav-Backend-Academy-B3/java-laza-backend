package com.phincon.laza.service;

import com.phincon.laza.model.entity.User;
import jakarta.mail.MessagingException;

public interface SenderMailService {
   void confirmRegister(User user, String token) throws MessagingException;
   void forgotPassword(User user, String code) throws MessagingException;
}
