package com.jackpan.TaiwanpetadoptionApp;

import com.google.gson.Gson;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;

public class SpinnerActivity extends Activity {
	private Spinner spinner;
	private TextView textview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spinner);
		String [] name = {};
	}

	private void initLayout(){
		spinner = (Spinner) findViewById(R.id.spinner1);
		textview = (TextView) findViewById(R.id.spinnertextView);
		
	}
}
