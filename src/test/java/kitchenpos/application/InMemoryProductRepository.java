package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;

import java.util.*;
import java.util.stream.Collectors;

class InMemoryProductRepository implements ProductRepository {
    private final Map<UUID, Product> entities = new HashMap<>();

    @Override
    public Product save(Product entity) {
        entities.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Product> findById(UUID uuid) {
        return Optional.ofNullable(entities.get(uuid));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public List<Product> findAllByIdIn(List<UUID> ids) {
        return ids.stream()
                .map(it -> entities.get(it))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}