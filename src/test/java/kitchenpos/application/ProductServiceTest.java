package kitchenpos.application;

import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.infra.ProfanityClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private ProductRepository productRepository = new InMemoryProductRepository();

    private MenuRepository menuRepository = new InMemoryMenuRepository();

    private ProfanityClient purgomalumClient = new FakeProfanityClient();

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository, menuRepository, purgomalumClient);
    }

    @Test
    public void 상품을_등록한다() {
        final Product request = createProductRequest();
        final Product actual = productService.create(request);
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    void 상품의_가격은_0원_미만이면_예외가_발생한다() {
        final long price = -1L;
        final Product request = createProductRequest(price);
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(ProductPriceException.class);
    }

    @ValueSource(strings = {"비속어가 포함된 이름", "치킨 욕설"})
    @ParameterizedTest
    void 상품의_이름에는_비속어가_포함될_수_없다(final String name) {
        final Product request = createProductRequest(name);
        assertThatThrownBy(() -> productService.create(request))
                .isExactlyInstanceOf(ProductNameException.class);
    }

    @Test
    void 상품을_조회한다() {
        productRepository.save(createProduct(UUID.randomUUID(), "후라이드", BigDecimal.valueOf(16_000L)));
        productRepository.save(createProduct(UUID.randomUUID(), "양념치킨", BigDecimal.valueOf(18_000L)));
        final List<Product> actual =  productService.findAll();
        assertThat(actual).hasSize(2);
    }

    @Test
    void 상품의_가격을_변경한다() {
        final Product product = createProduct(UUID.randomUUID(), "후라이드", BigDecimal.valueOf(16_000L));
        productRepository.save(product);
        final Product actual = productService.changePrice(product.getId(), createProductRequest(20_000L));
        assertThat(actual.getPrice()).isEqualTo(BigDecimal.valueOf(20_000L));
    }

    private Product createProductRequest() {
        return createProductRequest("후라이드 치킨", BigDecimal.valueOf(16_000L));
    }

    private Product createProductRequest(final String name) {
        return createProductRequest(name, BigDecimal.valueOf(16_000L));
    }

    private Product createProductRequest(final long price) {
        return createProductRequest(BigDecimal.valueOf(price));
    }

    private Product createProductRequest(final BigDecimal price) {
        return createProductRequest("후라이드 치킨", price);
    }

    private Product createProductRequest(final String name, final BigDecimal price) {
        final Product request = new Product();
        request.setName(name);
        request.setPrice(price);
        return request;
    }

    private Product createProduct(final UUID id, final String name, final BigDecimal price) {
        final Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}