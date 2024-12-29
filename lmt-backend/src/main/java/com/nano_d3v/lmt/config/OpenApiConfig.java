package com.nano_d3v.lmt.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = 

@Info(contact = 
@Contact(name = "Rayane", email = "rayanmelzi@outlook.com", url = ""),
          description = "Life Management Tool", 
          title = "LMT API", 
          version = "1.0", 
          termsOfService = "Terms of service"), 
          
          servers = {
        @Server
        (description = "Local ENV", url = "http://localhost:8080"),

        @Server
        (description = "PROD ENV", url = "")
}, 

    security = {
        @SecurityRequirement(name = "bearerAuth")
})


public class OpenApiConfig {
}