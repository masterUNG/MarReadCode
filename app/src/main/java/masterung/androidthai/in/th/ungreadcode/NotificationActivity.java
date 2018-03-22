package masterung.androidthai.in.th.ungreadcode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NotificationActivity extends AppCompatActivity {

    private String[] messageStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        messageStrings = getIntent().getStringArrayExtra("Message");


    }
}
