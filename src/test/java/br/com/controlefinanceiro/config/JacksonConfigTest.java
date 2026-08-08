package br.com.controlefinanceiro.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.YearMonth;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static org.assertj.core.api.Assertions.assertThat;

class JacksonConfigTest {

    @Test
    void serializaEDesserializaYearMonthComoAnoMes() throws Exception {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        new JacksonConfig().yearMonthJsonSupport().customize(builder);
        ObjectMapper mapper = builder.build();

        String json = mapper.writeValueAsString(new Referencia(YearMonth.of(2026, 9)));
        Referencia resposta = mapper.readValue("{\"mesReferencia\":\"2026-09\"}", Referencia.class);

        assertThat(json).isEqualTo("{\"mesReferencia\":\"2026-09\"}");
        assertThat(resposta.mesReferencia()).isEqualTo(YearMonth.of(2026, 9));
    }

    private record Referencia(YearMonth mesReferencia) {
    }
}
