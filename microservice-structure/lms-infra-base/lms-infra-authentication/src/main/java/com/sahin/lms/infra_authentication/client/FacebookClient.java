package com.sahin.lms.infra_authentication.client;

import com.sahin.lms.infra.model.auth.FacebookUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class FacebookClient {

    @Autowired
    private RestTemplate restTemplate;

    private static final String FACEBOOK_GRAPH_API_BASE = "https://graph.facebook.com";

    public FacebookUser getUser(String accessToken) {

        String path = "/me?fields={fields}&redirect={redirect}&access_token={access_token}";
        String fields = "email,first_name,last_name,id";
        final Map<String, String> variables = new HashMap<>();
        variables.put("fields", fields);
        variables.put("redirect", "false");
        variables.put("access_token", accessToken);
        return restTemplate.getForObject(FACEBOOK_GRAPH_API_BASE + path, FacebookUser.class, variables);
    }
}
