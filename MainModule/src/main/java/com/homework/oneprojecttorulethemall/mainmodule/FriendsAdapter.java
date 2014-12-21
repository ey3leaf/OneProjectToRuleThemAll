package com.homework.oneprojecttorulethemall.mainmodule;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.*;

import java.util.Arrays;
import java.util.List;

public class FriendsAdapter extends BaseExpandableListAdapter {

    private Context context;
    private GoogleMap map;

    public FriendsAdapter(Context context, GoogleMap map) {
        this.context = context;
        this.map = map;
    }

    @Override
    public int getGroupCount() {
        return UserSingleton.getInstance().getFriendList().size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return UserSingleton.getInstance().getFriendList().get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
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

    @Override
    public View getChildView(final int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout childLayout = (LinearLayout) inflater.inflate(R.layout.drawer_child_view, viewGroup, false);

        final TextView chat = (TextView) childLayout.findViewById(R.id.chat);
        chat.setMovementMethod(new ScrollingMovementMethod());
        final EditText message = (EditText) childLayout.findViewById(R.id.message);
        Button send = (Button) childLayout.findViewById(R.id.send);

        getTextFromParse(i, chat);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chat.setText(chat.getText().toString() + UserSingleton.getInstance().getUser().getUsername() + ": " +
                        message.getText().toString() + "\n");
                saveMessageIntoParse(message.getText().toString(), UserSingleton.getInstance().getFriendList().get(i).getId());
                message.setText("");
            }
        });

        return childLayout;
    }

    private void saveMessageIntoParse(String message, String toID) {
        ParseObject parseMessage = new ParseObject("Messages");
        parseMessage.put("ID", ParseObject.createWithoutData("ID", UserSingleton.getInstance().getUser().getObjectId()));
        parseMessage.put("FROM", UserSingleton.getInstance().getUser().getUsername());
        parseMessage.put("TO", toID);
        parseMessage.put("IS_READ", false);
        parseMessage.put("MESSAGE", message);
        parseMessage.saveInBackground();
    }

    private void getTextFromParse(int position, final TextView chat) {

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Messages");
        String[] peopleIDs = {UserSingleton.getInstance().getUser().getObjectId(),
                UserSingleton.getInstance().getFriendList().get(position).getId()};
        query.whereContainedIn("TO", Arrays.asList(peopleIDs));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject parseObject : list) {
                        chat.setText(chat.getText() + parseObject.getString("FROM") + ": " + parseObject.getString("MESSAGE") + "\n");
                    }
                } else {
                    System.out.println(e);
                }
            }
        });
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
