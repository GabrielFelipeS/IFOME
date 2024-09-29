package br.com.ifsp.ifome.validation.interfaces;

import java.util.Map;
import java.util.Optional;

public interface Validator<T> {
    Optional<Map.Entry<String, String>> isValid(T request);
}
