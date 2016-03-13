package mitso.v.homework_9;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DataHeadlessFragment extends Fragment {

    private Person person;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null)
            restoreState(savedInstanceState);
        setRetainInstance(true);
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Constants.KEY_PERSON, person);
    }

    private void restoreState(Bundle savedInstanceState) {
        person = (Person) savedInstanceState.getSerializable(Constants.KEY_PERSON);
    }


    public Person getPerson() {
        return person;
    }

    public void setPerson(Person _person) {
        person = _person;
    }
}
