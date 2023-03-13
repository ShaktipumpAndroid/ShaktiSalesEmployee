package activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.shaktipumps.shakti.shaktisalesemployee.BuildConfig;
import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bean.LoginBean;
import database.DatabaseHelper;
import webservice.CustomHttpClient;
import webservice.SAPWebService;
import webservice.WebURL;

@SuppressWarnings("deprecation")
public class LoginActivity extends AppCompatActivity {

    private AppUpdateManager appUpdateManager;
    private static final int IMMEDIATE_APP_UPDATE_REQ_CODE = 100;
    public final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    SAPWebService con = null;
    DatabaseHelper dataHelper = null;
    String username, password, login, ename,spinner_login_type_text;
    TextView btnforgot;
    SharedPreferences pref;
    List<String> list = null;
    Context mContext;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS,
         /*   android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,*/
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
    };
    SharedPreferences.Editor editor;

    int index1;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(LoginActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private ProgressDialog progressDialog;
    private Spinner spinner_login_type;

    private EditText inputName, inputPassword;
    private TextInputLayout inputLayoutName, inputLayoutPassword;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new1);
        mContext = this;

        progressDialog = new ProgressDialog(mContext);

        LoginActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        dataHelper = new DatabaseHelper(this);

        list = new ArrayList<>();
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        checkUpdate();
        getUserTypeValue();

        inputLayoutName = findViewById(R.id.input_layout_name);


        inputLayoutPassword = findViewById(R.id.input_layout_password);
        spinner_login_type =  findViewById(R.id.spinner_login_type);
        inputName = findViewById(R.id.login_Et);

        inputPassword = findViewById(R.id.password);
        TextView btnSignUp = findViewById(R.id.btn_signup);
        btnforgot = findViewById(R.id.tv_forgot);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        // inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

        con = new SAPWebService();

        // if(checkAndRequestPermissions()) {

//******* Create SharedPreferences *******/

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();



        btnSignUp.setOnClickListener(view -> submitForm());


        btnforgot.setOnClickListener(view -> {


            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);


        });


        spinner_login_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                index1 = arg0.getSelectedItemPosition();
                spinner_login_type_text = spinner_login_type.getSelectedItem().toString();
                if(spinner_login_type_text.equals("Service Center"))
                {
                    spinner_login_type_text = "SRV_CNTR";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinner_login_type.setPrompt("Select Login Type");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mContext, R.layout.spinner_item_center, list);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_item_center);

        // attaching data adapter to spinner
        spinner_login_type.setAdapter(dataAdapter);
    }

    private void checkUpdate() {

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startUpdateFlow(appUpdateInfo);
            } else if  (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
                startUpdateFlow(appUpdateInfo);
            }
        });
    }

    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, LoginActivity.IMMEDIATE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMMEDIATE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Update canceled by user! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Update success!" , Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Update Failed! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
                checkUpdate();
            }
        }
    }

    public void getUserTypeValue() {
        list.add("Select Login Type");
        list.add("Employee");
        list.add("Vendor");
        list.add("Service Center");
    }



    /**********************************************************************************************
     *                Server Login
     *********************************************************************************************/

    private void serverLogin() {



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);



        username = inputName.getText().toString();
        password = inputPassword.getText().toString();



        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.add(new BasicNameValuePair("PERNR", username));
        param.add(new BasicNameValuePair("PASS", password));
        param.add(new BasicNameValuePair("OBJS", spinner_login_type_text));
        param.add(new BasicNameValuePair("DEVICE_NAME", CustomUtility.getDeviceName()));
        param.add(new BasicNameValuePair("IMEI", CustomUtility.getDeviceId(mContext)));
        param.add(new BasicNameValuePair("APP_VERSION", BuildConfig.VERSION_NAME));
        param.add(new BasicNameValuePair("API", String.valueOf(Build.VERSION.SDK_INT)));
        param.add(new BasicNameValuePair("API_VERSION", Build.VERSION.RELEASE));
        //   param.add(new BasicNameValuePair("OS", osName));

//        try {

//******************************************************************************************/
/*                   server connection
/******************************************************************************************/
        progressDialog = ProgressDialog.show(LoginActivity.this, "", "Connecting to server..please wait !");

        new Thread() {

            public void run() {

                if (CustomUtility.isOnline(LoginActivity.this)) {

                    try {

                        String obj = CustomHttpClient.executeHttpPost1(WebURL.LOGIN_PAGE, param);

                        Log.d("login_obj", "" + obj);

                        if (obj != null) {
//******************************************************************************************/
/*                       get JSONwebservice Data
//******************************************************************************************/
                            JSONArray ja = new JSONArray(obj);
                            //Log.d("ja", "" + ja);
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject jo = ja.getJSONObject(i);

                                login = jo.getString("LOGIN");
                                ename = jo.getString("NAME");

                                Log.d("succ", "" + login);

                            }
//******************************************************************************************/
/*                       Call DashBoard
/******************************************************************************************/

                            if (!login.equalsIgnoreCase("logged_in")) {


                                if ("Y".equals(login)) {


                                    editor.putString("key_login", "Y");
                                    editor.putString("key_username", username);
                                    editor.putString("key_ename", ename);


                                    editor.commit();

                                    CustomUtility.setSharedPreference(mContext,"objs",spinner_login_type_text);
                                    CustomUtility.setSharedPreference(mContext,"pernr",username);


                                    LoginBean.setLogin(pref.getString("key_username", "userid"), pref.getString("key_ename", "username"));

                            if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }


                                    // call service for capture lat long on every 15 min

                                    //startService(new Intent(LoginActivity.this, TimeService.class));

                                    CustomUtility.setSharedPreference(mContext,"localconvenience","0");

                                    Intent intent = new Intent(LoginActivity.this, MainActivity1.class);
                                    startActivity(intent);
                                    // close login activity when going to next activity
                                    LoginActivity.this.finish();
                                    finish();

                                } else {

                                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
//*********************create message in thread*******************************/
                                    Message msg1 = new Message();
                                    msg1.obj = "Invalid username or password";
                                    mHandler.sendMessage(msg1);

//                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();

                                    // dismiss the progress dialog

                                }

                            } else {
                            if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                                Message msg = new Message();
                                msg.obj = "User is already logged in from another device !";
                                mHandler.sendMessage(msg);
                            }


                        } else {
                              if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                            Toast.makeText(getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        Log.d("exce", "" + e);
                        e.printStackTrace();
                    }


                } else {
                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }


                    Message msg2 = new Message();
                    msg2.obj = "No Internet Connection";
                    mHandler.sendMessage(msg2);
                }


            }

        }.start();
    }

    /**********************************************************************************************
     *                Validating form
     *********************************************************************************************/
    private void submitForm() {


        if (!validateName()) {
            return;
        }


        if (!validatePassword()) {
            return;
        }


//********************   Server Login    *******************************************************/

       if (!hasPermissions(mContext, PERMISSIONS)) {
            ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIONS, PERMISSION_ALL);
        } else {
            serverLogin();
        }
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // Permission Granted

                // Permission Denied

                LoginActivity.this.finish();
                System.exit(0);

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private final View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            if (view.getId() == R.id.login_Et) {
                validateName();

            }
        }

    }

}
