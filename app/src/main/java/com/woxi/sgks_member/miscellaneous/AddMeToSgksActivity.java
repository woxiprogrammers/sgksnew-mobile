package com.woxi.sgks_member.miscellaneous;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.woxi.sgks_member.interfaces.AppConstants;
import com.woxi.sgks_member.models.SGKSAreaItem;
import com.woxi.sgks_member.utils.AppCommonMethods;
import com.woxi.sgks_member.utils.AppURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.woxi.sgks_member.interfaces.AppConstants.PREFS_CURRENT_CITY;
import static com.woxi.sgks_member.interfaces.AppConstants.PREFS_SGKS_AREA_LIST;
import static com.woxi.sgks_member.interfaces.AppConstants.STATUS_SOMETHING_WENT_WRONG;

/**
 * <b>public class AddMeToSgksActivity extends AppCompatActivity implements AppConstants</b>
 * <p>This class is used for Add Me To Sgks page</p>
 * Created by Rohit.
 */
public class AddMeToSgksActivity extends AppCompatActivity implements AppConstants {
    private Context mContext;
    private EditText metName, metContact;
    private Spinner spArea;
    private String TAG = "AddMeToSgksActivity";
    private String strName, strContactNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_me_to_sgks);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.add_me_sgks);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initializeViews();

        //Setting 1st choice as hint
        ArrayList<String> arrSgksArea=new ArrayList<>();
        for(SGKSAreaItem sgksAreaItem: SplashAndCityActivity.sgksAreaItems){
            String s=sgksAreaItem.getAreaName();
            arrSgksArea.add(s);

        }
//        ArrayList<String> arrSgksArea = getSgksAreaList();
        arrSgksArea.add(0, "Choose One");

        ArrayAdapter<String> arrayAdapter = getStringArrayAdapter(arrSgksArea);
        spArea.setAdapter(arrayAdapter);

        findViewById(R.id.addSgksMember).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strName = metName.getText().toString().trim();
                strContactNumber = metContact.getText().toString().trim();

                if (strName.isEmpty()) {
                    metName.setError("Please Enter your Name");
                    metName.requestFocus();
                    return;
                }  else if (strContactNumber.isEmpty()) {
                    metContact.setError("Please Enter your Contact Number");
                    metContact.requestFocus();
                    return;
                }else if (spArea.getSelectedItemId() == 0) {
                    Toast.makeText(mContext, "Please Select SGKS Area", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (new AppCommonMethods(mContext).isNetworkAvailable()) {
                        requestAddToSgksContentAPI();
                    } else {
                        new AppCommonMethods(mContext).showAlert(mContext
                                .getString(R.string.noInternet));
                    }
                }
            }
        });
    }

    private void initializeViews() {
        mContext = AddMeToSgksActivity.this;
        metName =  findViewById(R.id.etName);
        metContact = findViewById(R.id.etContact);
        metName.requestFocus();
        spArea =  findViewById(R.id.spArea);

    }

    public void clearDataAddMeSGKS() {
        metName.setText("");
        metContact.setText("");
        spArea.setSelection(0);
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

    private void requestAddToSgksContentAPI() {
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading, Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        strName = metName.getText().toString().trim();
        strContactNumber = metContact.getText().toString().trim();
        String strAreaAddSGKS = spArea.getSelectedItem().toString();
        String currentCity = AppCommonMethods.getStringPref(PREFS_CURRENT_CITY, mContext);

        JSONObject params = new JSONObject();
        try {
            params.put("fullname", strName);
            params.put("area", strAreaAddSGKS.trim());
            params.put("cont_number", strContactNumber);
            params.put("sgks_city", currentCity);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.POST, AppURLs.API_ADDME_TO_SGKS, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        new AppCommonMethods().LOG(0, TAG, response.toString());
//On Response
                        if (response.has("message")) {
                            try {
                                Toast.makeText(mContext, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                                clearDataAddMeSGKS();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    new AppCommonMethods().LOG(0, TAG, "response code " + error.networkResponse.statusCode + " message= " + new String(error.networkResponse.data));
                    try {
                        if (response.statusCode == STATUS_SOMETHING_WENT_WRONG) {
                            new AppCommonMethods(mContext).showAlert("" + (new JSONObject(new String(response.data))).getString("message"));
                        } else {
                            new AppCommonMethods(mContext).showAlert("" + (getString(R.string.optional_api_error)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json; charset=UTF-8");
                return headers;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "addMeToSGKS");
    }

    private ArrayAdapter<String> getStringArrayAdapter(ArrayList<String> arrayList) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayList) {
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
}
