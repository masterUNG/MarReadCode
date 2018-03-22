package masterung.androidthai.in.th.ungreadcode.utility;

import android.content.Context;

/**
 * Created by masterung on 22/3/2018 AD.
 */

public class ChangeStringToArray {

    private Context context;

    public ChangeStringToArray(Context context) {
        this.context = context;
    }

    public String[] myChangeStringToArray(String string) {

        String myString = string;
        myString = myString.substring(1, myString.length() - 1);
        String[] strings = myString.split(",");

        return strings;
    }

}
