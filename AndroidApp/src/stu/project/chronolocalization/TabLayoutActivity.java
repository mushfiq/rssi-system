package stu.project.chronolocalization;

import java.util.Locale;

import com.example.chronolocalization.MainActivity;
import com.example.chronolocalization.R;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class TabLayoutActivity extends ActivityGroup {
	private TabHost mTabHost;
	public static TextToSpeech tts;


	private void setupTabHost() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(getLocalActivityManager());
	}
	/*
	*//** Called when the activity is first created. *//*
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// construct the tabhost
		setContentView(R.layout.tab);

		setupTabHost();
		mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);

		setupTab(new TextView(this), "Tab 1");
		setupTab(new TextView(this), "Tab 2");
		setupTab(new TextView(this), "Tab 3");
	}

	private void setupTab(final View view, final String tag) {
		View tabview = createTabView(mTabHost.getContext(), tag);

		Intent intent = new Intent(this,MainActivity.class);

		TabSpec setContent = mTabHost.newTabSpec(tag)
				.setIndicator(tabview)
//				.setContent(intent);
				.setContent(new TabContentFactory() {
			public View createTabContent(String tag) {return view;}
		});
		mTabHost.addTab(setContent);

	}*/

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tab_textview, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab);
		
tts = new TextToSpeech(TabLayoutActivity.this, new TextToSpeech.OnInitListener() {
			
			@Override
			public void onInit(int status) {
				// TODO Auto-generated method stub
//				Log.d("*****onInit****","onInit********");

				if(status!=TextToSpeech.ERROR){
					
					tts.setLanguage(Locale.UK);
				}
			}
		});

		
		

		
		setupTabHost();
		
		Resources res = getResources();
		mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);

				View tabview = createTabView(mTabHost.getContext(), "Map");

		TabHost.TabSpec spec;
		Intent intent;
		
		intent = new Intent(this,MainActivity.class);
		
		spec = mTabHost.newTabSpec("map")
				.setIndicator(tabview)
				.setContent(intent);
		mTabHost.addTab(spec);
		
		tabview = createTabView(mTabHost.getContext(), "Device");

		
		intent = new Intent(this,DeviceInformationActivity.class);
		spec = mTabHost.newTabSpec("Device")
				.setIndicator(tabview)
				.setContent(intent);
		mTabHost.addTab(spec);
		mTabHost.setCurrentTab(2);
		
		
	}
	/*
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab);
		
		setupTabHost();
		mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);

		tabhost = getTabHost();
		
		TabHost.TabSpec spec;
		Intent intent;
		
		intent = new Intent(this,MainActivity.class);
		
		spec = tabhost.newTabSpec("map")
				.setIndicator("Map")
				.setContent(intent);
		tabhost.addTab(spec);
		
		
intent = new Intent(this,DeviceInformationActivity.class);
		
		spec = tabhost.newTabSpec("Device")
				.setIndicator("Device")
				.setContent(intent);
		tabhost.addTab(spec);
		tabhost.setCurrentTab(2);
		
		
	}*/
}
