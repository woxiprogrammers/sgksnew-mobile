package com.woxi.sgks_member.miscellaneous;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.sgks_member.AppController;
import com.woxi.sgks_member.R;
import com.woxi.sgks_member.SplashAndCityActivity;
import com.woxi.sgks_member.home.HomeActivity;
import com.woxi.sgks_member.interfaces.AppConstants;
import com.woxi.sgks_member.models.BloodGroupItems;
import com.woxi.sgks_member.models.CityItems;
import com.woxi.sgks_member.models.SGKSAreaItem;
import com.woxi.sgks_member.utils.AppCommonMethods;
import com.woxi.sgks_member.utils.AppParser;
import com.woxi.sgks_member.utils.AppURLs;
import com.woxi.sgks_member.utils.ImageUtilityHelper;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.woxi.sgks_member.interfaces.AppConstants.PREFS_CURRENT_CITY;
import static com.woxi.sgks_member.interfaces.AppConstants.PREFS_SGKS_AREA_LIST;
import static com.woxi.sgks_member.interfaces.AppConstants.STATUS_SOMETHING_WENT_WRONG;

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
    private Spinner spBloodGroup;
    private Spinner spCity;
    private String TAG = "AddMeToSgksActivity";
    private String strFirstName;
    private String strLastName;
    private String strMiddleName;
    private String strContact;
    private String strGender;
    private int intBloodGroupId;
    private int intCityId;
    private String strEmail;
    private String strAddress;
    private String strDateOfBirth;
    private String strImageName;
    private ArrayList<BloodGroupItems> arrBloodGroup = new ArrayList<>();
    private ArrayList<CityItems> arrCity = new ArrayList<>();
    private RadioButton rbGender;
    private Calendar calendar;
    private ImageView ivProfilePicture;
    private ImageView ivAddImage;
    private ImageUtilityHelper imageUtilityHelper;
    private Bitmap bitmapProfile;
    private RadioGroup rgGender;
    private ArrayAdapter<String> arrBloodGroupAdapter;
    private Intent intent;
    private ArrayList<String> arrCityNames;
    private File fileProfileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_me_to_sgks);
        intent = getIntent();
        strContact = intent.getStringExtra("mobile_number");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.add_me_sgks);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        requestBloodGroup();
        requestCity();
        initializeViews();

        rgGender.clearCheck();
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbGender = findViewById(checkedId);
                if(null != rbGender && checkedId != -1){
                    strGender = rbGender.getText().toString();
                }
            }
        });

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                intCityId = parent.getSelectedItemPosition();
                Log.i("@@@", "onItemSelected: "+intCityId+1);
                intCityId=+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                intBloodGroupId = parent.getSelectedItemPosition();
                intBloodGroupId=+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tvDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddMeToSgksActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        tvDob.setText(mDay + "-" + mMonth + "-" + mYear);
                        strDateOfBirth = mDay + "-" + mMonth + "-" + mYear;
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
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
                if(strContact.isEmpty()) {
                    Toast.makeText(mContext, "Please Enter Mobile NUmber", Toast.LENGTH_SHORT).show();
                    return;
                }
               /* if(spCity.getSelectedItemId() == 0){
                    Toast.makeText(mContext, "Please Select City", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                 else{
                    if (new AppCommonMethods(mContext).isNetworkAvailable()) {
                        requestAddMember();
                    } else {
                        new AppCommonMethods(mContext).showAlert(mContext.getString(R.string.noInternet));
                    }
                }
            }
        });
    }

    private void initializeViews() {
        mContext = AddMeToSgksActivity.this;
        metFirstName =  findViewById(R.id.etFirstName);
        metMiddleName = findViewById(R.id.etMiddleName);
        metLastName = findViewById(R.id.etLastName);
        metContact = findViewById(R.id.etContact);
        metContact.setText(strContact);
        metContact.setEnabled(false);
        metEmail = findViewById(R.id.etEmail);
        metAddress = findViewById(R.id.etAddress);
        spCity = findViewById(R.id.spCity);
        spBloodGroup = findViewById(R.id.spBloodGroup);
        tvDob = findViewById(R.id.tvDob);
        ivProfilePicture = findViewById(R.id.ivProfileImage);
        ivAddImage = findViewById(R.id.ivAddImage);
        rgGender = findViewById(R.id.rgGender);
        metFirstName.requestFocus();
        imageUtilityHelper = new ImageUtilityHelper(mContext);
        ivAddImage.setOnClickListener(AddMeToSgksActivity.this);
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

    public ArrayList<String> getSgksAreaList() {
        ArrayList<String> arrSgksArea = new ArrayList<>();
        String strSuggestionCategory = AppCommonMethods.getStringPref(PREFS_SGKS_AREA_LIST, mContext);
        strSuggestionCategory = strSuggestionCategory.replace("[", "");
        strSuggestionCategory = strSuggestionCategory.replace("]", "");
        strSuggestionCategory = strSuggestionCategory.replace("\"", "");
        arrSgksArea = new ArrayList<>(Arrays.asList(strSuggestionCategory.split(",")));
        return arrSgksArea;
    }

    private void requestBloodGroup () {
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, AppURLs.API_GET_BLOOD_GROUP, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                new AppCommonMethods(mContext).LOG(0,"blood_group",response.toString());
                                if(response.has("data")) {
                                    try {
                                        Object resp = AppParser.parseBloodGroupResponse(response.toString());
                                        arrBloodGroup = (ArrayList<BloodGroupItems>) resp;
                                        ArrayAdapter<BloodGroupItems> arrBloodGroupAdapter = getStringArrayAdapter(arrBloodGroup);
                                        spBloodGroup.setAdapter(arrBloodGroupAdapter);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new AppCommonMethods(mContext).LOG(0,"error_blood_group",error.toString());
                        Toast.makeText(mContext,"Failed",Toast.LENGTH_LONG).show();
                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "get_blood_group");
    }

    private void requestCity () {
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, AppURLs.API_GET_CITY, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                new AppCommonMethods(mContext).LOG(0,"get_city",response.toString());
                                if(response.has("data")){
                                    try {
                                        Object resp = AppParser.parseCityResponse(response.toString());
                                        arrCity = (ArrayList<CityItems>) resp;
//                                        arrCity.add(0,"Choose One");
                                        ArrayAdapter<CityItems> arrCityAdapter = getCityArrayAdapter(arrCity);
                                        spCity.setAdapter(arrCityAdapter);
                                    }  catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                new AppCommonMethods(mContext).LOG(0,"error_city",error.toString());
                                Toast.makeText(mContext,"Failed",Toast.LENGTH_LONG).show();
                            }
                        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "get_city");
    }

    private void sendImageToServerRequest() {
        JSONObject params = new JSONObject();
        try {
            params.put("image_for","profile_img");
            params.put("image",fileProfileImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppURLs.API_UPLOAD_IMAGE, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            new AppCommonMethods(mContext).LOG(0, "image_uploaded", response.toString());
                            if (!response.getString("filename").isEmpty()){
                                strImageName = response.getString("filename").toString();
                            } else{
                                Toast.makeText(mContext,"Image Not Uploaded",Toast.LENGTH_SHORT);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext,"Image Not Uploaded",Toast.LENGTH_SHORT);
                        new AppCommonMethods(mContext).LOG(0,"error_image_uploaded",error.toString());

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
            params.put("first_name",strFirstName);
            params.put("middle_name",strMiddleName);
            params.put("last_name",strLastName);
            params.put("gender",strGender);
            params.put("address",strAddress);
            params.put("date_of_birth",strDateOfBirth);
            params.put("blood_group_id",intBloodGroupId);
            params.put("city_id",intCityId);
            params.put("mobile",strContact);
            params.put("email",strEmail);
            params.put("profile_pic",strImageName);
            Log.i("@@@", "requestAddMember: "+params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppURLs.API_ADD_MEMBER, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            pDialog.hide();
                            new AppCommonMethods(mContext).LOG(0,"member_added",response.toString());
                            new AppCommonMethods(mContext).showAlert(response.getString("message"));
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
                        new AppCommonMethods(mContext).LOG(0,"error_member_added",error.toString());
                        Toast.makeText(mContext,"Failed. Please Try Again",Toast.LENGTH_LONG);
                    }
                });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "add_new_member");
    }

    private ArrayAdapter<BloodGroupItems> getStringArrayAdapter(ArrayList<BloodGroupItems> arrayList) {
        ArrayAdapter<BloodGroupItems> arrayAdapter = new ArrayAdapter<BloodGroupItems>(mContext, android.R.layout.simple_spinner_item, arrayList) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorTextHint, null));
                } else {
                    tv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorTextMain, null));
                }
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
    }
    private ArrayAdapter<CityItems> getCityArrayAdapter(ArrayList<CityItems> arrayList) {
        ArrayAdapter<CityItems> arrayAdapter = new ArrayAdapter<CityItems>(mContext, android.R.layout.simple_spinner_item, arrayList) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorTextHint, null));
                } else {
                    tv.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorTextMain, null));
                }
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
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
                case AppConstants.USER_LOGIN_ACTIVITY_RESULT_CODE:
                    //setProfileData();
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE:
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                case AddMeToSgksActivity.IMAGE_CHOOSER_CODE:
//                    For new camera functionality
                    imageUtilityHelper.onSelectionResult(requestCode, resultCode, data);
                    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                        bitmapProfile = imageUtilityHelper.bitmapProfile;
                        fileProfileImage = imageUtilityHelper.localImageFile;


                        //Uploading (bitmapProfileImage) to server using API.
                        // If successful then set image to (mIvMyImage).
                        sendImageToServerRequest();
                        ivProfilePicture.setImageBitmap(bitmapProfile);
                    } else return;
                default:
                    break;
            }
        } else {
            onBackPressed();
        }
    }

    private void goToHomeScreen(){
        Intent goToHomeIntent = new Intent(AddMeToSgksActivity.this, HomeActivity.class);
        startActivity(goToHomeIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivAddImage:
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_CODE);
                } else {
                    getImageChooser();
                }
                break;
        }
    }
}



