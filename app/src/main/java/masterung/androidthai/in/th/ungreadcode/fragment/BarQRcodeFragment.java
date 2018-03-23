package masterung.androidthai.in.th.ungreadcode.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import masterung.androidthai.in.th.ungreadcode.R;
import masterung.androidthai.in.th.ungreadcode.ServiceActivity;
import masterung.androidthai.in.th.ungreadcode.utility.ChangeStringToArray;
import masterung.androidthai.in.th.ungreadcode.utility.EditMessageWhereCode;
import masterung.androidthai.in.th.ungreadcode.utility.GetMessageWhereCode;
import masterung.androidthai.in.th.ungreadcode.utility.MyAlert;
import masterung.androidthai.in.th.ungreadcode.utility.MyConstant;

/**
 * Created by masterung on 24/3/2018 AD.
 */

public class BarQRcodeFragment extends Fragment {

    private String codeString, currentTimeString, messageString;
    private String[] messageStrings;


    public static BarQRcodeFragment barQRcodeInstance(String codeString) {
        BarQRcodeFragment barQRcodeFragment = new BarQRcodeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Code", codeString);
        barQRcodeFragment.setArguments(bundle);
        return barQRcodeFragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        codeString = getArguments().getString("Code");

//        Get Value Message
        getValueMessage();

//        Show Current Time
        showCurrentTime();

//        Show Name Child
        showNameChild();

//        Sent Message Controller
        sentMessageController();


    }   // Main Method

    private void sentMessageController() {
        Button button = getView().findViewById(R.id.btnSentMessage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editText = getView().findViewById(R.id.edtMessage);
                messageString = editText.getText().toString().trim();

                if (messageString.isEmpty()) {
                    MyAlert myAlert = new MyAlert(getActivity());
                    myAlert.myDialog(getString(R.string.have_space), getString(R.string.message_have_space));
                } else {

                    ChangeStringToArray changeStringToArray = new ChangeStringToArray(getActivity());

//                    for DateString
                    String[] dateStrings = changeStringToArray.myChangeStringToArray(messageStrings[6]);
                    ArrayList<String> dateStringArrayList = new ArrayList<>();
                    for (int i=0; i<dateStrings.length; i+=1) {
                        dateStringArrayList.add(dateStrings[i]);
                    }
                    dateStringArrayList.add(currentTimeString);
                    currentTimeString = dateStringArrayList.toString();

//                    for MessageString
                    String[] oldMessageStrings = changeStringToArray.myChangeStringToArray(messageStrings[7]);
                    ArrayList<String> messageStringArrayList = new ArrayList<>();
                    for (int i=0; i<oldMessageStrings.length; i+=1) {
                        messageStringArrayList.add(oldMessageStrings[i]);
                    }
                    messageStringArrayList.add(messageString);
                    messageString = messageStringArrayList.toString();

                    try {

                        MyConstant myConstant = new MyConstant();
                        EditMessageWhereCode editMessageWhereCode = new EditMessageWhereCode(getActivity());
                        editMessageWhereCode.execute(codeString, currentTimeString, messageString, myConstant.getUrlEditMessageWhereCode());

                        if (Boolean.parseBoolean(editMessageWhereCode.get())) {

                            Intent intent = new Intent(getActivity(), ServiceActivity.class);
                            startActivity(intent);
                            getActivity().finish();

                        } else {
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                } // if


            }
        });
    }

    private void showNameChild() {
        TextView textView = getView().findViewById(R.id.txtNameChild);

        try {

            MyConstant myConstant = new MyConstant();
            GetMessageWhereCode getMessageWhereCode = new GetMessageWhereCode(getActivity());
            getMessageWhereCode.execute(codeString, myConstant.getUrlGetMessageWhereCode());

            String jsonString = getMessageWhereCode.get();
            Log.d("24MarchV1", "json on BarQRcodeFragement ==> " + jsonString);

            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            String[] columnMessage = myConstant.getColumnMessageStrings();
            messageStrings = new String[columnMessage.length];
            for (int i=0; i<columnMessage.length; i+=1) {
                messageStrings[i] = jsonObject.getString(columnMessage[i]);
            }

            textView.setText(messageStrings[3]);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showCurrentTime() {
        TextView textView = getView().findViewById(R.id.txtCurrent);
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        currentTimeString = dateFormat.format(calendar.getTime());
        textView.setText(currentTimeString);
    }

    private void getValueMessage() {
        try {

            MyConstant myConstant = new MyConstant();
            GetMessageWhereCode getMessageWhereCode = new GetMessageWhereCode(getActivity());
            getMessageWhereCode.execute(codeString, myConstant.getUrlGetMessageWhereCode());
            String jsonString = getMessageWhereCode.get();
            Log.d("24MarchV1", "JSON => " + jsonString);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar_qr_code, container, false);
        return view;
    }
}
