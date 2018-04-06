package masterung.androidthai.in.th.ungreadcode.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import masterung.androidthai.in.th.ungreadcode.R;
import masterung.androidthai.in.th.ungreadcode.ServiceActivity;
import masterung.androidthai.in.th.ungreadcode.utility.MyAlert;
import masterung.androidthai.in.th.ungreadcode.utility.MyConstant;
import masterung.androidthai.in.th.ungreadcode.utility.UploadImageToServer;

public class AddChildFragment extends Fragment{

    private Uri uri;
    private ImageView imageView;
    private boolean aBoolean = true;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Create Toolbar
        createToolbar();

//        Image Controller
        imageController();


    }   // Main Method

    private void imageController() {
        imageView = getView().findViewById(R.id.imageViewAvata);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("3AprilV1", "Click Image");

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Please Choose Image"), 1);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {

            aBoolean = false;

            try {

                uri = data.getData();
                Bitmap bitmap = BitmapFactory
                        .decodeStream(getActivity().getContentResolver().openInputStream(uri));
                imageView.setImageBitmap(bitmap);



            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add_child, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.itemAddChild) {
            uploadValueToServer();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void uploadValueToServer() {

        EditText editText = getView().findViewById(R.id.edtNameChild);
        String nameChildString = editText.getText().toString();
        MyAlert myAlert = new MyAlert(getActivity());

        if (aBoolean) {
            myAlert.myDialog("Not Choose", "Please Choose Image");
        } else if (nameChildString.isEmpty()) {
            myAlert.myDialog(getString(R.string.have_space),
                    getString(R.string.message_have_space));
        } else {

            try {

                String pathString = null;
                String[] strings = new String[]{MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(uri, strings,
                        null, null, null);
                if (cursor != null) {

                    cursor.moveToFirst();
                    int indexInt = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    pathString = cursor.getString(indexInt);

                } else {
                    pathString = uri.getPath();
                }

                Log.d("6AprilV1", "Path Image ==> " + pathString);

                UploadImageToServer uploadImageToServer = new UploadImageToServer(getActivity());
                uploadImageToServer.execute(pathString);

                Log.d("6AprilV1", "Result ==> " + uploadImageToServer.get());



            } catch (Exception e) {

                e.printStackTrace();
            }

        }   // if



    }

    private void createToolbar() {

        setHasOptionsMenu(true);

        Toolbar toolbar = getView().findViewById(R.id.toolbarAddChild);
        ((ServiceActivity)getActivity()).setSupportActionBar(toolbar);

        ((ServiceActivity) getActivity()).getSupportActionBar().setTitle("Add Your Child");
        ((ServiceActivity) getActivity()).getSupportActionBar().setSubtitle("Choose Image and Fill Name");

        ((ServiceActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((ServiceActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_child, container, false);
        return view;
    }
}
