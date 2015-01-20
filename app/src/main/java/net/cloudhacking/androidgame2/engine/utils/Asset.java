package net.cloudhacking.androidgame2.engine.utils;

/**
 * Created by Andrew on 1/17/2015.
 */
public class Asset {

    // idea: put this and resources.java in a registry package

    private String mAssetFileName;

    public Asset(String assetFileName) {
        mAssetFileName = assetFileName;
    }

    public String getFileName() {
        return mAssetFileName;
    };

}
