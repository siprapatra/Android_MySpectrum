package com.sipra.myspectrum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sipra.myspectrum.database.DatabaseHandler;

import java.util.ArrayList;


/**
 * An activity representing a single AddSalesManActivity detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 */
public class UserListActivity extends AppCompatActivity {

    private static final String TAG = UserListActivity.class.getName();
    private Context mContext;

    private FloatingActionButton fab_addUser;
    private RecyclerView rv_prd_type;
    private SwipeRefreshLayout swr_Prd_type;
    private TextView txtNoRecord;

    UserListAdapter userListAdapter;
    ArrayList<CustomerModel> customerModelArrayList;
    /**
     * Data base Handler object
     */
    DatabaseHandler dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        try {
            mContext = UserListActivity.this;
            dbHelper = new DatabaseHandler(UserListActivity.this);

            txtNoRecord = (TextView) findViewById(R.id.txtNoRecord);

            fab_addUser = (FloatingActionButton) findViewById(R.id.fab_addUser);
            fab_addUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserListActivity.this, AddUserActivity.class);
                    startActivityForResult(intent, 101);
                }
            });


            swr_Prd_type = (SwipeRefreshLayout) findViewById(R.id.swr_Prd_type);
            swr_Prd_type.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    customerModelArrayList = new ArrayList<>();
                    customerModelArrayList = getSalesMan();
                    userListAdapter = new UserListAdapter(UserListActivity.this, customerModelArrayList);
                    rv_prd_type.setAdapter(userListAdapter);
                    if (customerModelArrayList.size() > 0) {
                        swr_Prd_type.setVisibility(View.VISIBLE);
                        txtNoRecord.setVisibility(View.GONE);
                    } else {
                        txtNoRecord.setVisibility(View.VISIBLE);
                        swr_Prd_type.setVisibility(View.GONE);
                    }
                    swr_Prd_type.setRefreshing(false);
                }
            });
            rv_prd_type = (RecyclerView) findViewById(R.id.rv_prd_type);
            RecyclerView.LayoutManager mActiveLayoutManager = new LinearLayoutManager(UserListActivity.this);
            rv_prd_type.setLayoutManager(mActiveLayoutManager);

            customerModelArrayList = new ArrayList<>();
            customerModelArrayList = getSalesMan();
            userListAdapter = new UserListAdapter(UserListActivity.this, customerModelArrayList);
            rv_prd_type.setAdapter(userListAdapter);

            if (customerModelArrayList.size() > 0) {
                swr_Prd_type.setVisibility(View.VISIBLE);
                txtNoRecord.setVisibility(View.GONE);
            } else {
                txtNoRecord.setVisibility(View.VISIBLE);
                swr_Prd_type.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<CustomerModel> getSalesMan() {
        return dbHelper.getAllCustomer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Log.e(TAG, " requestCode = " + requestCode + " resultCode = " + resultCode);
            if (requestCode == 101 && resultCode == 102) {
                customerModelArrayList = new ArrayList<>();
                customerModelArrayList = getSalesMan();
                userListAdapter.addAll(customerModelArrayList);
                if (customerModelArrayList.size() > 0) {
                    swr_Prd_type.setVisibility(View.VISIBLE);
                    txtNoRecord.setVisibility(View.GONE);
                } else {
                    txtNoRecord.setVisibility(View.VISIBLE);
                    swr_Prd_type.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toast(String msg) {
        Toast.makeText(UserListActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
