package com.apps.contactlistapp.view.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.contactlistapp.R;
import com.apps.contactlistapp.databinding.ActivityMainBinding;
import com.apps.contactlistapp.databinding.ContactEditLayoutBinding;
import com.apps.contactlistapp.databinding.ContactUpdateLayoutBinding;
import com.apps.contactlistapp.model.ContactModel;
import com.apps.contactlistapp.view.classes.BaseActivity;
import com.apps.contactlistapp.view.classes.Constant;
import com.apps.contactlistapp.view.classes.SharePreferenceConstant;
import com.apps.contactlistapp.view.classes.Utils;
import com.apps.contactlistapp.view.database.DBHelper;
import com.apps.contactlistapp.view.ui.adapters.RecylcerContact;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends BaseActivity {


    private ActivityMainBinding binding;
    private ArrayList<ContactModel> contactModelArrayList = new ArrayList<>();
    private RecylcerContact mAdapter;


    public static int height, width;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;


        binding.userName.setText("" + getPreference(SharePreferenceConstant.USER_NUMER));
        binding.logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                MainActivity.this.finish();
                clearAllPreference();
                clearLoginPreference();
            }
        });

        dbHelper = new DBHelper(MainActivity.this);
        try {
            dbHelper.createDataBase();
            Log.d("Database", "Database created");
        } catch (IOException e) {
            e.printStackTrace();
        }

        contactModelArrayList.clear();

        binding.contactRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        binding.contactRecyclerView.setHasFixedSize(true);

        ShowSQLiteDBdata();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContact();
            }
        });


    }

    public static Dialog dialog;
    ContactEditLayoutBinding layoutBinding;

    private void showContact() {
        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        layoutBinding = ContactEditLayoutBinding.inflate(getLayoutInflater());
        dialog.setContentView(layoutBinding.getRoot());
        dialog.getWindow().setLayout(width - width / 8, height - height / 4);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        layoutBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        layoutBinding.capBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission();


            }
        });


        layoutBinding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    boolean checkInsert =  dbHelper.saveToContactList(generateRandomNumber(),layoutBinding.cName.getText().toString().trim(),
                            layoutBinding.cNumber.getText().toString().trim(), layoutBinding.cEmail.getText().toString().trim(),
                            ephoto);
                    if(checkInsert==true)
                    {
                        ShowSQLiteDBdata();
                        showToastMessage("New Contact Inserted");
                    }else{
                        showToastMessage("New Contact Not Inserted");
                    }


                    dialog.dismiss();
                }
            }
        });


        dialog.show();
    }
    private String generateRandomNumber() {
        Random r = new Random();
        int randomNumber = r.nextInt(100);
        String date_time = getDateTime();
        String form_id = randomNumber + "-" + date_time;
        return form_id;
    }

    private boolean validation() {

        if (bitmap == null) {
            Toast.makeText(getApplicationContext(), "image missing", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(layoutBinding.cName.getText().toString())) {
            layoutBinding.cName.requestFocus();
            Utils.showToastMessage(MainActivity.this, "Please Enter Last Name");
            return false;
        }
        if (!Utils.validatePhno(getApplicationContext(), layoutBinding.cNumber)) {
            return false;
        }
        if (TextUtils.isEmpty(layoutBinding.cEmail.getText().toString()) || !isValidEmail(layoutBinding.cEmail.getText().toString().trim())) {
            layoutBinding.cEmail.requestFocus();
            Utils.showToastMessage(MainActivity.this, "Please enter a valid email address");
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    private void selectImage() {

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        TextView title = new TextView(MainActivity.this);
        title.setText("Add Photo!");
        title.setBackgroundColor(Color.BLACK);
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(22);


        AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.this);


        builder.setCustomTitle(title);

        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Take Photo")) {
                    CapturePictureIntent();
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select Picture"),
                            PICK_IMAGE_GALLERY);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private int camera_ReqCode = 220;
    private int PICK_IMAGE_GALLERY = 240;

    private void CapturePictureIntent() {
        if (isDeviceSupportCamera()) {
            Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camIntent, camera_ReqCode);
        } else {
            Toast.makeText(getApplicationContext(), "Device Not supports Camera", Toast.LENGTH_LONG).show();

        }
    }

    private boolean isDeviceSupportCamera() {

        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);

    }

    private void requestCameraPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                            selectImage();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    private void ShowSQLiteDBdata() {
        contactModelArrayList.clear();
        contactModelArrayList = dbHelper.getContactDetail(Constant.TABLE_CONTACT);
        mAdapter = new RecylcerContact(MainActivity.this, contactModelArrayList, this);
        binding.contactRecyclerView.setAdapter(mAdapter);
        Log.e("msg", "" + contactModelArrayList.size());
        if (contactModelArrayList.size() > 0) {
            mAdapter.notifyDataSetChanged();
        }

        bitmap = null;
        ephoto = null;

    }

    String ephoto = null;
    Bitmap bitmap;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == camera_ReqCode) {
            if (resultCode == RESULT_OK) {
                bitmap = (Bitmap) data.getExtras().get("data");

                ephoto = BitMapToString(bitmap);
                if (bitmap != null) {
                    layoutBinding.image1.setImageBitmap(bitmap);
                }

            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                ephoto = BitMapToString(bitmap);

                layoutBinding.image1.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public void callFunction(int position) {
        Intent helpIntent = new Intent(Intent.ACTION_DIAL);
        helpIntent.setData(Uri.parse("tel:" + (contactModelArrayList.get(position).getC_number().replaceAll("[\\s\\-()]", ""))));
        startActivity(helpIntent);
    }

    public void msgFunction(int position) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + Uri.encode(contactModelArrayList.get(position).getC_number())));
        startActivity(intent);
    }

    public void deletFunction(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(R.drawable.ic_delete);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to delete this contact?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dbHelper.deleteRecord(Constant.TABLE_CONTACT, contactModelArrayList.get(position).getC_number());
                ShowSQLiteDBdata();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }


    public void editFunction(int position) {
        showContactEdit(position);

    }

    public static Dialog dialog1;
    ContactUpdateLayoutBinding updateLayoutBinding;

    private void showContactEdit(int position) {


        dialog1 = new Dialog(MainActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);
        updateLayoutBinding = ContactUpdateLayoutBinding.inflate(getLayoutInflater());
        dialog1.setContentView(updateLayoutBinding.getRoot());
        dialog1.getWindow().setLayout(width - width / 8, height - height / 4);

        updateLayoutBinding.cEmail.setText("" + contactModelArrayList.get(position).getC_email());
        updateLayoutBinding.cNumber.setText("" + contactModelArrayList.get(position).getC_number());
        updateLayoutBinding.cName.setText("" + contactModelArrayList.get(position).getC_name());
        updateLayoutBinding.image1.setImageBitmap(StringToBitMap(contactModelArrayList.get(position).getC_image()));
        bitmap = StringToBitMap(contactModelArrayList.get(position).getC_image());
        ephoto = BitMapToString(bitmap);

        updateLayoutBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        updateLayoutBinding.capBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission();
            }
        });


        updateLayoutBinding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (validation1()) {
                    boolean checkUpdate =  dbHelper.upDatedData(Constant.TABLE_CONTACT,updateLayoutBinding.cName.getText().toString().trim(),
                            updateLayoutBinding.cNumber.getText().toString().trim(), updateLayoutBinding.cEmail.getText().toString().trim(),
                            ephoto);
                    if(checkUpdate)
                    {
                        ShowSQLiteDBdata();
                        showToastMessage("Entry updated");
                    }else{

                        showToastMessage("We can not updated mobile number");
                    }
                    dialog1.dismiss();
                }

            }
        });


        dialog1.show();
        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


    }

    private boolean validation1() {

        if (bitmap == null) {
            Toast.makeText(getApplicationContext(), "image missing", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(updateLayoutBinding.cName.getText().toString())) {
            updateLayoutBinding.cName.requestFocus();
            Utils.showToastMessage(MainActivity.this, "Please Enter Last Name");
            return false;
        }
        if (!Utils.validatePhno(getApplicationContext(), updateLayoutBinding.cNumber)) {
            return false;
        }
        if (TextUtils.isEmpty(updateLayoutBinding.cEmail.getText().toString()) || !isValidEmail(updateLayoutBinding.cEmail.getText().toString().trim())) {
            updateLayoutBinding.cEmail.requestFocus();
            Utils.showToastMessage(MainActivity.this, "Please enter a valid email address");
            return false;
        }

        return true;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}