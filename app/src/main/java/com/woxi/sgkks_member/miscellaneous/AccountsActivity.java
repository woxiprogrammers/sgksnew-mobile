package com.woxi.sgkks_member.miscellaneous;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woxi.sgkks_member.AppController;
import com.woxi.sgkks_member.R;
import com.woxi.sgkks_member.adapters.AccountAndEventDetailsAdapter;
import com.woxi.sgkks_member.models.AccountDetailsItem;
import com.woxi.sgkks_member.models.AccountYearItem;
import com.woxi.sgkks_member.utils.AppCommonMethods;
import com.woxi.sgkks_member.utils.AppParser;
import com.woxi.sgkks_member.utils.AppSettings;
import com.woxi.sgkks_member.utils.AppURLs;
import com.woxi.sgkks_member.utils.ImageZoomDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_CURRENT_CITY;
import static com.woxi.sgkks_member.interfaces.AppConstants.PREFS_LANGUAGE_APPLIED;
import static com.woxi.sgkks_member.interfaces.AppConstants.STATUS_NO_RESULTS_FOUND;
import static com.woxi.sgkks_member.interfaces.AppConstants.STATUS_SOMETHING_WENT_WRONG;

/**
 * <b>public class AccountsActivity extends AppCompatActivity</b>
 * <p>This class is used to show Accounts Screen</p>
 * Created by Rohit.
 */
public class AccountsActivity extends AppCompatActivity {
    public static View.OnClickListener onGridImageClickListener;
    private Context mContext;
    private String TAG = "AccountsActivity";
    private RecyclerView mRvAccountImages;
    private Spinner mSpinAccountYear;
    private ArrayList<AccountDetailsItem> mArrAccountDetails;
    private ArrayList<AccountYearItem> arrAccountYear;
    private AccountAndEventDetailsAdapter accountAndEventDetailsAdapter;
    private ArrayList<Integer> arrayYearIntegerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_and_account_home);
        getSupportActionBar().setTitle(getString(R.string.accounts));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Calling function to initialize required views.
        initializeViews();
        requestAccountsAPI(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
    }

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Rohit
     */
    private void initializeViews() {
        mContext = AccountsActivity.this;

        //Set-up Year spinner
        mSpinAccountYear =  findViewById(R.id.spinAccountYear);
        ((TextView) findViewById(R.id.tvYearTitle)).setText("Select Account Year");
        for (int i = 2015; i <= 2030; i++){
            arrayYearIntegerList.add(i-2015,i);
        }
        ArrayAdapter<Integer> integerArrayAdapter = new ArrayAdapter<Integer>(AccountsActivity.this, android.R.layout.simple_spinner_item, arrayYearIntegerList);
        integerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinAccountYear.setAdapter(integerArrayAdapter);
        mSpinAccountYear.setSelection(3);

        //Set-up RecyclerView
        mRvAccountImages = findViewById(R.id.rvAccountImages);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        mRvAccountImages.setLayoutManager(layoutManager);
        mArrAccountDetails = new ArrayList<>();
        accountAndEventDetailsAdapter = new AccountAndEventDetailsAdapter(mArrAccountDetails);
        mRvAccountImages.setAdapter(accountAndEventDetailsAdapter);

        //Function for account image click listener
        onGridImageClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View selectedView) {
                AccountDetailsItem accountDetailsItem = mArrAccountDetails.get(mRvAccountImages.getChildAdapterPosition(selectedView));
                ImageZoomDialogFragment imageZoomDialogFragment = ImageZoomDialogFragment.newInstance(accountDetailsItem);
                imageZoomDialogFragment.setCancelable(true);
                imageZoomDialogFragment.show(getSupportFragmentManager(), "imageZoomDialogFragment");
            }
        };

        //Function for spinner year change listener
        mSpinAccountYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (arrayYearIntegerList.size() > 0) {
                    requestAccountsAPI(parent.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void requestAccountsAPI(String year) {
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading, Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        JSONObject params = new JSONObject();
        try {
            params.put("sgks_city",1);
            params.put("language_id", AppSettings.getStringPref(PREFS_LANGUAGE_APPLIED,mContext));
            params.put("year",year);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AppURLs.API_ACCOUNT_LISTING,  params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        new AppCommonMethods().LOG(0, TAG, response.toString());
                        try {
                            Object resp = AppParser.parseAccountDetailsResponse(response.toString());
                            if (resp instanceof ArrayList) {
                                mArrAccountDetails= (ArrayList<AccountDetailsItem>) resp;
                                setCurrentYearAccount(mArrAccountDetails);
                                /*if (((ArrayList) resp).size() != 0) {
                                    ( findViewById(R.id.tvNotAvailable)).setVisibility(View.GONE);
                                    ( findViewById(R.id.llSelectYear)).setVisibility(View.VISIBLE);
                                    arrAccountYear = (ArrayList<AccountYearItem>) resp;
                                    if (arrAccountYear.size() > 0) {
                                        arrayYearStringsList = new ArrayList<>();
                                        for (int index = 0; index < arrAccountYear.size(); index++) {
                                            String strYear = arrAccountYear.get(index).getStrYear();
                                            arrayYearStringsList.add(strYear);
                                        }
                                        ArrayAdapter<String> arrayYearAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, arrayYearStringsList);
                                        arrayYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        mSpinAccountYear.setAdapter(arrayYearAdapter);

                                        setCurrentYearAccount();
                                    }
                                }*/
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    ( findViewById(R.id.tvNotAvailable)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tvNotAvailable)).setText("No Account To Show");
                    ( findViewById(R.id.llSelectYear)).setVisibility(View.GONE);
                    new AppCommonMethods().LOG(0, TAG, "response code " + error.networkResponse.statusCode + " message= " + new String(error.networkResponse.data));
                    try {
                        if (response.statusCode == STATUS_SOMETHING_WENT_WRONG) {
                            new AppCommonMethods(mContext).showAlert("" + (new JSONObject(new String(response.data))).getString("message"));
                        } else if (response.statusCode == STATUS_NO_RESULTS_FOUND) {
                            new AppCommonMethods(mContext).showAlert("" + (new JSONObject(new String(response.data))).getString("message"));
                        } else {
                            new AppCommonMethods(mContext).showAlert((getString(R.string.optional_api_error)));
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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "apiAccountsDetails");
    }

    private void setCurrentYearAccount(ArrayList<AccountDetailsItem> accountDetailsItems) {
        /*String strSelectedYear = mSpinAccountYear.getSelectedItem().toString();
        AccountYearItem accountYearItem = arrAccountYear.get(arrayYearStringsList.indexOf(strSelectedYear));
        mArrAccountDetails = accountYearItem.getArrAccountDetails();*/
        accountAndEventDetailsAdapter = new AccountAndEventDetailsAdapter(accountDetailsItems);
        mRvAccountImages.setAdapter(accountAndEventDetailsAdapter);
        accountAndEventDetailsAdapter.notifyDataSetChanged();
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
}
