package com.api_gateway.constant;

import java.util.Set;

public class ApiPaths
{
    private static final String API_REGISTRATION = "/api/v1/authentication/register";
    private static final String API_LOGIN = "/api/v1/authentication/login";

    private static final String TEST = "/api/v1/authentication/test";

//    private static final String ACTUATOR = "/actuator/*";

    public static final Set<String> ApiPath = Set.of(API_REGISTRATION,API_LOGIN,TEST);
}
