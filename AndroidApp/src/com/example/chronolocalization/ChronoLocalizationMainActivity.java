package com.example.chronolocalization;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Resources res = getResources();
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
		
		tabHost.addTab(tabHost.newTabSpec("Watch User").setIndicator("Watch User", res.getDrawable(R.drawable.ic_launcher)).setContent(R.id.tabWatchUser));
		tabHost.addTab(tabHost.newTabSpec("Configuration").setIndicator("Configuration", res.getDrawable(R.drawable.ic_launcher)).setContent(R.id.tabConfiguration));
		
		Intent intent = new Intent(this,WatchUserActivity.class);
		startActivity(intent);
		tabHost.setCurrentTab(0);
	}
}
