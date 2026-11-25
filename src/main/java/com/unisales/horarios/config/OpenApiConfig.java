package com.unisales.horarios.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/** Metadados do Swagger e botão "Authorize" (HTTP Basic) aplicado a todos os endpoints. */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Horários de Aula",
                version = "1.0.0",
                description = "Organização e gerenciamento de horários de aula de professores"),
        security = @SecurityRequirement(name = "basicAuth"))
@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
public class OpenApiConfig {
}
