package com.vi.StoryHelperBack.service;

public interface TokenCheckerService {
    boolean checkToken(String token, String username);
}
