package com.homework.oneprojecttorulethemall.mainmodule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseImageView;

public class FriendsAdapter extends BaseAdapter {
    private Context context;
    private GoogleMap map;

    public FriendsAdapter(Context context, GoogleMap map) {
        this.context = context;
        this.map = map;
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
        TextView location = (TextView) rl.findViewById(R.id.location);

        double latitude = UserSingleton.getInstance().getFriendList().get(i).getLocation().getLatitude();
        double longitude = UserSingleton.getInstance().getFriendList().get(i).getLocation().getLongitude();

        photo.setParseFile(UserSingleton.getInstance().getFriendList().get(i).getFriendPhoto());
        photo.loadInBackground();
        name.setText(UserSingleton.getInstance().getFriendList().get(i).getName());
        location.setText("Lat: " + latitude + "\nLon: " + longitude);
        map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(UserSingleton.getInstance().getFriendList().get(i).getName()));
        return rl;
    }
}
