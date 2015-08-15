package edu.oakland.mysale.utils;

import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.App;

import java.util.logging.Logger;

import edu.oakland.mysale.R;

/**
 * Created by Erik on 8/15/15.
 */
public class ImageProcessing {
    public static void setImageFromUrl(ImageView imageView, String path) {
        try {
            Picasso.with(imageView.getContext())
                    .load(path)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);
        } catch (Exception e) {
            Log.e("image", e.getMessage(), e);
        }

    }

    public static void setImageFromDrawable(ImageView imageView, int resNo) {
        try {
            Picasso.with(imageView.getContext())
                    .load(resNo)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);
        } catch (Exception e) {
        }

    }
}
