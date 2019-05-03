package cn.cychust.simpleeventbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.cychust.simpleeventbus.SimpleEventBus.EventBus;
import cn.cychust.simpleeventbus.SimpleEventBus.Subscribe;
import cn.cychust.simpleeventbus.SimpleEventBus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private Button   mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.tv);
        mButton = findViewById(R.id.btn_start);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN_THREAD)
    public void testFoo(TestEvent testEvent) {
        mTextView.setText(testEvent.getText());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unRegister(this);
    }

}
