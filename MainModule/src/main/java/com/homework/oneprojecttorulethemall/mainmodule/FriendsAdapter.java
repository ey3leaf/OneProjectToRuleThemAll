package com.homework.oneprojecttorulethemall.mainmodule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.parse.ParseImageView;

public class FriendsAdapter extends BaseAdapter {
    private Context context;

    public FriendsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return UserSingleton.getInstance().getFriendList().size();
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

        ParseImageView photo = (ParseImageView) rl.findViewById(R.id.friendsPhoto);
        TextView name = (TextView) rl.findViewById(R.id.friendsName);
        //TextView location = (TextView) rl.findViewById(R.id.location);

        photo.setParseFile(UserSingleton.getInstance().getFriendList().get(i).getFriendPhoto());
        photo.loadInBackground();
        name.setText(UserSingleton.getInstance().getFriendList().get(i).getName());
        return rl;
    }
}
