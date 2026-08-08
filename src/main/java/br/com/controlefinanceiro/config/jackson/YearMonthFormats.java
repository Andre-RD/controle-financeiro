package br.com.controlefinanceiro.config.jackson;

import java.time.format.DateTimeFormatter;

final class YearMonthFormats {

    static final DateTimeFormatter JSON = DateTimeFormatter.ofPattern("yyyy-MM");

    private YearMonthFormats() {
    }
}
