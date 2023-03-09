package activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import syncdata.SyncDataToSAP;

public class ForgotPasswordActivity extends AppCompatActivity {

    ConnectivityManager connectivityManager;
    Context context = null;
    boolean connected = false;
    String status = null;
    EditText pernr;
    EditText telnr;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(ForgotPasswordActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;
    private TextInputLayout inputLayoutpernr, inputLayouttelnr;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        context = this;
        progressDialog = new ProgressDialog(context);

        ForgotPasswordActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        inputLayoutpernr = (TextInputLayout) findViewById(R.id.input_layout_pernr);
        inputLayouttelnr = (TextInputLayout) findViewById(R.id.input_layout_telnr);
        pernr = (EditText) findViewById(R.id.pernr);
        telnr = (EditText) findViewById(R.id.telnr);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        Button reset = (Button) findViewById(R.id.reset);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Password Recovery");




        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (submitForm()) {


                    progressDialog = ProgressDialog.show(ForgotPasswordActivity.this, "", "Validating Credentials");

                    new Thread() {

                        public void run() {

                            if (CustomUtility.isOnline(ForgotPasswordActivity.this)) {


                                if (new SyncDataToSAP().ResetPassword(context, pernr.getText().toString(), telnr.getText().toString()).equals("success")) {

                                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

                                    Message msg = new Message();
                                    msg.obj = "Password Sent To Registered Mobile Number";
                                    mHandler.sendMessage(msg);

                                    ForgotPasswordActivity.this.finish();

                                } else {
                                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

                                    Message msg = new Message();
                                    msg.obj = "Invalid Credentials";
                                    mHandler.sendMessage(msg);

                                }

                            } else {
                                  if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                                Message msg = new Message();
                                msg.obj = "No Internet Connection";
                                mHandler.sendMessage(msg);


                            }
                        }
                    }.start();


                }
            }
        });


    }

    private boolean submitForm() {
        boolean value;
        if (
            // validateOnline() &&
                (validatePerson()) &&
                        (validatePhone()) &&
                        (validateMobileNo())

        ) {
            value = true;
        } else {
            value = false;
        }
        return value;
    }

    private boolean validatePerson() {
        if (pernr.getText().toString().trim().isEmpty()) {
            inputLayoutpernr.setError("Enter User Name");

            requestFocus(pernr);
            return false;
        } else {
            inputLayoutpernr.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateOnline() {
        if (CustomUtility.isOnline(ForgotPasswordActivity.this)) {


            return true;
        } else {

            Toast.makeText(this, "No Internet Connection Found !", Toast.LENGTH_LONG).show();
            return false;

        }


    }


    private boolean validatePhone() {
        if (telnr.getText().toString().trim().isEmpty()) {
            inputLayouttelnr.setError("Enter Registered Mobile No");

            requestFocus(telnr);
            return false;
        } else {
            inputLayouttelnr.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public boolean validateMobileNo() {
        if (telnr.getText().toString().trim().isEmpty()) {
            inputLayouttelnr.setError("Please Enter Mobile Number");

            requestFocus(telnr);
            return false;
        } else {
            inputLayouttelnr.setErrorEnabled(false);
        }


        int len = telnr.getText().toString().trim().length();
        if (len < 10) {
            inputLayouttelnr.setError("Please Enter Valid Mobile No");
            requestFocus(telnr);
            return false;
        }


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {

            case android.R.id.home:
                onBackPressed();

                return true;

//            case R.id.action_dsr_menu:
//                /**************************** sync dsr data **************************************************/
//                syncing_dsr_data();
//                return true;

        }
        return super.onOptionsItemSelected(item);
    }


}
