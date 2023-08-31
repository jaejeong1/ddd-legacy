package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryMenuRepository implements MenuRepository {
    private final Map<UUID, Menu> entities = new HashMap<>();

    @Override
    public <S extends Menu> S save(S entity) {
        entities.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Menu> findById(UUID uuid) {
        return Optional.ofNullable(entities.get(uuid));
    }

    @Override
    public List<Menu> findAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public List<Menu> findAllByIdIn(List<UUID> ids) {
        return ids.stream()
                .map(it -> entities.get(it))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Menu> findAllByProductId(UUID productId) {
        return entities.values().stream()
                .filter(menu -> menu.getMenuProducts().stream().anyMatch(it -> productId.equals(it.getProductId())))
                .collect(Collectors.toList());

    }
}