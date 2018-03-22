package masterung.androidthai.in.th.ungreadcode.utility;

/**
 * Created by masterung on 22/3/2018 AD.
 */

public class MyConstant {

    //    About URL
    private String urlAddChildString = "http://androidthai.in.th/mar/AddChild.php";
    private String urlGetAllUserString = "http://androidthai.in.th/mar/getAllUser.php";
    private String urlPostUserString = "http://androidthai.in.th/mar/postUser.php";

    //    About Array
    private String[] loginStrings = new String[]{"id", "Name", "User", "Password"};


    public String getUrlAddChildString() {
        return urlAddChildString;
    }

    public String[] getLoginStrings() {
        return loginStrings;
    }

    public String getUrlGetAllUserString() {
        return urlGetAllUserString;
    }

    public String getUrlPostUserString() {
        return urlPostUserString;
    }
}   // Main Class
