package masterung.androidthai.in.th.ungreadcode.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import masterung.androidthai.in.th.ungreadcode.NotificationActivity;
import masterung.androidthai.in.th.ungreadcode.R;
import masterung.androidthai.in.th.ungreadcode.utility.ChangeStringToArray;

/**
 * Created by masterung on 23/3/2018 AD.
 */

public class ShowNotiFragment extends Fragment{

    private String[] messageStrings, loginStrings;

    public static ShowNotiFragment showNotiInstance(String[] messageStrings) {

        ShowNotiFragment showNotiFragment = new ShowNotiFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray("Message", messageStrings);
        showNotiFragment.setArguments(bundle);
        return showNotiFragment;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Get Value From Argument
        messageStrings = getArguments().getStringArray("Message");

//        Get Value From SherePreferance
        getValueFromSharePreference();


//        Create Toolbar
        createToolbar();

//        Create ListView
        createListView();

    }   // Main Class

    private void getValueFromSharePreference() {

        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences("LoginFile", Context.MODE_PRIVATE);

        String resultString = sharedPreferences.getString("Login", null);
        Log.d("23MarchV1", "result form Prefer ==> " + resultString);

        ChangeStringToArray changeStringToArray = new ChangeStringToArray(getActivity());
        loginStrings = changeStringToArray.myChangeStringToArray(resultString);


    }

    private void createListView() {

        ListView listView = getView().findViewById(R.id.listViewMessage);
        ChangeStringToArray changeStringToArray = new ChangeStringToArray(getActivity());
        String[] dateStrings = changeStringToArray.myChangeStringToArray(messageStrings[6]);
        String[] newsStrings = changeStringToArray.myChangeStringToArray(messageStrings[7]);

        String[] contentStrings = new String[dateStrings.length];
        for (int i=0; i<dateStrings.length; i+=1) {
            contentStrings[i] = dateStrings[i] + "\n" + newsStrings[i];
        }


        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, contentStrings);
        listView.setAdapter(stringArrayAdapter);

    }

    private void createToolbar() {
        Toolbar toolbar = getView().findViewById(R.id.toolbarShowNoti);
        ((NotificationActivity) getActivity()).setSupportActionBar(toolbar);

        ((NotificationActivity) getActivity())
                .getSupportActionBar()
                .setTitle(messageStrings[3]);

        ((NotificationActivity) getActivity()).getSupportActionBar().setSubtitle(loginStrings[1] + " Parent");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_notification,
                container, false);
        return view;
    }
}   // Main Class
