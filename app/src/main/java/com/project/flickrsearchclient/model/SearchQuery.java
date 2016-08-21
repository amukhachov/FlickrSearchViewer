package com.project.flickrsearchclient.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchQuery implements Parcelable {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss, d MMM yyyy");

    private final String mQuery;
    private final long mTimestamp;

    public SearchQuery(String query) {
        mQuery = query;
        mTimestamp = System.currentTimeMillis();
    }

    public SearchQuery(String query, long timestamp) {
        mQuery = query;
        mTimestamp = timestamp;
    }

    public String getQuery() {
        return mQuery;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public String getReadableDate() {
        return DATE_FORMAT.format(new Date(mTimestamp));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mQuery);
        dest.writeLong(this.mTimestamp);
    }

    protected SearchQuery(Parcel in) {
        this.mQuery = in.readString();
        this.mTimestamp = in.readLong();
    }

    public static final Parcelable.Creator<SearchQuery> CREATOR = new Parcelable.Creator<SearchQuery>() {
        @Override
        public SearchQuery createFromParcel(Parcel source) {
            return new SearchQuery(source);
        }

        @Override
        public SearchQuery[] newArray(int size) {
            return new SearchQuery[size];
        }
    };
}
