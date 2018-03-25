package autovalue;

import autovalue.model.Person;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        final Person anna = Person.builder()
                .age(31)
                .firstName("Anna")
                .lastName("Smith")
                .build();
        System.out.println(anna);
    }
}
