package com.nsteuerberg.backend.virma.service.implementation;

import com.nsteuerberg.backend.virma.service.interfaces.ICommonMediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;

@Service
public class CommonMediaServiceImpl implements ICommonMediaService {
    private final RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(CommonMediaServiceImpl.class);

    @Value("${nginx.server.url}")
    private String nginxBaseUrl;

    public CommonMediaServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public boolean isUrlValid(String endpoint) {
        log.info("Verificando la url -> {}", nginxBaseUrl + endpoint);
        boolean isValid = false;
        try {
            URL parsedUrl = new URL(nginxBaseUrl + endpoint);
            // isValid = isValidServer(parsedUrl);
            isValid = urlExists(parsedUrl);
        } catch (Exception e){
            isValid = false;
            log.error("Error verificando la url: {} -> {}: {}", endpoint, e.getClass().getName(), e.getMessage());
        }
        if (isValid) log.info("La url {} es valida", endpoint);
        else log.warn("La url {} no es válida", endpoint);
        return isValid;
    }

    @Override
    public String createUrlByEndpoint(String endpoint) {
        if (endpoint == null) return null;
        return nginxBaseUrl + endpoint;
    }

    private boolean isValidServer(URL url) {
        String route = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
        return nginxBaseUrl.equals(route);
    }

    private boolean urlExists(URL url) {
        try {
            ResponseEntity<?> response = restTemplate.exchange(url.toURI(), HttpMethod.GET, null, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new Exception("No ha sido satisfactoria");
            }
            String headerContentType = response.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
            if (headerContentType == null || !headerContentType.toLowerCase().contains("mpegurl")){
                throw new Exception("No es un fichero correcto");
            }

            return true;
        } catch (Exception e) {
            log.error("Error comprobando si la url existe -> {}: {}", e.getClass().getName(), e.getMessage());
            return false;
        }
    }
}
