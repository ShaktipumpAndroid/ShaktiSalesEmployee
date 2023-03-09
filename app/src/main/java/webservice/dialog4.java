package webservice;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import activity.DeviceStatusActivity;


public class dialog4 extends Dialog {

    private final DeviceStatusActivity activity;
    private EditText text;
    private final dialog4 thisDialog;

    public dialog4(DeviceStatusActivity context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.activity = context;
        this.thisDialog = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dailog_search1);

        thisDialog.setCancelable(false);
        thisDialog.setCanceledOnTouchOutside(false);
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        initalize();
    }

    private void initalize() {
        // TODO Auto-generated method stub
        text = findViewById(R.id.text);
        Button search = findViewById(R.id.search);
        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            thisDialog.cancel();
            activity.finish();
        });
        search.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            String textString = text.getText().toString();
            activity.searchWord(textString);
        });
    }

}
