package br.com.controlefinanceiro.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI controleFinanceiroOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Controle Financeiro API")
                        .description("API REST para controle de gastos pessoais")
                        .version("0.0.1"));
    }
}
