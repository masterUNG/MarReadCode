package masterung.androidthai.in.th.ungreadcode.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import masterung.androidthai.in.th.ungreadcode.NotificationActivity;
import masterung.androidthai.in.th.ungreadcode.R;
import masterung.androidthai.in.th.ungreadcode.ServiceActivity;
import masterung.androidthai.in.th.ungreadcode.utility.AddChild;
import masterung.androidthai.in.th.ungreadcode.utility.ChangeStringToArray;
import masterung.androidthai.in.th.ungreadcode.utility.GetChildWhereIdUser;
import masterung.androidthai.in.th.ungreadcode.utility.GetMessageWhereCode;
import masterung.androidthai.in.th.ungreadcode.utility.MyConstant;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by masterung on 22/3/2018 AD.
 */

public class ShowChildFragment extends Fragment {

    private String[] loginStrings, messageStrings;
    private boolean statusABoolean = true;
    private ZXingScannerView zXingScannerView;
    private String resultFromReadQrString;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Get Value From SharePreference
        getValueFromSharePreference();

//        Create Toolbar
        createToolbar();

        createListView();

        myLoop();


    }   // Main Method

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void myLoop() {

//        To Do
        try {

            MyConstant myConstant = new MyConstant();
            GetChildWhereIdUser getChildWhereIdUser = new GetChildWhereIdUser(getActivity());
            getChildWhereIdUser.execute(loginStrings[0], myConstant.getUrlGetChildWhereIdUser());

            String jsonString = getChildWhereIdUser.get();
            Log.d("23MarchV1", "json form loop ==> " + jsonString);

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i=0; i<jsonArray.length(); i+=1) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("Status").equals("1")) {
                    statusABoolean = false;

                    String[] columnStrings = myConstant.getColumnMessageStrings();
                    String[] messageStrings = new String[columnStrings.length];

                    for (int i1=0; i1<columnStrings.length; i1+=1) {
                        messageStrings[i1] = jsonObject.getString(columnStrings[i1]);
                    }
                    myNoticication(messageStrings);
                    Log.d("23MarchV1", "Loop Stop");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (statusABoolean) {
                    myLoop();
                }
            }
        }, 1000);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void myNoticication(String[] messageStrings) {

        Log.d("23MarchV1", "Notification Work");

        Intent intent = new Intent(getActivity(), NotificationActivity.class);
        intent.putExtra("Message", messageStrings);

        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(),
                (int) System.currentTimeMillis(), intent, 0);

        Uri uri = RingtoneManager.getDefaultUri(Notification.DEFAULT_SOUND);

        Notification.Builder builder = new Notification.Builder(getActivity());
        builder.setTicker(getString(R.string.app_name));
        builder.setContentTitle(messageStrings[3] + " Have Message");
        builder.setContentText("Please Click Here");
        builder.setSmallIcon(R.drawable.ic_action_message);
        builder.setSound(uri);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notification.flags |= Notification.DEFAULT_SOUND | Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE;
        notificationManager.notify(0, notification);


    }

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
            return true;
        }

        if (item.getItemId() == R.id.itemReadQR) {
            readQRandBar();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void readQRandBar() {

        zXingScannerView = new ZXingScannerView(getActivity());
        getActivity().setContentView(zXingScannerView);
        zXingScannerView.setAutoFocus(true);
        zXingScannerView.startCamera();
        zXingScannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {

                zXingScannerView.stopCamera();
                resultFromReadQrString = result.getText().toString();

                Log.d("23MarchV2", "QR or Bar Code ==> " + resultFromReadQrString);

                try {

                    GetMessageWhereCode getMessageWhereCode = new GetMessageWhereCode(getActivity());
                    MyConstant myConstant = new MyConstant();
                    getMessageWhereCode.execute(resultFromReadQrString,
                            myConstant.getUrlGetMessageWhereCode());

                    String resultString = getMessageWhereCode.get();

                    JSONArray jsonArray = new JSONArray(resultString);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String[] column = myConstant.getColumnMessageStrings();
                    messageStrings = new String[column.length];
                    for (int i=0; i<column.length; i+=1) {
                        messageStrings[i] = jsonObject.getString(column[i]);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }



                String newDateString = findDate();
//                String newMessageString =
                addNameChild();


//                Restart Activity
                if (resultFromReadQrString.length() != 0) {
                    restartActivity();
                }


            }   // handleResult
        });

    }

    private String findDate() {

        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String dateString = dateFormat.format(calendar.getTime());

        ChangeStringToArray changeStringToArray = new ChangeStringToArray(getActivity());
        String[] strings = changeStringToArray.myChangeStringToArray(messageStrings[6]);
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (int i=0; i<strings.length; i+=1) {
            stringArrayList.add(strings[i]);
        }
        stringArrayList.add(dateString);

        return stringArrayList.toString();
    }

    private void restartActivity() {
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
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

            if (Boolean.parseBoolean(resultString)) {
                createListView();
            } else {
                Toast.makeText(getActivity(),
                        "Cannot Add Child Please Try Again",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createListView() {

        ListView listView = getView().findViewById(R.id.listviewChild);

        try {

            MyConstant myConstant = new MyConstant();
            GetChildWhereIdUser getChildWhereIdUser = new GetChildWhereIdUser(getActivity());
            getChildWhereIdUser.execute(loginStrings[0], myConstant.getUrlGetChildWhereIdUser());

            String jsonString = getChildWhereIdUser.get();
            Log.d("22MarchV1", "JSON ==> " + jsonString);

            JSONArray jsonArray = new JSONArray(jsonString);

            String[] nameChildStrings = new String[jsonArray.length()];
            for (int i=0; i<jsonArray.length(); i+=1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                nameChildStrings[i] = jsonObject.getString("NameChild");
            }

            ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, nameChildStrings);
            listView.setAdapter(stringArrayAdapter);

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
