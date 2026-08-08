package br.com.controlefinanceiro.api.dto;

import java.time.Instant;
import java.util.Map;

public record ApiError(Instant timestamp, int status, String error, Map<String, String> campos) {
}
