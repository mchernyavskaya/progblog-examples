package lombokify.model;

import lombok.*;

@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Person {
    @NonNull
    @Getter
    private final String lastName;
    @NonNull
    @Getter
    private final String firstName;
    @NonNull
    @Getter
    private final Integer age;
}
