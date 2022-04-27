package com.young.commons.config;

import java.util.*;
import java.util.function.UnaryOperator;

import static java.util.stream.Collectors.toList;

public final class Environment {

    public static final UnaryOperator<List<String>> LIST_SANITIZER =
        input -> Optional.ofNullable(input).orElse(Collections.emptyList()).stream()
            .filter(Objects::nonNull)
            .map(String::trim)
            .collect(toList());

    private final Map<String, String> environment;

    public Environment() {
        this(System.getenv());
    }

    public Environment(final Map<String, String> environment) {
        this.environment = environment;
    }

    public Set<String> getPropertyKeys() {
        return Collections.unmodifiableSet(environment.keySet());
    }

    public Optional<String> get(final String name) {
        return Optional.ofNullable(environment.get(name));
    }

    public Optional<Integer> getInt(final String name) {
        try {
            return get(name).map(Integer::valueOf);
        } catch (final Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Double> getDouble(final String name) {
        try {
            return get(name).map(Double::valueOf);
        } catch (final Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Long> getLong(final String name) {
        try {
            return get(name).map(Long::valueOf);
        } catch (final Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Boolean> getBool(final String name) {
        try {
            return get(name).map(Boolean::valueOf);
        } catch (final Exception e) {
            return Optional.empty();
        }
    }

    public Optional<List<String>> getList(final String name) {
        return get(name).map(v -> v.split(",")).map(Arrays::asList).map(LIST_SANITIZER);
    }
}
