package kitchenpos.application;

import kitchenpos.infra.ProfanityClient;

import java.util.List;

class FakeProfanityClient implements ProfanityClient {
    private final List<String> profanities = List.of("욕설", "비속어");

    @Override
    public boolean containsProfanity(String text) {
        return profanities.stream()
                        .anyMatch(text::contains);
    }
}