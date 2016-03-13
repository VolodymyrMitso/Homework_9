package mitso.v.homework_9;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignInFragment extends Fragment {

    private TextView mTextView_Greeting;
    private EditText mEditText_Login;
    private EditText mEditText_Password;
    private TextView mTextView_Registration;
    private Button mButton_SignIn;

    private EventHandler mEventHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_in_fragment, container, false);

        mTextView_Greeting = (TextView) view.findViewById(R.id.tv_Greeting_SF);
        mEditText_Login = (EditText) view.findViewById(R.id.et_Login_SF);
        mEditText_Password = (EditText) view.findViewById(R.id.et_Password_FF);

        mTextView_Registration = (TextView) view.findViewById(R.id.tv_Registration_SF);
        mTextView_Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventHandler.openRegistrationFragment();
            }
        });

        mButton_SignIn = (Button) view.findViewById(R.id.btn_SignIn_SF);
        mButton_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventHandler.signIn(
                        mEditText_Login.getText().toString(),
                        mEditText_Password.getText().toString(),
                        getAlertDialog());
            }
        });

        return view;
    }

    private AlertDialog getAlertDialog() {
        AlertDialog alertDialog = new AlertDialog
                .Builder(getActivity())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < MainActivity.persons.size(); i++) {
                            Person person =  MainActivity.persons.get(i);
                            if (mEditText_Login.getText().toString().equals(person.getLogin()) && mEditText_Password.getText().toString().equals(person.getPassword())) {
                                String greeting;
                                if (person.getGender().equals("FEMALE"))
                                    greeting = "Hello MRS. " + person.getFirstName() + " " + person.getLastName();
                                else
                                    greeting = "Hello MR. " + person.getFirstName() + " " + person.getLastName();
                                mTextView_Greeting.setText(greeting);
                                break;
                            }
                        }
                        dialog.cancel();
                    }
                })
                .setCancelable(false)
                .create();
        return alertDialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        Person person = ((MainActivity) getActivity()).getDataFragment().getPerson();
        if (person != null) {
            String greeting;
            if (person.getGender().equals("FEMALE"))
                greeting = "Hello MRS. " + person.getFirstName() + " " + person.getLastName();
            else
                greeting = "Hello MR. " + person.getFirstName() + " " + person.getLastName();
            mTextView_Greeting.setText(greeting);
            mEditText_Login.setText(person.getLogin());
            mEditText_Password.setText(person.getPassword());
        }
    }

    public void setEventHandler(EventHandler eventHandler) {
        mEventHandler = eventHandler;
    }

    public void releaseEventHandler() {
        mEventHandler = null;
    }
}