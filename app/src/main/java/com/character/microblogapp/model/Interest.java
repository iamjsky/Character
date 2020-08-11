package com.character.microblogapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Interest implements Parcelable {
    public int uid;
    public String name;
    public ArrayList<Child> child = new ArrayList<>();

    public class Child {
        public int uid;
        public String name;
        public boolean selected;

        public Child(JSONObject object) {
            try {
                uid = object.getInt("uid");
                name = object.getString("name");
                selected = object.getBoolean("selected");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    Interest(Parcel in) {
        uid = in.readInt();
        name = in.readString();
        String serializedJson = in.readString();
        try {
            JSONArray jArray = new JSONArray(serializedJson);
            for (int ind = 0; ind < jArray.length(); ind++) {
                Interest.Child tmp = new Interest.Child(jArray.getJSONObject(ind));
                child.add(tmp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static final Creator<Interest> CREATOR = new Creator<Interest>() {
        @Override
        public Interest createFromParcel(Parcel in) {
            return new Interest(in);
        }

        @Override
        public Interest[] newArray(int size) {
            return new Interest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeString(name);
        Gson gson = new Gson();
        String serializedJson = gson.toJson(child);
        dest.writeString(serializedJson);

    }

    public int selectedChildCount() {
        int count = 0;
        for (Interest.Child item : child) {
            if (item.selected) {
                count++;
            }
        }

        return count;
    }
}