package ru.mtuci.coursemanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.net.URI;
import java.net.InetAddress;

@RestController
public class ProxyController {
    @GetMapping("/api/proxy")
    public String proxy(@RequestParam("targetUrl") String targetUrl) {
        try {
            URI uri = new URI(targetUrl);
	    String scheme = uri.getScheme();
            if (!("http".equals(scheme) || "https".equals(scheme))) {
                return "error";
            }
            String host = uri.getHost();
            InetAddress inetAddress = InetAddress.getByName(host);
            if (!(inetAddress.isSiteLocalAddress() || inetAddress.isAnyLocalAddress() || inetAddress.isLoopbackAddress())){
                return "error";
            }
        } catch (Exception e) {
            return "error";
        }
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(1000);
        RestTemplate rt = new RestTemplate(requestFactory);
        return rt.getForObject(targetUrl, String.class);
    }
}
