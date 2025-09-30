package com.nsteuerberg.backend.virma.service.interfaces;

public interface ICommonMediaService {
    boolean isUrlValid(String url, boolean isCover);
    String createUrlByEndpoint(String endpoint);
}
