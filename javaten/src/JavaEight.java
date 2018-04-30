import java.util.Arrays;
import java.util.List;

public class JavaEight {
    static class User {
        String firstName;
        String lastName;
        int age;

        User(String firstName, String lastName, int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    public static void main(String[] args) {
        List<User> users = Arrays.asList(
                new User("Elisabeth", "Bennett", 20),
                new User("Jane", "Bennett", 22),
                new User("Mary", "Bennett", 18),
                new User("Kitty", "Bennett", 17),
                new User("Lydia", "Bennett", 15)
        );
        users.stream()
                .map(u ->
                        new Object() {
                            String fullName = u.firstName + " " + u.lastName;
                            boolean canDrink = u.age >= 18;
                        })
                .forEach(u -> {
                    if (u.canDrink) {
                        System.out.println("+ " + u.fullName + " is of age and can drink");
                    } else {
                        System.out.println("- " + u.fullName + " is not of age and cannot drink");
                    }
                });
        /*  Output will be
         * + Elisabeth Bennett is of age and can drink
         * + Jane Bennett is of age and can drink
         * + Mary Bennett is of age and can drink
         * - Kitty Bennett is not of age and cannot drink
         * - Lydia Bennett is not of age and cannot drink
         */
    }
}
