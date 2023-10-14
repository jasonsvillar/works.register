package com.jasonvillar.works.register.authentication;

import lombok.RequiredArgsConstructor;

import java.util.TimerTask;

@RequiredArgsConstructor
public class DeleteToExpireJwtTask extends TimerTask {
    private String jwt;
    private JWTBlacklistService jwtBlacklistService;
    public DeleteToExpireJwtTask(String jwt, JWTBlacklistService jwtBlacklistService) {
        this.jwt = jwt;
        this.jwtBlacklistService = jwtBlacklistService;
    }
    @Override
    public void run() {
        this.jwtBlacklistService.deleteByToken(this.jwt);
    }
}
