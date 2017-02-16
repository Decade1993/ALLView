package rejointech.decade.allview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import rejointech.decade.allview.DJN_Views.SuperLoadView;

public class MainActivity extends AppCompatActivity {

    SuperLoadView mSuperLoadView;
    Button btn_repeat_success;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSuperLoadView =(SuperLoadView)findViewById(R.id.View_superLoad);
        btn_repeat_success = (Button)findViewById(R.id.btn_repeat);
        btn_repeat_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSuperLoadView.start(true);
            }
        });
    }
}
