package org.fossasia.phimpme.Favourites;

import static com.facebook.FacebookSdk.getApplicationContext;

import java.io.File;

import org.fossasia.phimpme.R;
import org.fossasia.phimpme.data.local.FavouriteImagesModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * Created by saurav on 7/10/17.
 */

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {

    private Realm realm = Realm.getDefaultInstance();
    private RealmQuery<FavouriteImagesModel> realmfav;
    private View.OnClickListener listener;
    private final ListItemClickListener listItemClickListener;

    public FavouritesAdapter(ListItemClickListener listItemClickListener){

        this.listItemClickListener = listItemClickListener;
    }

    public interface ListItemClickListener{
        void listItemClicked(int index);
    }


    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favourites_item_view, null, false);
      view.setOnClickListener(listener);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {

        realmfav = realm.where(FavouriteImagesModel.class);
        if(realmfav.findAll().size() !=0){

            Uri uri = Uri.fromFile(new File(realmfav.findAll().get(position).getPath()));
            Glide.with(getApplicationContext()).load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView);

        }
    }

    @Override public int getItemCount() {
        return (int) realmfav.count();
    }

    public void setOnClickListener(View.OnClickListener lis){
        listener = lis;
    }

    public void setResults(RealmQuery<FavouriteImagesModel> results) {
        realmfav = results;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.favimg)
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imageView.setOnClickListener(this);
        }

        @Override public void onClick(View view) {
            int pos = getAdapterPosition();
            listItemClickListener.listItemClicked(pos);

        }
    }
}