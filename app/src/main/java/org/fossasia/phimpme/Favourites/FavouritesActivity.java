package org.fossasia.phimpme.Favourites;

import static org.fossasia.phimpme.R.string.position;
import static org.fossasia.phimpme.R.string.size;

import java.io.File;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;

import org.fossasia.phimpme.R;
import org.fossasia.phimpme.base.ThemedActivity;
import org.fossasia.phimpme.data.local.FavouriteImagesModel;
import org.fossasia.phimpme.gallery.activities.SingleMediaActivity;
import org.fossasia.phimpme.gallery.util.Measure;
import org.fossasia.phimpme.gallery.util.PreferenceUtil;
import org.fossasia.phimpme.gallery.views.GridSpacingItemDecoration;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.squareup.haha.perflib.Instance;

public class FavouritesActivity extends ThemedActivity {

    @BindView(R.id.favourites_activity_recycler_view)
    RecyclerView favouritesRecyclerView;

    @BindView(R.id.toolbar1)
    Toolbar toolbar;

    @BindView(R.id.empty_icon)
    IconicsImageView emptyIcon;

    @BindView(R.id.emptyLayout)
    RelativeLayout emptyLayout;

    @BindView(R.id.empty_view_text)
    TextView emptyText;

    Realm realm;

    private FavouritesAdapter favouritesAdapter;

    private PreferenceUtil SP;
    private GridSpacingItemDecoration gridSpacingItemDecoration;

    private static Instance instance;

    private RealmQuery<FavouriteImagesModel> favouritesImages;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        ButterKnife.bind(this);
        setUpToolbar();
        SP = PreferenceUtil.getInstance(this);
        realm = Realm.getDefaultInstance();
        favouritesImages = realm.where(FavouriteImagesModel.class);
        int spanCount = SP.getInt("n_columns_media", 3);
        gridSpacingItemDecoration = new GridSpacingItemDecoration(spanCount, Measure.pxToDp(3, getApplicationContext()), true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        favouritesRecyclerView.setLayoutManager(gridLayoutManager);
        favouritesRecyclerView.addItemDecoration(gridSpacingItemDecoration);
        favouritesRecyclerView.setHasFixedSize(false);
        favouritesAdapter = new FavouritesAdapter(new FavouritesAdapter.ListItemClickListener() {
            @Override public void listItemClicked(int index) {
                Uri uri = Uri.fromFile(new File(favouritesImages.findAll().get(index).getPath()));
                String uri1 = String.valueOf(uri);
                Log.i("sdfsdfdf", uri1);
                String size = String.valueOf(new File(uri1).length());
                int size11 = Integer.parseInt(String.valueOf(favouritesImages.count()));

                int s = Integer.parseInt(size);
                Intent intent = new Intent(SingleMediaActivity.ACTION_OPEN_ALBUM, uri);
                intent.putExtra(getString(R.string.all_photo_mode), true);
                intent.putExtra(getString(R.string.position), index);
                intent.putExtra(getString(R.string.allMediaSize), size11);
                intent.setClass(getApplicationContext(), SingleMediaActivity.class);
                startActivity(intent);

            }
        });
        favouritesRecyclerView.setAdapter(favouritesAdapter);
        favouritesAdapter.setResults(favouritesImages);
        emptyIcon.setColor(getIconColor());
        emptyText.setTextColor(getAccentColor());

    }

    public static FavouritesActivity getInstance() {
        return getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (favouritesImages.findAll().size() == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            favouritesRecyclerView.setVisibility(View.GONE);
        }
    }



    private void setUpToolbar(){
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.favourite_activity_title);
        toolbar.setPopupTheme(getPopupToolbarStyle());
        toolbar.setBackgroundColor(getPrimaryColor());
        toolbar.setNavigationIcon(
                new IconicsDrawable(this)
                        .icon(CommunityMaterial.Icon.cmd_arrow_left)
                        .color(Color.WHITE)
                        .sizeDp(19));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
