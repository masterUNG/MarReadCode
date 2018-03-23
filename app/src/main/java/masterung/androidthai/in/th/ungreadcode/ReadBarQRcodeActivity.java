package masterung.androidthai.in.th.ungreadcode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import masterung.androidthai.in.th.ungreadcode.fragment.BarQRcodeFragment;

public class ReadBarQRcodeActivity extends AppCompatActivity {

    private String codeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_bar_qrcode);

        codeString = getIntent().getStringExtra("Code");
        Log.d("24MarchV1", "Code Result ==> " + codeString);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contentBarQRcodeFragment, BarQRcodeFragment.barQRcodeInstance(codeString))
                    .commit();
        }

    }
}
