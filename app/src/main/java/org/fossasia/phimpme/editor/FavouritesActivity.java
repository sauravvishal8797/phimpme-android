package org.fossasia.phimpme.editor;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import org.fossasia.phimpme.data.local.UploadHistoryRealmModel;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.squareup.haha.perflib.Instance;

public class FavouritesActivity extends ThemedActivity {

    @BindView(R.id.favourites_activity_recycler_view)
    RecyclerView favouritesRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.empty_icon)
    IconicsImageView emptyIcon;

    @BindView(R.id.emptyLayout)
    RelativeLayout emptyLayout;

    @BindView(R.id.empty_view_text)
    TextView emptyText;

    Realm realm;

    private FavouritesAdapter favouritesAdapter;

    private static Instance instance;

    private RealmQuery<FavouriteImagesModel> favouritesImages;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        ButterKnife.bind(this);
        setUpToolbar();
        favouritesAdapter = new FavouritesAdapter();
        realm = Realm.getDefaultInstance();
        favouritesImages = realm.where(FavouriteImagesModel.class);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        favouritesRecyclerView.setLayoutManager(gridLayoutManager);
        favouritesRecyclerView.setHasFixedSize(false);
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
