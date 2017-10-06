package org.fossasia.phimpme.data.local;

import static android.R.id.primary;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by saurav on 6/10/17.
 */

public class FavouriteImagesModel extends RealmObject {

    @PrimaryKey
    private String path;

    public FavouriteImagesModel(){

    }

    public FavouriteImagesModel(String path, String description){

        this.path = path;
    }

    public void setPath(String path){

        this.path = path;
    }

    public String getPath(){

        return path;
    }

}
