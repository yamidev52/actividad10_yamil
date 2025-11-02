package com.yamidev.actividad10.data;

import android.net.Uri;

public class Photo {
    public final long id;
    public final Uri uri;
    public final String displayName;
    public final String description;

    public Photo(long id, Uri uri, String displayName, String description) {
        this.id = id;
        this.uri = uri;
        this.displayName = displayName;
        this.description = description;
    }
}