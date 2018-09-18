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
import com.woxi.sgks_member.models.SGKSCategory;
import com.woxi.sgks_member.utils.AppCommonMethods;
import com.woxi.sgks_member.utils.AppURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.woxi.sgks_member.interfaces.AppConstants.PREFS_CURRENT_CITY;
import static com.woxi.sgks_member.interfaces.AppConstants.PREFS_SUGGESTION_CATEGORY;
import static com.woxi.sgks_member.interfaces.AppConstants.STATUS_SOMETHING_WENT_WRONG;

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
    private String strSuggestionType = "suggestion", strSuggestionCategory, strSuggestionMessage;
    private String TAG = "SuggestionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.suggestion_box);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initializeViews();
        TextView tv =  findViewById(R.id.tvSubmit);
        tv.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      strSuggestionMessage = mEtComplaintsSuggestion.getText().toString().trim();
                                      if (strSuggestionMessage.isEmpty()) {
                                          mEtComplaintsSuggestion.setError("Please Enter Your Message");
                                          mEtComplaintsSuggestion.requestFocus();
                                          return;
                                      } else if (mSpinCategories.getSelectedItemId() == 0) {
                                          Toast.makeText(mContext, "Please Select Category", Toast.LENGTH_SHORT).show();
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
        RadioGroup mRgSuggestionType = findViewById(R.id.radioGroup);
        mRbSuggestion = findViewById(R.id.radioButtonSuggestion);
        mEtComplaintsSuggestion =  findViewById(R.id.editTextSuggestion);
        mSpinCategories = findViewById(R.id.spinnerSelect);
        mRbSuggestion.setChecked(true);
//        arrSuggestions = suggestionData();
        arrayAdapterSuggestion = getStringArrayAdapter(SplashAndCityActivity.sgksCategory);
        arrSuggestions.add(0, "Choose One");
        mSpinCategories.setAdapter(arrayAdapterSuggestion);
        mRgSuggestionType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == 0) {
                    strSuggestionType = "suggestion";
                } else {
                    strSuggestionType = "complaint";
                }
            }
        });
    }

    private ArrayList<String> suggestionData() {
        ArrayList<String> arrSuggestionCategory = new ArrayList<>();
        String strSuggestionCategory = AppCommonMethods.getStringPref(PREFS_SUGGESTION_CATEGORY, mContext);
        strSuggestionCategory = strSuggestionCategory.replace("[", "");
        strSuggestionCategory = strSuggestionCategory.replace("]", "");
        strSuggestionCategory = strSuggestionCategory.replace("\"", "");
        arrSuggestionCategory = new ArrayList<>(Arrays.asList(strSuggestionCategory.split(",")));
//        arrSuggestionCategory = new ArrayList<>(Collections.singletonList(Arrays.toString(strSuggestionCategory.split(",")).trim()));
        return arrSuggestionCategory;
    }

    public void requestCommentSgksContentAPI() {
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading, Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        strSuggestionMessage = mEtComplaintsSuggestion.getText().toString().trim();
        strSuggestionCategory = mSpinCategories.getSelectedItem().toString();
        String currentCity = AppCommonMethods.getStringPref(PREFS_CURRENT_CITY, mContext);
        JSONObject params = new JSONObject();
        try {
            params.put("is_suggestion", strSuggestionType);
            params.put("suggestion_cat", strSuggestionCategory.trim());
            params.put("suggestion_message", strSuggestionMessage);
//            params.put("sgks_city", currentCity);
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
        mRbSuggestion.setChecked(true);
    }

    private ArrayAdapter<String> getStringArrayAdapter(ArrayList<SGKSCategory> sgksCategories) {
//        ArrayList<String> stringArrayList=new ArrayList<>();a
        arrSuggestions=new ArrayList<>();
        for(SGKSCategory sgksCategory:sgksCategories){
            String s=sgksCategory.getStrCategoryName();
            arrSuggestions.add(s);

        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrSuggestions) {
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
