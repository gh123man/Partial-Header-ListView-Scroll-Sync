package com.example.partialheaderlistviewscrollsync;

import java.util.Dictionary;
import java.util.Hashtable;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends Activity {

	private ListView listView;
	private View header;
	private int headerHeight, baseScrollHeight, lowerHeaderHeight;
	private LinearLayout floatingBarHeader;
	private Dictionary<Integer, Integer> listViewItemHeights;
	private boolean setScrollHeight, offsetSet;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setScrollHeight = false;
		offsetSet = false;

		listViewItemHeights = new Hashtable<Integer, Integer>();

		listView = (ListView) findViewById(R.id.listView);

		header = header();

		listView.addHeaderView(header, null, false);

		String[] strings = { "item1", "item2", "item3", "item4", "item5", "item6", "item7", "item8", "item9", "item10", "item11" };

		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strings);

		listView.setAdapter(adapter);

		listView.setOverScrollMode(View.OVER_SCROLL_NEVER);

		floatingBarHeader = (LinearLayout) findViewById(R.id.progBarFloat);

		setOffset();
		placeFloatingViewWhenReady();

		ViewTreeObserver vto = listView.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (!setScrollHeight) {
					baseScrollHeight = getScroll();
					setScrollHeight = true;
				}

			}
		});

		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				placeFloatingView();
			}
		});

	}

	private void placeFloatingView() {
		if (getScroll() < headerHeight + baseScrollHeight) {
			floatingBarHeader.setTop(-1 * getScroll() + baseScrollHeight);
		} else {
			floatingBarHeader.setTop(-1 * headerHeight);
		}
	}

	private int getScroll() {
		int scrollY = 0;
		View c = listView.getChildAt(0);
		if (c != null) {
			scrollY = -c.getTop();
			listViewItemHeights.put(listView.getFirstVisiblePosition(), c.getHeight());
			for (int i = 0; i < listView.getFirstVisiblePosition(); ++i) {
				if (listViewItemHeights.get(i) != null)
					scrollY += listViewItemHeights.get(i);
			}
		}
		return scrollY;
	}

	private void placeFloatingViewWhenReady() {
		View v = findViewById(R.id.progBarFloat);
		ViewTreeObserver vto = v.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				placeFloatingView();
			}
		});
	}

	private void setOffset() {

		final View v = findViewById(R.id.headerbottom);
		ViewTreeObserver vto = v.getViewTreeObserver();

		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {

				lowerHeaderHeight = v.getMeasuredHeight();
				ViewTreeObserver vto1 = header.getViewTreeObserver();

				vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {

						if (!offsetSet) {
							headerHeight = header.getMeasuredHeight() - lowerHeaderHeight;
							floatingBarHeader.setY(headerHeight);
							offsetSet = true;
						}

					}
				});
			}
		});

	}

	private View header() {
		View header = getLayoutInflater().inflate(R.layout.header, null);
		return header;
	}

}