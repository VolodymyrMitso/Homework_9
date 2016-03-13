package mitso.v.homework_9;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements EventHandler {

    public static ArrayList<Person> persons = new ArrayList<>();

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

    private void commitHeadlessFragment() {
        DataHeadlessFragment dataHeadlessFragment = new DataHeadlessFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(dataHeadlessFragment, Constants.HEADLESS_FRAGMENT_TAG)
                .commit();
    }

    public DataHeadlessFragment getDataFragment () {
        return (DataHeadlessFragment) getSupportFragmentManager().findFragmentByTag(Constants.HEADLESS_FRAGMENT_TAG);
    }

    @Override
    public void signIn(String _login, String _password, AlertDialog _alertDialog) {
        if (!persons.isEmpty()) {
            for (int i = 0; i < persons.size(); i++) {
                if (persons.get(i).getLogin().equals(_login) && persons.get(i).getPassword().equals(_password)) {
                    _alertDialog.setTitle("LOGIN SUCCESS");
                    _alertDialog.setMessage("WELCOME " + persons.get(i).getLogin());
                    _alertDialog.show();
                    break;
                } else {
                    _alertDialog.setTitle("LOGIN FAILURE");
                    _alertDialog.setMessage("USER " + _login + " DOESN'T EXIST OR PASSWORD IS WRONG");
                    _alertDialog.show();
                }
            }
        } else {
            if (_login.isEmpty()) {
                _alertDialog.setTitle("LOGIN FAILURE");
                _alertDialog.setMessage("USER DOESN'T EXIST");
            } else {
                _alertDialog.setTitle("LOGIN FAILURE");
                _alertDialog.setMessage("USER " + _login + " DOESN'T EXIST OR PASSWORD IS WRONG");
            }
            _alertDialog.show();
        }

        Toast.makeText(MainActivity.this, String.valueOf(persons.size()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openRegistrationFragment() {
        commitRegistrationFragment();
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

    @Override
    public void registerPerson(String _login, String _password, String _firstName, String _lastName, String _gender) {
        Person person = new Person();
        person.setLogin(_login);
        person.setPassword(_password);
        person.setFirstName(_firstName);
        person.setLastName(_lastName);
        person.setGender(_gender);
        persons.add(person);
        Toast.makeText(MainActivity.this, person.toString(), Toast.LENGTH_SHORT).show();
        getDataFragment().setPerson(person);

        Toast.makeText(MainActivity.this, String.valueOf(persons.size()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SignInFragment signInFragment =
                (SignInFragment) getSupportFragmentManager().findFragmentById(R.id.fl_Container_AM);
        signInFragment.setEventHandler(this);
    }
}