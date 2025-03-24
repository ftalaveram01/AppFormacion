package com.viewnext.core.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

@Component
public class UtilsOTP {
    private final GoogleAuthenticator gAuth;

    @Autowired
    public UtilsOTP() {
        this.gAuth = new GoogleAuthenticator();
    }
    
    public GoogleAuthenticatorKey generateKey() {
        return gAuth.createCredentials();
    }

    public String generateQRCode(String email, GoogleAuthenticatorKey key) {
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL("GestionFormacion", email, key);
    }

    public boolean verifyCode(String secreto, int code) {
        return gAuth.authorize(secreto, code, new Date().getTime());
    }
}
