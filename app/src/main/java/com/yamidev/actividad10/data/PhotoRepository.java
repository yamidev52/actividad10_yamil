package com.yamidev.actividad10.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class PhotoRepository {

    private static Uri imagesCollection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // 1) intenta "external"; 2) si falla, "external_primary"; 3) interno
            try {
                return MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
            } catch (IllegalArgumentException ignore) {}
            try {
                return MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            } catch (IllegalArgumentException ignore) {}
            return MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_INTERNAL);
        } else {
            return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
    }

    public static List<Photo> loadAll(ContentResolver cr) {
        List<Photo> list = new ArrayList<>();

        Uri collection = imagesCollection();

        String[] projection = new String[] {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                // DATE_TAKEN está deprecado en API nuevas; usa DATE_ADDED como orden por defecto
                MediaStore.Images.Media.DESCRIPTION
        };

        Cursor c = null;
        try {
            c = cr.query(collection, projection, null, null,
                    MediaStore.Images.Media.DATE_ADDED + " DESC");
            if (c == null) return list;

            int idCol   = c.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int nameCol = c.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int descCol = c.getColumnIndex(MediaStore.Images.Media.DESCRIPTION);

            while (c.moveToNext()) {
                long id = c.getLong(idCol);
                Uri uri = ContentUris.withAppendedId(collection, id);
                String name = c.getString(nameCol);
                String desc = (descCol >= 0) ? c.getString(descCol) : "";
                list.add(new Photo(id, uri, name, desc));
            }
        } catch (IllegalArgumentException e) {
            // Si AÚN así el proveedor no tiene imágenes, devolvemos lista vacía
            // para que la app no crashee.
            e.printStackTrace();
        } finally {
            if (c != null) c.close();
        }
        return list;
    }
}