package com.phincon.laza.service.impl;

import com.phincon.laza.model.entity.User;
import com.phincon.laza.service.SenderMailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class SenderMailServiceImpl implements SenderMailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${application.frontend.origin-url}")
    private String frontendOriginUrl;

    @Async
    @Override
    public void confirmRegister(User user, String token) throws MessagingException {
        String link = String.format("%s/auth/register/confirm?token=%s", frontendOriginUrl, token);
        String subject = "Confirm your account";

        Context context = new Context();
        context.setVariable("name", user.getUsername());
        context.setVariable("link", link);

        String buildMail = templateEngine.process("auth/confirm-email.html", context);
        sendMail(user, buildMail, subject);
    }

    @Async
    @Override
    public void forgotPassword(User user, String code) throws MessagingException {
        String subject = "Forgot Password";

        Context context = new Context();
        context.setVariable("name", user.getUsername());
        context.setVariable("code", code);

        String buildMail = templateEngine.process("auth/forgot-password-email.html", context);
        sendMail(user, buildMail, subject);
    }

    private void sendMail(User user, String buildMail, String subject) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(buildMail, true);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setFrom("libary@email.com");
        javaMailSender.send(mimeMessage);
    }
}
