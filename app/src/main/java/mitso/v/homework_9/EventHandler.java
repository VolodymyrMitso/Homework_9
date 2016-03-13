package mitso.v.homework_9;

public interface EventHandler {

    void openRegistrationFragment();
    void registerPerson(
            String firstName,
            String lastName,
            String login,
            String password,
            String gender);
}