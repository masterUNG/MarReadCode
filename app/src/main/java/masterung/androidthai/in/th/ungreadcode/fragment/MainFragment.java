package masterung.androidthai.in.th.ungreadcode.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import masterung.androidthai.in.th.ungreadcode.R;
import masterung.androidthai.in.th.ungreadcode.ServiceActivity;
import masterung.androidthai.in.th.ungreadcode.utility.GetAllUser;
import masterung.androidthai.in.th.ungreadcode.utility.MyAlert;
import masterung.androidthai.in.th.ungreadcode.utility.MyConstant;

/**
 * Created by masterung on 20/3/2018 AD.
 */

public class MainFragment extends Fragment{

    boolean aBoolean = false;   //false ==> Not Remember
    CheckBox checkBox;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        checkStatus();

//        Register Controller
        registerController();


//        Login Controller
        loginController();

//        CheckBox Controller
        checkBoxController();

    }   // Main Method

    private void checkStatus() {
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginFile", Context.MODE_PRIVATE);
            aBoolean = sharedPreferences.getBoolean("Status", false);
            Log.d("26MarchV1", "aBoolean From SharePerferences ==> " + aBoolean);
            if (aBoolean) {
                goToService();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkBoxController() {
        checkBox = getView().findViewById(R.id.checkboxRemember);
        if (checkBox.isChecked()) {
            aBoolean = true;
        }

    }

    private void loginController() {

        Button button = getView().findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText userEditText = getView().findViewById(R.id.edtUser);
                EditText passwordEditText = getView().findViewById(R.id.edtPassword);

                String userString = userEditText.getText().toString().trim();
                String passwordString = passwordEditText.getText().toString().trim();

                if (userString.isEmpty() || passwordString.isEmpty()) {
//                    Have Space
                    MyAlert myAlert = new MyAlert(getActivity());
                    myAlert.myDialog(getString(R.string.have_space),
                            getString(R.string.message_have_space));

                } else {
//                    No Space

                    try {

                        MyConstant myConstant = new MyConstant();
                        GetAllUser getAllUser = new GetAllUser(getActivity());
                        getAllUser.execute(myConstant.getUrlGetAllUserString());

                        String jsonString = getAllUser.get();
                        Log.d("22MarchV1", "JSON ==> " + jsonString);

                        String[] columnUserStrings = myConstant.getLoginStrings();
                        String[] loginStrings = new String[columnUserStrings.length];
                        boolean statusBool = true;

                        JSONArray jsonArray = new JSONArray(jsonString);

                        for (int i=0; i<jsonArray.length(); i+=1) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (userString.equals(jsonObject.getString(columnUserStrings[2]))) {

                                statusBool = false;
                                for (int i1=0; i1<columnUserStrings.length; i1+=1) {
                                    loginStrings[i1] = jsonObject.getString(columnUserStrings[i1]);
                                    Log.d("22MarchV1", "loginString[" + i1 + "] ==> " + loginStrings[i1]);
                                }


                            }   // if

                        }   // for

                        if (statusBool) {
//                            User False
                            MyAlert myAlert = new MyAlert(getActivity());
                            myAlert.myDialog("User False",
                                    "No This User in mySQL");
                        } else if (passwordString.equals(loginStrings[3])) {
//                            Password True
                            Toast.makeText(getActivity(), "Welcome " + loginStrings[1],
                                    Toast.LENGTH_SHORT).show();

                            saveToSharePreferance(loginStrings);

                            goToService();

                        } else {
//                            Password False
                            MyAlert myAlert = new MyAlert(getActivity());
                            myAlert.myDialog("Password False",
                                    "Please Try Again Password False");

                        }





                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                }   // if


            }   // onClick
        });


    }

    private void goToService() {
        Intent intent = new Intent(getActivity(), ServiceActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void saveToSharePreferance(String[] loginStrings) {

        ArrayList<String> stringArrayList = new ArrayList<>();
        for (int i=0; i<loginStrings.length; i+=1) {
            stringArrayList.add(loginStrings[i]);
        }
        Log.d("22MarchV1", "String Save SharePrefer ==> " + stringArrayList.toString());

        if (checkBox.isChecked()) {
            aBoolean = true;
        }
        Log.d("26MarchV1", "aBoolean Before Save Prefer ==> " + aBoolean);


        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences("LoginFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Login", stringArrayList.toString());
        editor.putBoolean("Status", aBoolean);
        editor.commit();


    }

    private void registerController() {
        TextView textView = getView().findViewById(R.id.txtRegister);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Replace Fragment
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentMainFragment, new RegisterFragment())
                        .addToBackStack(null)
                        .commit();


            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        return view;
    }
}   // Main Class
