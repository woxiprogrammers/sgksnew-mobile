package com.woxi.sgkks_member.miscellaneous;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.woxi.sgkks_member.AppController;
import com.woxi.sgkks_member.R;
import com.woxi.sgkks_member.models.SuggestionCategoriesItem;
import com.woxi.sgkks_member.utils.AppCommonMethods;
import com.woxi.sgkks_member.utils.AppParser;
import com.woxi.sgkks_member.utils.AppURLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_CURRENT_CITY;
import static com.woxi.sgkks_member.interfaces.AppConstants.STATUS_SOMETHING_WENT_WRONG;

/**
 * <b>public class SuggestionActivity extends AppCompatActivity</b>
 * <p>This class is used for "Suggestion" page</p>
 * Created by Rohit.
 */
public class SuggestionActivity extends AppCompatActivity {
    private Context mContext;
    private RadioButton mRbSuggestion;
    private EditText mEtComplaintsSuggestion;
    private Spinner mSpinCategories;
    private ArrayList<String> arrSuggestions;
    private ArrayAdapter<String> arrayAdapterSuggestion;
    private ArrayList<SuggestionCategoriesItem> arrCategoriesList;
    private String strSuggestionType="";
    private String strSuggestionMessage;
    private String TAG = "SuggestionActivity";
    private JSONArray arrJsonCategories;
    private String strCategoryId;
    private RadioGroup mRgSuggestionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.suggestion_box);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initializeViews();
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

    private void initializeViews() {
        mContext = SuggestionActivity.this;
        mRgSuggestionType = findViewById(R.id.radioGroup);
        mEtComplaintsSuggestion =  findViewById(R.id.editTextSuggestion);
        mSpinCategories = findViewById(R.id.spinnerSelect);
        if (new AppCommonMethods(mContext).isNetworkAvailable()){
            requestCategories();
        } else {
            new AppCommonMethods(mContext).showAlert("You are Offline");
        }


        //arrSuggestions.add(0, "Choose One");
        mSpinCategories.setAdapter(arrayAdapterSuggestion);
        mSpinCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SuggestionCategoriesItem suggestionCategoriesItem = arrCategoriesList.get(parent.getSelectedItemPosition());
                strCategoryId = suggestionCategoriesItem.getStrCategoryId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categorySpinnerListener();
        validateDateAndRequest();
    }

    public void requestCommentSgksContentAPI() {
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading, Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        strSuggestionMessage = mEtComplaintsSuggestion.getText().toString().trim();
        JSONObject params = new JSONObject();
        try {
            params.put("suggestion_type",strSuggestionType.toLowerCase());
            params.put("suggestion_cat", strCategoryId);
            params.put("suggestion_message", strSuggestionMessage);
            params.put("sgks_city", AppCommonMethods.getStringPref(PREFS_CURRENT_CITY,mContext));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppURLs.API_SGKS_SUGGESTIONS, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        new AppCommonMethods().LOG(0, TAG, response.toString());
//On Response
                        if (response.has("message")) {
                            try {
                                Toast.makeText(mContext, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                                clearCommentDataSGKS();
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
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(VOLLEY_REQUEST_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "SGKS_Suggestion");
    }

    public void clearCommentDataSGKS() {
        mSpinCategories.setSelection(0);
        mEtComplaintsSuggestion.setText("");

    }

    private void requestCategories(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppURLs.API_SUGGESTION_CATEGORIES, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                new AppCommonMethods(mContext).LOG(0,TAG,response.toString());
                try {
                    Object resp = AppParser.parseSuggestionCategoryResponse(response.toString());
                    arrCategoriesList = (ArrayList<SuggestionCategoriesItem>) resp;
                    arrJsonCategories = response.getJSONArray("data");
                    arrSuggestions = new ArrayList<>();
                    for (int i = 0; i < arrJsonCategories.length(); i++) {
                        JSONObject jsonObject = arrJsonCategories.getJSONObject(i);
                        arrSuggestions.add(jsonObject.getString("category"));
                    }
                    arrayAdapterSuggestion = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, arrSuggestions);
                    mSpinCategories.setAdapter(arrayAdapterSuggestion);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest,TAG);
    }

    private void validateDateAndRequest(){
        TextView tv =  findViewById(R.id.tvSubmit);
        tv.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      strSuggestionMessage = mEtComplaintsSuggestion.getText().toString().trim();
                                      Log.i(TAG, "onClick: "+strSuggestionType);
                                      if (strSuggestionMessage.isEmpty()) {
                                          mEtComplaintsSuggestion.setError("Please Enter Your Message");
                                          mEtComplaintsSuggestion.requestFocus();
                                          return;
                                      } else if (mSpinCategories.getSelectedItemId() == -1) {
                                          Toast.makeText(mContext, "Please Select Category", Toast.LENGTH_SHORT).show();
                                          return;
                                      } else if (strSuggestionType.isEmpty()) {
                                              new AppCommonMethods(mContext).showAlert("Is it Suggestion or Complaint? \nPlease Select One");
                                              return;
                                      } else {
                                          if (new AppCommonMethods(mContext).isNetworkAvailable()) {
                                              requestCommentSgksContentAPI();
                                          } else {
                                              new AppCommonMethods(mContext).showAlert(mContext
                                                      .getString(R.string.noInternet));
                                          }
                                      }
                                  }
                              }
        );
    }

    private void categorySpinnerListener(){
        mRgSuggestionType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton;
                Log.i(TAG, "onCheckedChanged: "+checkedId);
                radioButton = findViewById(checkedId);
                if (null != radioButton && checkedId != -1) {
                    strSuggestionType = radioButton.getText().toString();
                    Log.i(TAG, "onCheckedChanged: "+strSuggestionType);
                }
            }
        });
    }
}
