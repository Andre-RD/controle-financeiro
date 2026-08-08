package br.com.controlefinanceiro.config;

import br.com.controlefinanceiro.config.jackson.YearMonthDeserializer;
import br.com.controlefinanceiro.config.jackson.YearMonthSerializer;

import java.time.YearMonth;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    Jackson2ObjectMapperBuilderCustomizer yearMonthJsonSupport() {
        return builder -> builder
                .serializerByType(YearMonth.class, new YearMonthSerializer())
                .deserializerByType(YearMonth.class, new YearMonthDeserializer());
    }
}
