package immutables.model;

import org.immutables.value.Value;

@Value.Immutable
public abstract class Person {
    public abstract String lastName();
    public abstract String firstName();
    public abstract Integer age();
}
