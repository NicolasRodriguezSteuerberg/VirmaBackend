package com.nsteuerberg.backend.virma.service.interfaces;

public interface ICommonMediaService {
    boolean isUrlValid(String url);
    String createUrlByEndpoint(String endpoint);
}
