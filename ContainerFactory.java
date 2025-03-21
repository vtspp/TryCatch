package br.com.vtspp.github;

import java.util.Map;

@FunctionalInterface
public interface ContainerFactory<K, V> {
    Map<K, V> container();
}
