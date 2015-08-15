package edu.oakland.mysale.activities;

import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.List;

import edu.oakland.mysale.R;
import go.gosale.Gosale;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {
    go.gosale.Gosale.ThisAllBusinesses thisAllBusinesses;
    go.gosale.Gosale.ThisAllCategories thisAllCategories;

    //Only to be called once!
    @Background
    public void getAllBusinesses() {
        try {
            thisAllBusinesses = go.gosale.Gosale.AllBusinessesInit();
        }catch(Exception e) {

        }
    }

    //Only to be called once!
    @Background
    public void getAllCategories() {
        try{
            thisAllCategories = go.gosale.Gosale.AllCategoriesInit();
        }catch (Exception e) {

        }
    }

    public List<Gosale.AllBusinesses> searchAllBusinesses(String pattern) {
        List<Gosale.AllBusinesses> results = new ArrayList<>();

        thisAllBusinesses.Search(pattern);
        for(int i=0; i<thisAllBusinesses.SizeFiltered(); i++) {
            try {
                results.add(thisAllBusinesses.GetFiltered(i));
            }catch (Exception e) {

            }
        }

        return results;
    }

    public List<Gosale.AllCategories> searchAllCategories(String pattern) {
        List<Gosale.AllCategories> results = new ArrayList<>();

        thisAllCategories.Search(pattern);
        for(int i=0; i<thisAllCategories.SizeFiltered(); i++) {
            try {
                results.add(thisAllCategories.GetFiltered(i));
            }catch (Exception e) {

            }
        }

        return results;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllBusinesses();
        getAllCategories();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
