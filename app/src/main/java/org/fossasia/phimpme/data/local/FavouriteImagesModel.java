package org.fossasia.phimpme.data.local;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by saurav on 7/10/17.
 */

public class FavouriteImagesModel extends RealmObject {

    @PrimaryKey
    private String path;
    private String size;

    public FavouriteImagesModel(){

    }

    public FavouriteImagesModel(String path, String size){

        this.path = path;
        this.size = size;
    }

    public void setPath(String path){

        this.path = path;
    }

    public String getPath(){

        return path;
    }

    public void setSize(String size){
        this.size = size;

    }

    public String getSize(){
        return size;
    }

}
