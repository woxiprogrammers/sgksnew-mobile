package com.woxi.sgks_member.miscellaneous;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.woxi.sgks_member.AppController;
import com.woxi.sgks_member.R;
import com.woxi.sgks_member.models.AccountDetailsItem;
import com.woxi.sgks_member.models.AccountYearItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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
    private ArrayList<String> arrayYearStringsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_and_account_home);
        getSupportActionBar().setTitle(getString(R.string.accounts));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Calling function to initialize required views.
        initializeViews();
        requestAccountsAPI();
    }

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Rohit
     */
    private void initializeViews() {
        mContext = AccountsActivity.this;
        mSpinAccountYear = (Spinner) findViewById(R.id.spinAccountYear);
        ((TextView) findViewById(R.id.tvYearTitle)).setText("Select Account Year");

        //Set-up RecyclerView
        mRvAccountImages = (RecyclerView) findViewById(R.id.rvAccountImages);
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
                if (arrAccountYear.size() > 0) {
                    setCurrentYearAccount();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void requestAccountsAPI() {
        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading, Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        String currentCity = AppCommonMethods.getStringPref(PREFS_CURRENT_CITY, mContext);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppURLs.API_ACCOUNT_DEATILS + currentCity, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        new AppCommonMethods().LOG(0, TAG, response.toString());
                        try {
                            Object resp = AppParser.parseAccountDetailsResponse(response.toString());
                            if (resp instanceof ArrayList) {
                                if (((ArrayList) resp).size() != 0) {
                                    ((TextView) findViewById(R.id.tvNotAvailable)).setVisibility(View.GONE);
                                    ((LinearLayout) findViewById(R.id.llSelectYear)).setVisibility(View.VISIBLE);
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
                                }
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
                    ((TextView) findViewById(R.id.tvNotAvailable)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tvNotAvailable)).setText("No Account To Show");
                    ((LinearLayout) findViewById(R.id.llSelectYear)).setVisibility(View.GONE);
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

    private void setCurrentYearAccount() {
        String strSelectedYear = mSpinAccountYear.getSelectedItem().toString();
        AccountYearItem accountYearItem = arrAccountYear.get(arrayYearStringsList.indexOf(strSelectedYear));
        mArrAccountDetails = accountYearItem.getArrAccountDetails();
        accountAndEventDetailsAdapter = new AccountAndEventDetailsAdapter(mArrAccountDetails);
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
