package mitso.v.homework_9;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements EventHandler {

    private ArrayList<Person> persons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (savedInstanceState == null) {
            commitSignInFragment();
            commitHeadlessFragment();
        }
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
        MainSupport.signInSupport(this, persons, _login, _password, _alertDialog);
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
    public void registerPerson(String _login, String _password, String _firstName, String _lastName, String _gender) {
        RegistrationDialogFragment dialog = new RegistrationDialogFragment();
        Bundle args = new Bundle();

        if (MainSupport.personDataCheck(this, _login, _password, _firstName, _lastName, _gender)) {
            Person person = new Person();
            person.setLogin(_login);
            person.setPassword(_password);
            person.setFirstName(_firstName);
            person.setLastName(_lastName);
            person.setGender(_gender);

            persons.add(person);
            getDataFragment().setPersons(persons);

            args.putString(Constants.KEY_DIALOG_MESSAGE,
                            getResources().getString(R.string.s_dm_user_n) +
                            person.getFirstName() + " " + person.getLastName() +
                            getResources().getString(R.string.s_dm_n_registered));

        } else
            args.putString(Constants.KEY_DIALOG_MESSAGE,
                    getResources().getString(R.string.s_dm_emptyFields));

        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), Constants.DIALOG_FRAGMENT_TAG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getDataFragment().getPersons() != null)
            persons = getDataFragment().getPersons();

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
        getDataFragment().setPersons(persons);

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
    public void onBackPressed() {
        super.onBackPressed();
        SignInFragment signInFragment =
                (SignInFragment) getSupportFragmentManager().findFragmentById(R.id.fl_Container_AM);
        signInFragment.setEventHandler(this);
    }
}