package com.viewnext.core.security;

import com.warrenstrange.googleauth.ICredentialRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CredentialRepositoryImpl implements ICredentialRepository {
    private final Map<String, String> credentials = new HashMap<>();

    @Override
    public String getSecretKey(String userName) {
        return credentials.get(userName);
    }

    @Override
    public void saveUserCredentials(String userName, String secretKey, int validationCode, List<Integer> scratchCodes) {
        credentials.put(userName, secretKey);
    }
}
