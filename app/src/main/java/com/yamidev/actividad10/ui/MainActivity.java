package com.yamidev.actividad10.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.yamidev.actividad10.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yamidev.actividad10.R;
import com.yamidev.actividad10.data.Photo;
import com.yamidev.actividad10.data.PhotoRepository;
import com.yamidev.actividad10.databinding.ActivityMainBinding;
import com.yamidev.actividad10.util.PermissionHelper;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding b;
    private final List<Photo> photos = new ArrayList<>();
    private PhotoPagerAdapter adapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        b.topBar.setNavigationOnClickListener(v ->
                startActivity(new Intent(this, GridActivity.class)));
        b.topBar.setOnMenuItemClickListener(this::onMenuClick);

        b.fabPrev.setOnClickListener(v -> move(-1));
        b.fabNext.setOnClickListener(v -> move(+1));

        if (!PermissionHelper.hasReadImages(this)) {
            PermissionHelper.requestReadImages(this);
        } else {
            loadPhotos();
        }
    }

    private boolean onMenuClick(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_share_current) {
            shareCurrent();
            return true;
        }
        return false;
    }

    private void shareCurrent() {
        int pos = b.viewPager.getCurrentItem();
        if (photos.isEmpty()) return;
        Photo p = photos.get(pos);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, p.uri);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Compartir foto"));
    }

    private void move(int delta) {
        int count = b.viewPager.getAdapter() != null ? b.viewPager.getAdapter().getItemCount() : 0;
        if (count == 0) return;
        int next = Math.max(0, Math.min(count - 1, b.viewPager.getCurrentItem() + delta));
        b.viewPager.setCurrentItem(next, true);
    }

    private void loadPhotos() {
        photos.clear();
        photos.addAll(PhotoRepository.loadAll(getContentResolver()));
        if (photos.isEmpty()) {
            Toast.makeText(this, "Sin fotos en el dispositivo", Toast.LENGTH_SHORT).show();
        }
        adapter = new PhotoPagerAdapter(photos);
        b.viewPager.setAdapter(adapter);

        new TabLayoutMediator(b.tabDots, b.viewPager, (tab, position) -> {
            tab.setText(""); // puntos sin texto
        }).attach();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] perms, @NonNull int[] res) {
        super.onRequestPermissionsResult(requestCode, perms, res);
        if (requestCode == PermissionHelper.REQ_READ) {
            if (PermissionHelper.hasReadImages(this)) loadPhotos();
            else Toast.makeText(this, "Permiso requerido para mostrar fotos", Toast.LENGTH_LONG).show();
        }
    }
}