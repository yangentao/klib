package net.yet.yetlibdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import net.yet.yetlib.Hello;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String s = Hello.hello();
		TextView tv = new TextView(this);
		tv.setText(s);
		setContentView(tv);

	}
}
