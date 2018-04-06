package masterung.androidthai.in.th.ungreadcode.utility;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.jibble.simpleftp.SimpleFTP;

import java.io.File;

public class UploadImageToServer extends AsyncTask<String, Void, String>{

    private Context context;

    public UploadImageToServer(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {

        try {

            MyConstant myConstant = new MyConstant();

            SimpleFTP simpleFTP = new SimpleFTP();
            simpleFTP.connect(myConstant.getHostString(),
                    myConstant.getPortAnInt(), myConstant.getUserString(), myConstant.getPasswordString());
            simpleFTP.bin();
            simpleFTP.cwd("Childrent");
            simpleFTP.stor(new File(strings[0]));
            simpleFTP.disconnect();

            return "true";

        } catch (Exception e) {
            Log.d("6AprilV1", "e at doIn ==> " + e.toString());
            return null;
        }


    }





}
