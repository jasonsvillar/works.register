package com.jasonvillar.works.register.schedule_tasks;

import com.jasonvillar.works.register.services.JWTBlacklistService;
import lombok.RequiredArgsConstructor;

import java.util.TimerTask;

@RequiredArgsConstructor
public class DeleteToExpireJWT extends TimerTask {
    private String jwt;
    private JWTBlacklistService jwtBlacklistService;
    public DeleteToExpireJWT(String jwt, JWTBlacklistService jwtBlacklistService) {
        this.jwt = jwt;
        this.jwtBlacklistService = jwtBlacklistService;
    }
    @Override
    public void run() {
        this.jwtBlacklistService.deleteByToken(this.jwt);
    }
}
