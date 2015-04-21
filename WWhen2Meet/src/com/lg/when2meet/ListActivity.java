package com.lg.when2meet;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListActivity extends Activity {
	ListView listView;
	CustomAdapter adapter;
	ImageView add;
	ImageView del;

	ArrayList<DateClass> datelist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		ArrayList<String> data = new ArrayList<String>();
		String pinkColor = "#F5908D";
		//SharedPreferences setting = getSharedPreferences("login", 0);
		
		
		//data.add(setting.getString("id", "010"));
		data.add("room2");
		data.add("room3");

		adapter = new CustomAdapter(this, R.layout.room_list, data, false);
		listView = (ListView) findViewById(R.id.room_list);
		listView.setDivider(new ColorDrawable(Color.parseColor(pinkColor)));
		listView.setDividerHeight(4);
		listView.setAdapter(adapter);

		add = (ImageView) findViewById(R.id.add_room);
		del = (ImageView) findViewById(R.id.del_room);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Toast.makeText(ListActivity.this, "clicked",
				// Toast.LENGTH_SHORT).show();
				String pinkColor = "#F5908D";

				LinearLayout layout = (LinearLayout) view
						.findViewById(R.id.room_bg);
				layout.setBackgroundColor(Color.parseColor(pinkColor));
				ImageView icon = (ImageView) view.findViewById(R.id.room_icon);
				icon.setImageResource(R.drawable.room_icon_click);
				TextView name = (TextView) view.findViewById(R.id.room_name);
				name.setTextColor(Color.WHITE);
				TextView mem_list = (TextView) view.findViewById(R.id.room_mem);
				mem_list.setTextColor(Color.WHITE);

				/* datelist �ӽ� �߰� */
				datelist = new ArrayList<DateClass>();
				datelist.clear();
				datelist.add(new DateClass(21, 4, 2015, 1));
				datelist.add(new DateClass(22, 4, 2015, 2));

				Intent intent = new Intent(ListActivity.this,
						RoomActivity.class);
				Bundle bundle = new Bundle();

				bundle.putParcelableArrayList("datelist", datelist);
				bundle.putString("s_time", "4");
				bundle.putString("e_time", "7");
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				add.setImageResource(R.drawable.button_add_click);
				Intent intent = new Intent(ListActivity.this,
						CreateRoomActivity.class);
				startActivity(intent);
			}
		});

		del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				del.setImageResource(R.drawable.button_del_click);
				adapter.changeVisibility(true);
				adapter.notifyDataSetChanged();
				listView.setAdapter(adapter);
			}
		});
	}
}
