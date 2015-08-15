package edu.oakland.mysale.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.oakland.mysale.R;

/**
 * Created by Erik on 8/15/15.
 */
@EBean
public class ImageProcessing {
    public static void setImageFromUrl(ImageView imageView, String path, Target target) {
        try {
            Picasso.with(imageView.getContext())
                    .load(path)
                    .error(R.drawable.ic_broken_image)
                    .into(target);
            Picasso.with(imageView.getContext())
                    .load(path)
                    .error(R.drawable.ic_broken_image)
                    .into(imageView);
        } catch (Exception e) {
            Log.e("image", e.getMessage(), e);
        }
    }
    public static void setImageFromUrl(ImageView imageView, String path) {
        try {
            Picasso.with(imageView.getContext())
                    .load(path)
                    .error(R.drawable.ic_broken_image)
                    .into(imageView);
        } catch (Exception e) {
            Log.e("image", e.getMessage(), e);
        }
    }
}
