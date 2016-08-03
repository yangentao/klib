package net.yet.yetlibdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import net.yet.util.DateUtil;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String s = DateUtil.date();
		TextView tv = new TextView(this);
		tv.setText(s);
		setContentView(tv);

	}
}
