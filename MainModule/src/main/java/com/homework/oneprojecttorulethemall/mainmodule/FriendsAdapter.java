package com.homework.oneprojecttorulethemall.mainmodule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FriendsAdapter extends BaseAdapter {
    private Context context;
    private String[] array = {"test1", "test2", "test3"};

    public FriendsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return array.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.drawer_list_item, viewGroup, false);

        TextView tv = (TextView) rl.findViewById(R.id.textView);
        tv.setText(array[i]);
        return rl;
    }
}
