package org.telegram.android.fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import org.telegram.android.R;
import org.telegram.android.StelsFragment;
import org.telegram.android.core.model.User;

/**
 * Created with IntelliJ IDEA.
 * User: ex3ndr
 * Date: 24.09.13
 * Time: 3:18
 */
public class EditContactNameFragment extends StelsFragment {
    private int uid;
    private User user;

    private EditText firstName;
    private EditText lastName;

    public EditContactNameFragment(int uid) {
        this.uid = uid;
    }

    public EditContactNameFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View res = inflater.inflate(R.layout.settings_name, container, false);
        firstName = (EditText) res.findViewById(R.id.firstName);
        lastName = (EditText) res.findViewById(R.id.lastName);
        lastName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    onDone();
                    return true;
                }
                return false;
            }
        });
        if (user == null) {
            user = application.getEngine().getUser(uid);
        }
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        return res;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSherlockActivity().getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSherlockActivity().getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSherlockActivity().getSupportActionBar().setCustomView(R.layout.settings_name_bar);
        getSherlockActivity().getSupportActionBar().getCustomView().findViewById(R.id.dialogCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancel();
            }
        });
        getSherlockActivity().getSupportActionBar().getCustomView().findViewById(R.id.dialogDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDone();
            }
        });
        getSherlockActivity().getSupportActionBar().setSubtitle(null);
    }

    private void onCancel() {
        hideKeyboard(firstName);
        hideKeyboard(lastName);
        getActivity().onBackPressed();
    }

    private void onDone() {
        final String newFirstName = firstName.getText().toString().trim();
        final String newLastName = lastName.getText().toString().trim();

        if (newFirstName.length() == 0) {
            Toast.makeText(getActivity(), R.string.st_settings_name_empty_first, Toast.LENGTH_SHORT).show();
            return;
        }

        if (newLastName.length() == 0) {
            Toast.makeText(getActivity(), R.string.st_settings_name_empty_last, Toast.LENGTH_SHORT).show();
            return;
        }

        application.getContactsSource().editOrCreateUserContactName(uid, newFirstName, newLastName);

        Toast.makeText(getActivity(), R.string.st_contacts_edit_toast, Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }
}