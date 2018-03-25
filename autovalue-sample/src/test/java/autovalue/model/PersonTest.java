package autovalue.model;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class PersonTest {
    private static Person JOHN = Person.builder()
            .firstName("John")
            .lastName("Doe")
            .age(30)
            .build();
    private static Person JANE = Person.builder()
            .firstName("Jane")
            .lastName("Doe")
            .age(30)
            .build();

    @Test
    public void testEquals() throws Exception {
        Person JOHN_COPY = Person.create(JOHN.lastName(), JOHN.firstName(), JOHN.age());
        assertThat(JOHN_COPY).isEqualTo(JOHN);
    }

    @Test
    public void testNotEquals() throws Exception {
        assertThat(JANE).isNotEqualTo(JOHN);
    }

    @Test
    public void testHashCode() throws Exception {
        Person JOHN_COPY = Person.create(JOHN.lastName(), JOHN.firstName(), JOHN.age());
        assertThat(JOHN_COPY.hashCode()).isEqualTo(JOHN.hashCode());
    }

    @Test
    public void testHashCodeNotEquals() throws Exception {
        Person JOHN_COPY = Person.create(JOHN.lastName(), JOHN.firstName(), JOHN.age());
        assertThat(JOHN_COPY.hashCode()).isNotEqualTo(JANE.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        String jane = JANE.toString();

        assertThat(jane).contains(JANE.lastName());
        assertThat(jane).contains(JANE.firstName());
        assertThat(jane).contains("" + JANE.age());
        assertThat(jane).doesNotContain(JOHN.firstName());
    }

}