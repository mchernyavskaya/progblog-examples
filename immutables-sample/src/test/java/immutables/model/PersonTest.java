package immutables.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class PersonTest {
    private static Person JOHN = ImmutablePerson.builder()
            .firstName("John")
            .lastName("Doe")
            .age(30)
            .build();
    private static Person JANE = ImmutablePerson.builder()
            .firstName("Jane")
            .lastName("Doe")
            .age(30)
            .build();

    @Test
    public void testEquals() throws Exception {
        //ImmutablePerson JOHN_COPY = ImmutablePerson.builder().from(JOHN).build();
        Person JOHN_COPY = ImmutablePerson.copyOf(JOHN);
        assertThat(JOHN_COPY).isEqualTo(JOHN);
    }

    @Test
    public void testNotEquals() throws Exception {
        assertThat(JANE).isNotEqualTo(JOHN);
    }

    @Test
    public void testHashCode() throws Exception {
        Person JOHN_COPY = ImmutablePerson.copyOf(JOHN);
        assertThat(JOHN_COPY.hashCode()).isEqualTo(JOHN.hashCode());
    }

    @Test
    public void testHashCodeNotEquals() throws Exception {
        Person JOHN_COPY = ImmutablePerson.copyOf(JOHN);
        assertThat(JOHN_COPY.hashCode()).isNotEqualTo(JANE.hashCode());
    }

    @Test
    public void testToString() throws Exception {
        String jane = JANE.toString();

        assertThat(jane).contains(JANE.firstName());
        assertThat(jane).contains(JANE.lastName());
        assertThat(jane).contains("" + JANE.age());
        assertThat(jane).doesNotContain(JOHN.firstName());
    }

}