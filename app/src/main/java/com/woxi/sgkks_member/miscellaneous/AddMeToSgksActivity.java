package com.woxi.sgkks_member.miscellaneous;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.sgkks_member.AppController;
import com.woxi.sgkks_member.R;
import com.woxi.sgkks_member.home.HomeActivity;
import com.woxi.sgkks_member.home.SelectCityActivity;
import com.woxi.sgkks_member.interfaces.AppConstants;
import com.woxi.sgkks_member.models.BloodGroupItems;
import com.woxi.sgkks_member.models.MemberDetailsItem;
import com.woxi.sgkks_member.utils.AppCommonMethods;
import com.woxi.sgkks_member.utils.AppParser;
import com.woxi.sgkks_member.utils.AppURLs;
import com.woxi.sgkks_member.utils.ImageUtilityHelper;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * <b>public class AddMeToSgksActivity extends AppCompatActivity implements AppConstants</b>
 * <p>This class is used for Add Me To Sgks page</p>
 * Created by Rohit.
 */
public class AddMeToSgksActivity extends AppCompatActivity implements AppConstants, View.OnClickListener {
    private Context mContext;
    private EditText metFirstName;
    private EditText metLastName;
    private EditText metMiddleName;
    private EditText metContact;
    private EditText metEmail;
    private EditText metAddress;
    private TextView tvDob;
    private TextView tvSelectCity;
    private Spinner spBloodGroup;
    private String TAG = "AddMeToSgksActivity";
    private String strFirstName;
    private String strLastName;
    private String strMiddleName;
    private String strContact ="7709307123";
    private String strGender;
    private String strEmail;
    private String strAddress;
    private String strDateOfBirth;
    private String strImageName;
    private String strActivityType;
    private String strCityName = "";
    private String strCityId;
    private String strBloodGroupId;
    private RadioButton rbGender;
    private Calendar calendar;
    private ImageView ivProfilePicture;
    private ImageView ivAddImage;
    private ImageUtilityHelper imageUtilityHelper;
    private Bitmap bitmapProfile;
    private RadioGroup rgGender;
    private ArrayAdapter<String> arrBloodGroupAdapter;
    private Bundle bundle;
    private JSONArray jsonArrayBloodGroup;
    private ArrayList<String> bloodGroupArrayList;
    private MemberDetailsItem memberDetailsItem;
    private Boolean isFromEdit = false;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private ArrayList <BloodGroupItems>arrbloodGroupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_me_to_sgks);

        bundle = getIntent().getExtras();
        if(bundle != null){
            if(bundle.containsKey("memberItems")){
                memberDetailsItem = (MemberDetailsItem) bundle.getSerializable("memberItems");
            }
            if (bundle.containsKey("activityType")) {
                strActivityType = bundle.getString("activityType");
                if(strActivityType.equalsIgnoreCase("EditProfile")){
                    isFromEdit = true;
                }
            }
            if(bundle.containsKey("contactNumber")){
                strContact = bundle.getString("contactNumber");
            }
        }
        if (getSupportActionBar() != null) {
            if(isFromEdit){
                getSupportActionBar().setTitle("Edit Profile");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } else {
                getSupportActionBar().setTitle(R.string.add_me_sgks);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

        }
        initializeViews();
        requestBloodGroup();
        genderCheckBox();
        bloodGropSpinnerListener();
        dobListener();
        validateFieldsAndApiCall();
    }

    private void initializeViews() {
        mContext = AddMeToSgksActivity.this;
        metFirstName = findViewById(R.id.etFirstName);
        metMiddleName = findViewById(R.id.etMiddleName);
        metLastName = findViewById(R.id.etLastName);
        metContact = findViewById(R.id.etContact);
        metEmail = findViewById(R.id.etEmail);
        metAddress = findViewById(R.id.etAddress);
        spBloodGroup = findViewById(R.id.spBloodGroup);
        tvDob = findViewById(R.id.tvDob);
        tvSelectCity = findViewById(R.id.tvSelectCity);
        tvSelectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectCityActivity.class);
                intent.putExtra("isFromCreateMember",true);
                startActivityForResult(intent,AppConstants.CITY_SELECT_CODE);
            }
        });
        ivProfilePicture = findViewById(R.id.ivProfileImage);
        ivAddImage = findViewById(R.id.ivAddImage);
        rgGender = findViewById(R.id.rgGender);
        metFirstName.requestFocus();
        imageUtilityHelper = new ImageUtilityHelper(mContext);
        ivAddImage.setOnClickListener(AddMeToSgksActivity.this);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        if(isFromEdit){
            metFirstName.setText(memberDetailsItem.getStrFirstName());
            metMiddleName.setText(memberDetailsItem.getStrMiddleName());
            metLastName.setText(memberDetailsItem.getStrLastName());
            metEmail.setText(memberDetailsItem.getStrEmail());
            metContact.setText(memberDetailsItem.getStrMobileNumber());
            metContact.setEnabled(false);
            tvDob.setText(memberDetailsItem.getStrDateOfBirth());
            ivProfilePicture.setBackground(Drawable.createFromPath(memberDetailsItem.getStrMemberImageUrl()));
            if(memberDetailsItem.getStrGender().equalsIgnoreCase("Male")){
                rbMale.setChecked(true);
            } else if(memberDetailsItem.getStrGender().equalsIgnoreCase("Female")){
                rbFemale.setChecked(true);
            }
            if(memberDetailsItem.getStrBloodGroup().equalsIgnoreCase("A")){
                spBloodGroup.setSelection(1);
            } else if(memberDetailsItem.getStrBloodGroup().equalsIgnoreCase("A-")){
                spBloodGroup.setSelection(2);
            } else if(memberDetailsItem.getStrBloodGroup().equalsIgnoreCase("B")){
                spBloodGroup.setSelection(4);
            } else if(memberDetailsItem.getStrBloodGroup().equalsIgnoreCase("B-")){
                spBloodGroup.setSelection(4);
            } else if(memberDetailsItem.getStrBloodGroup().equalsIgnoreCase("O-")){
                spBloodGroup.setSelection(5);
            } else if(memberDetailsItem.getStrBloodGroup().equalsIgnoreCase("O")){
                spBloodGroup.setSelection(6);
            } else if(memberDetailsItem.getStrBloodGroup().equalsIgnoreCase("AB-")){
                spBloodGroup.setSelection(7);
            } else if(memberDetailsItem.getStrBloodGroup().equalsIgnoreCase("AB")){
                spBloodGroup.setSelection(8);
            }
            metAddress.setText(memberDetailsItem.getStrAddress());
        } else {
            metContact.setText(strContact);
            metContact.setEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestBloodGroup() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppURLs.API_GET_BLOOD_GROUP, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    new AppCommonMethods(mContext).LOG(0, "get_blood_group", response.toString());
                    Object resp = AppParser.parseBloodGroupResponse(response.toString());
                    arrbloodGroupList = (ArrayList<BloodGroupItems>) resp;
                    jsonArrayBloodGroup = response.getJSONArray("data");
                    bloodGroupArrayList = new ArrayList<>();
                    //  bloodGroupArrayList.add(0, "Choose One");
                    for (int i = 0; i < jsonArrayBloodGroup.length(); i++) {
                        JSONObject jsonObject = jsonArrayBloodGroup.getJSONObject(i);
                        bloodGroupArrayList.add(jsonObject.getString("blood_group"));
                    }
                    arrBloodGroupAdapter = new ArrayAdapter<String>(AddMeToSgksActivity.this, android.R.layout.simple_spinner_item, bloodGroupArrayList);
                    spBloodGroup.setAdapter(arrBloodGroupAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new AppCommonMethods(mContext).LOG(0, "error_blood_group", error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "get_blood_group");
    }

    private void sendImageToServerRequest() {
        String strImageBase64 = convertImageToBase64(bitmapProfile,100);
        JSONObject params = new JSONObject();
        try {
            params.put("image_for", "profile_img");
            params.put("image", strImageBase64);
            params.put("extension","png");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppURLs.API_UPLOAD_IMAGE, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            new AppCommonMethods(mContext).LOG(0, "image_uploaded", response.toString());
                            if (!response.getString("filename").isEmpty()) {
                                strImageName = response.getString("filename").toString();
                                ivProfilePicture.setImageBitmap(bitmapProfile);
                            } else {
                                Toast.makeText(mContext, "Image Not Uploaded", Toast.LENGTH_SHORT);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, "Image Not Uploaded", Toast.LENGTH_SHORT);
                        new AppCommonMethods(mContext).LOG(0, "error_image_uploaded", error.toString());

                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "upload_image");

    }

    private void requestAddMember() {
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading, Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        JSONObject params = new JSONObject();
        try {
            params.put("first_name", strFirstName);
            params.put("middle_name", strMiddleName);
            params.put("last_name", strLastName);
            params.put("gender", strGender);
            params.put("address", strAddress);
            params.put("date_of_birth", strDateOfBirth); // Date format is YYYY-MM-DD
            params.put("blood_group_id", strBloodGroupId);
            params.put("city_id", strCityId);
            params.put("mobile", strContact);
            params.put("email", strEmail);
            params.put("profile_images", strImageName);
            Log.i(TAG, "requestAddMember: "+params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppURLs.API_ADD_MEMBER, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            pDialog.hide();
                            new AppCommonMethods(mContext).LOG(0, "member_added", response.toString());
                            Toast.makeText(mContext,response.get("message").toString(),Toast.LENGTH_SHORT);
                            goToHomeScreen();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        new AppCommonMethods(mContext).LOG(0, "error_member_added", error.toString());
                        Toast.makeText(mContext, "Failed. Please Try Again", Toast.LENGTH_LONG);
                    }
                });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "add_new_member");
    }

    private void getImageChooser() {
        //Step 5. Call image chooser function to get app list.
        Intent imageChooserIntent = imageUtilityHelper.getPickImageChooserIntent();
        startActivityForResult(imageChooserIntent, IMAGE_CHOOSER_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case AppConstants.CITY_SELECT_CODE:
                    strCityName = data.getStringExtra("cityName");
                    strCityId = data.getStringExtra("cityId");
                    tvSelectCity.setText(strCityName);
                    break;
                case AppConstants.USER_LOGIN_ACTIVITY_RESULT_CODE:
                    //setProfileData();
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE:
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                case AddMeToSgksActivity.IMAGE_CHOOSER_CODE:
//                    For new camera functionality
                    imageUtilityHelper.onSelectionResult(requestCode, resultCode, data);
                    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                        String fileName = "ImageFile.jpg";
                        bitmapProfile = imageUtilityHelper.bitmapProfile;
                        //Uploading (bitmapProfileImage) to server using API.
                        // If successful then set image to (mIvMyImage).
                        sendImageToServerRequest();

                    } else return;
                default:
                    break;
            }
        } else {
            onBackPressed();
        }
    }

    private void goToHomeScreen() {
        Intent goToHomeIntent = new Intent(AddMeToSgksActivity.this, HomeActivity.class);
        startActivity(goToHomeIntent);
    }

    public static String convertImageToBase64(Bitmap bitmap, int compression) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, compression, baos);
        byte[] b = baos.toByteArray();

        String result = Base64.encodeToString(b, Base64.DEFAULT);
        //String result = new String(encoded);

        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivAddImage:
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_CODE);
                } else {
                    getImageChooser();
                }
                break;
        }
    }

    public void genderCheckBox(){
        rgGender.clearCheck();
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbGender = findViewById(checkedId);
                if (null != rbGender && checkedId != -1) {
                    strGender = rbGender.getText().toString();
                }
            }
        });
    }

    public void bloodGropSpinnerListener(){
        spBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BloodGroupItems bloodGroupItems = arrbloodGroupList.get(parent.getSelectedItemPosition());
                strBloodGroupId = bloodGroupItems.getStrBloodGroupId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void dobListener(){
        tvDob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        int intMonth = mMonth+1;
                        tvDob.setText(mDay + "-" + intMonth + "-" + mYear);
                        strDateOfBirth = mYear + "-" + intMonth + "-" + mDay ;
                        Log.i(TAG, "onDateSet: "+strDateOfBirth);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    public void validateFieldsAndApiCall(){
        findViewById(R.id.addSgksMember).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strFirstName = metFirstName.getText().toString().trim();
                strLastName = metLastName.getText().toString().trim();
                strMiddleName = metMiddleName.getText().toString().trim();
                strEmail = metEmail.getText().toString().trim();
                strAddress = metAddress.getText().toString().trim();

                if (strFirstName.isEmpty()) {
                    metFirstName.setError("Please Enter your First Name");
                    metFirstName.requestFocus();
                    return;
                }
                if (strMiddleName.isEmpty()) {
                    metMiddleName.setError("Please Enter your Middle Name");
                    metMiddleName.requestFocus();
                    return;
                }
                if (strLastName.isEmpty()) {
                    metLastName.setError("Please Enter your Last Name");
                    metLastName.requestFocus();
                    return;
                }
                if (strContact.isEmpty()) {
                    Toast.makeText(mContext, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (new AppCommonMethods(mContext).isNetworkAvailable()) {
                        requestAddMember();
                    } else {
                        new AppCommonMethods(mContext).showAlert(mContext.getString(R.string.noInternet));
                    }
                }
            }
        });
    }
}


