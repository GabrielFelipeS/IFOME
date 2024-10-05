package br.com.ifsp.ifome.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse (String status, Object data, String message) { }


