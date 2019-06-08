package edu.csumb.spring19.capstone.services;

import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginProtectionService {
    private final int MAX_ATTEMPTS = 10;
    private LoadingCache<String, Integer> attemptsCache;

    @Autowired
    private HttpServletRequest request;

    public LoginProtectionService() {
        attemptsCache = CacheBuilder.newBuilder().
              expireAfterWrite(1, TimeUnit.HOURS).build(new CacheLoader<String, Integer>() {
            public Integer load(String key) {
                return 0;
            }
        });
    }

    public boolean isBlocked() {
        try {
            return attemptsCache.get(getIp()) > MAX_ATTEMPTS;
        } catch (ExecutionException ignored) {}

        return true;
    }

    public void loginFailed() {
        int attempts = 0;
        String currentIP = getIp();
        try {
            attempts = attemptsCache.get(currentIP);
        } catch (ExecutionException e) {
            attempts = 0;
        }

        attempts++;
        attemptsCache.put(currentIP, attempts);
    }

    public void loginSucceeded() {
        attemptsCache.invalidate(getIp());
    }

    private String getIp() {
        String header = request.getHeader("X-Forwarded-For");
        if (Strings.isNullOrEmpty(header)) return request.getRemoteAddr();
        return header.split(",")[0];
    }
}