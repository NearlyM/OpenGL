package com.ningerlei.previewdemo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ListActivity {

    private ArrayList<HashMap<String, String>> listItems;
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initData() {
        String[] names = new String[]{"SurfaceView", "GLSurfaceView", "SurfaceTexture", "TextureView"};
        listItems = new ArrayList<>();
        for(int i = 0; i < names.length; i++)    {
            HashMap<String, String> map = new HashMap<>();
            map.put("ItemTitle", names[i]);
            listItems.add(map);
        }
        simpleAdapter = new SimpleAdapter(this, listItems, R.layout.item_layout, new String[]{"ItemTitle"}, new int[]{R.id.name});
    }

    private void initView() {
        setListAdapter(simpleAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        switch (position){
            case 0:
                PreviewActivity.startActivity(this, SurfaceViewActivity.class);
                break;
            case 1:
                PreviewActivity.startActivity(this, GLSurfaceViewActivity.class);
                break;
            case 2:
                PreviewActivity.startActivity(this, SurfaceTextureActivity.class);
                break;
            case 3:
                PreviewActivity.startActivity(this, TextureViewActivity.class);
                break;

        }
    }
}
