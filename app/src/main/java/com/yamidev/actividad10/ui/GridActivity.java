package com.yamidev.actividad10.ui;

import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yamidev.actividad10.R;
import com.yamidev.actividad10.data.Photo;
import com.yamidev.actividad10.data.PhotoRepository;
import com.yamidev.actividad10.databinding.ActivityGridBinding;
import com.yamidev.actividad10.databinding.ItemThumbBinding;
import com.yamidev.actividad10.util.PermissionHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GridActivity extends AppCompatActivity {

    private ActivityGridBinding b;
    private final Set<Photo> selection = new HashSet<>();
    private ActionMode actionMode;
    private ThumbsAdapter adapter;

    private final ActivityResultLauncher<IntentSenderRequest> deleteLauncher =
            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
                // No-op; si el usuario confirma, el proveedor borra los ítems
                refresh();
            });

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityGridBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        b.toolbar.setNavigationOnClickListener(v -> finish());

        b.recycler.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new ThumbsAdapter(new ThumbsAdapter.OnItem() {
            @Override public void onClick(Photo p, int pos) {
                // abrir en ViewPager en esa posición
                Intent i = new Intent(GridActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
            @Override public void onLongClick(Photo p, int pos) { toggleSelect(p); }
        });
        b.recycler.setAdapter(adapter);

        if (!PermissionHelper.hasReadImages(this)) {
            PermissionHelper.requestReadImages(this);
        } else {
            refresh();
        }
    }

    private void refresh() {
        List<Photo> all = PhotoRepository.loadAll(getContentResolver());
        adapter.submitList(all);
    }

    private void toggleSelect(Photo p) {
        if (selection.contains(p)) selection.remove(p); else selection.add(p);
        adapter.setSelection(selection);
        if (selection.isEmpty()) {
            if (actionMode != null) actionMode.finish();
        } else {
            if (actionMode == null) actionMode = startActionMode(actionCb);
            if (actionMode != null) actionMode.setTitle(selection.size() + " seleccionadas");
        }
    }

    private final ActionMode.Callback actionCb = new ActionMode.Callback() {
        @Override public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getMenuInflater().inflate(R.menu.menu_content, menu);
            return true;
        }
        @Override public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }
        @Override public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.ctx_share) { shareSelected(); return true; }
            if (id == R.id.ctx_delete) { deleteSelected(); return true; }
            if (id == R.id.ctx_move) { /* TODO mover a álbum */ return true; }
            return false;
        }
        @Override public void onDestroyActionMode(ActionMode mode) {
            selection.clear();
            adapter.setSelection(selection);
            actionMode = null;
        }
    };

    private void shareSelected() {
        ArrayList<Uri> uris = new ArrayList<>();
        for (Photo p : selection) uris.add(p.uri);
        Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
        i.setType("image/*");
        i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(i, "Compartir fotos"));
    }

    private void deleteSelected() {
        ArrayList<Uri> toDelete = new ArrayList<>();
        for (Photo p : selection) toDelete.add(p.uri);

        ContentResolver cr = getContentResolver();
        try {
            IntentSender sender = MediaStore.createDeleteRequest(cr, toDelete).getIntentSender();
            deleteLauncher.launch(new IntentSenderRequest.Builder(sender).build());
        } catch (RecoverableSecurityException ex) {
            deleteLauncher.launch(new IntentSenderRequest.Builder(ex.getUserAction().getActionIntent().getIntentSender()).build());
        }
    }

    // ----- Adapter miniaturas -----
    static class ThumbsAdapter extends ListAdapter<Photo, ThumbsAdapter.VH> {
        interface OnItem { void onClick(Photo p, int pos); void onLongClick(Photo p, int pos); }

        private final OnItem cb;
        private Set<Photo> selected = new HashSet<>();

        protected ThumbsAdapter(OnItem cb) {
            super(DIFF);
            this.cb = cb;
        }

        void setSelection(Set<Photo> sel) { selected = new HashSet<>(sel); notifyDataSetChanged(); }

        @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(ItemThumbBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override public void onBindViewHolder(@NonNull VH h, int position) {
            Photo p = getItem(position);
            Glide.with(h.b.imgThumb.getContext()).load(p.uri).into(h.b.imgThumb);
            h.b.selectionOverlay.setVisibility(selected.contains(p) ? View.VISIBLE : View.GONE);
            h.itemView.setOnClickListener(v -> cb.onClick(p, position));
            h.itemView.setOnLongClickListener(v -> { cb.onLongClick(p, position); return true;});
        }

        static class VH extends RecyclerView.ViewHolder {
            ItemThumbBinding b; VH(ItemThumbBinding b) { super(b.getRoot()); this.b = b; }
        }

        static final DiffUtil.ItemCallback<Photo> DIFF = new DiffUtil.ItemCallback<Photo>() {
            @Override public boolean areItemsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) { return oldItem.id == newItem.id; }
            @Override public boolean areContentsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) { return oldItem.uri.equals(newItem.uri); }
        };
    }
}