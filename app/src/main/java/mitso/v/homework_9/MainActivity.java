package mitso.v.homework_9;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements EventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (savedInstanceState == null)
            commitSignInFragment();
            commitHeadlessFragment();
    }

    private void commitSignInFragment() {
        SignInFragment signInFragment = new SignInFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_Container_AM, signInFragment, Constants.SIGN_IN_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        signInFragment.setEventHandler(this);
    }

    private void commitRegistrationFragment() {
        RegistrationFragment registrationFragment = new RegistrationFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_Container_AM, registrationFragment, Constants.REGISTRATION_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
        registrationFragment.setEventHandler(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSupportFragmentManager().findFragmentById(R.id.fl_Container_AM) instanceof SignInFragment) {
            SignInFragment signInFragment =
                    (SignInFragment) getSupportFragmentManager().findFragmentById(R.id.fl_Container_AM);
            signInFragment.setEventHandler(this);
        } else {
            RegistrationFragment registrationFragment =
                    (RegistrationFragment) getSupportFragmentManager().findFragmentById(R.id.fl_Container_AM);
            registrationFragment.setEventHandler(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getSupportFragmentManager().findFragmentById(R.id.fl_Container_AM) instanceof SignInFragment) {
            SignInFragment signInFragment =
                    (SignInFragment) getSupportFragmentManager().findFragmentById(R.id.fl_Container_AM);
            signInFragment.releaseEventHandler();
        } else {
            RegistrationFragment registrationFragment =
                    (RegistrationFragment) getSupportFragmentManager().findFragmentById(R.id.fl_Container_AM);
            registrationFragment.releaseEventHandler();
        }
    }

    private void commitHeadlessFragment() {
        DataHeadlessFragment dataHeadlessFragment = new DataHeadlessFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(dataHeadlessFragment, "headless")
                .commit();
    }

    public DataHeadlessFragment getDataFragment () {
        return (DataHeadlessFragment) getSupportFragmentManager().findFragmentByTag("headless");
    }

    @Override
    public void openRegistrationFragment() {
        commitRegistrationFragment();
    }

    @Override
    public void registerPerson(String login, String password, String firstName, String lastName, String gender) {
        Person person = new Person();
        person.setLogin(login);
        person.setPassword(password);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setGender(gender);
        Toast.makeText(MainActivity.this, person.toString(), Toast.LENGTH_SHORT).show();
        getDataFragment().setPerson(person);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SignInFragment signInFragment =
                (SignInFragment) getSupportFragmentManager().findFragmentById(R.id.fl_Container_AM);
        signInFragment.setEventHandler(this);
    }
}
