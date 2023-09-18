package com.phincon.laza.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import com.phincon.laza.exception.custom.BadRequestException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageUploadInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // allow request to passthrough this interceptor if the request is neither POST
        // or PUT
        if (!request.getMethod().equalsIgnoreCase("POST") && !request.getMethod().equalsIgnoreCase("PUT")) {
            return true;
        }
        var parts = request.getPart("imageFile");
        if (parts == null) return true;
        String contentType = parts.getContentType();
        long size = parts.getSize();
        log.info("From {} got a file with Content-Type: {} and size: {}", request.getRemoteAddr(), contentType,
                size);
        if (contentType.equalsIgnoreCase("image/png") || contentType.equalsIgnoreCase("image/jpeg")
                || contentType.equalsIgnoreCase("image/webp")) {
            return true;
        } else {
            String format = "Supported files are: image/png, image/jpeg, image/webp. But provided %s.";
            response.sendError(400, String.format(format, contentType));
            return false;
        }
    }
}
