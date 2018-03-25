package autovalue.model;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Person {
    public abstract String lastName();

    public abstract String firstName();

    public abstract Integer age();

    public static Person create(String lastName, String firstName, Integer age) {
        return builder().lastName(lastName).firstName(firstName).age(age).build();
    }

    public static Builder builder() {
        return new AutoValue_Person.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder lastName(String lastName);
        public abstract Builder firstName(String firstName);
        public abstract Builder age(Integer age);

        public abstract Person build();
    }
}
