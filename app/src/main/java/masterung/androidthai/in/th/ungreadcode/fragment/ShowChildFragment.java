package masterung.androidthai.in.th.ungreadcode.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import masterung.androidthai.in.th.ungreadcode.R;
import masterung.androidthai.in.th.ungreadcode.ServiceActivity;
import masterung.androidthai.in.th.ungreadcode.utility.AddChild;
import masterung.androidthai.in.th.ungreadcode.utility.ChangeStringToArray;
import masterung.androidthai.in.th.ungreadcode.utility.MyConstant;

/**
 * Created by masterung on 22/3/2018 AD.
 */

public class ShowChildFragment extends Fragment {

    private String[] loginStrings;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Get Value From SharePreference
        getValueFromSharePreference();

//        Create Toolbar
        createToolbar();


    }   // Main Method

    private void getValueFromSharePreference() {
        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences("LoginFile", Context.MODE_PRIVATE);
        String resultString = sharedPreferences.getString("Login", null);
        Log.d("22MarchV1", "String from SharePrefer ==> " + resultString);

        ChangeStringToArray changeStringToArray = new ChangeStringToArray(getActivity());
        loginStrings = changeStringToArray.myChangeStringToArray(resultString);

    }

    private void createToolbar() {
        Toolbar toolbar = getView().findViewById(R.id.toolbarShowChild);
        ((ServiceActivity) getActivity()).setSupportActionBar(toolbar);
        ((ServiceActivity) getActivity()).getSupportActionBar()
                .setTitle(getString(R.string.title_show_child));
        ((ServiceActivity) getActivity()).getSupportActionBar().setSubtitle(loginStrings[1] + " login");
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.itemAddChild) {
            addNameChild();
        }

        return super.onOptionsItemSelected(item);
    }

    private void addNameChild() {

        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        final String dateString = "[" + dateFormat.format(calendar.getTime()) + "]";

        Random random = new Random();
        int i = random.nextInt(10000);
        final String codeString = "Code" + Integer.toString(i);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_action_name);
        builder.setTitle("Please Fill Name Child");


        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.edittext_add_child, null);
        builder.setView(view);

        builder.setMessage("Father ==> " + loginStrings[1] + "\n" + codeString + "\n" + dateString);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText editText = view.findViewById(R.id.edtNameChild);
                String nameChildString = editText.getText().toString().trim();

                saveValueToServer(loginStrings[0], codeString, nameChildString, dateString);

                dialog.dismiss();
            }
        });

        builder.show();

    }

    private void saveValueToServer(String loginString,
                                   String codeString,
                                   String nameChildString,
                                   String dateString) {

        try {

            MyConstant myConstant = new MyConstant();
            AddChild addChild = new AddChild(getActivity());
            addChild.execute(loginString, codeString, nameChildString,
                    dateString, myConstant.getUrlAddChildString());

            String resultString = addChild.get();
            Log.d("22MarchV1", "result from AddChild ==> " + resultString);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_show_child, menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_showchild, container, false);
        return view;
    }
}
