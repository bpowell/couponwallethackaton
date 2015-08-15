package edu.oakland.mysale.activities;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;

import edu.oakland.mysale.R;
import edu.oakland.mysale.adapters.CouponListAdapter;
import edu.oakland.mysale.utils.GpsLocationHandler;
import java.util.ArrayList;
import java.util.List;

import edu.oakland.mysale.R;
import go.gosale.Gosale;

@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity {

    go.gosale.Gosale.ThisAllBusinesses thisAllBusinesses;
    go.gosale.Gosale.ThisAllCategories thisAllCategories;
    private SimpleCursorAdapter searchAdapter;

    @ViewById(R.id.toolbar)
    public Toolbar toolbar;

    @AfterViews
    public void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("BranBarn@GoToJAIL.com");

        final String[] from = new String[] {"businesses"};
        final int[] to = new int[] {android.R.id.text1};
        searchAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @ViewById(R.id.list)
    ListView list;

    private GpsLocationHandler locationHandler;
    static List<List<Gosale.CouponsByLocation>> coupons;

    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        locationHandler = new GpsLocationHandler(this);
        getAllBusinesses();
        getAllCategories();
    }

    public void getStuff() {
        List<Gosale.CouponsByLocation> coupons = GpsLocationHandler.getCoupons();

        CouponListAdapter adapter = new CouponListAdapter(
                MainActivity.this,
                list,
                R.layout.header_card,
                coupons
        );

        list.setAdapter(adapter);
    }

    public void getStuffAndThings(List<Gosale.CouponsByLocation> coupons) {
        CouponListAdapter adapter = new CouponListAdapter(
                MainActivity.this,
                list,
                R.layout.header_card,
                coupons
        );

        list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));

        searchView.setSuggestionsAdapter(searchAdapter);
        searchView.setIconifiedByDefault(false);
        // Getting selected (clicked) item suggestion
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                Log.i("Search(Erik)", "Suggestion Clicked");
                Cursor cursor = (Cursor) searchAdapter.getItem(position);
                int index = cursor.getColumnIndex("businesses");
                String selectedString = cursor.getString(index);
                getStuffAndThings(getCouponsFromSearchPattern(selectedString));
                searchView.setQuery("", false);
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                Log.i("Search(Erik)", "Suggestion Selected");
                Cursor cursor = (Cursor) searchAdapter.getItem(position);
                int index = cursor.getColumnIndex("businesses");
                String selectedString = cursor.getString(index);
                getStuffAndThings(getCouponsFromSearchPattern(selectedString));
                searchView.setQuery("", false);
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.i("Search(Erik)", "Query Text Submit");
                String selectedString = s;
                getStuffAndThings(getCouponsFromSearchPattern(selectedString));
                searchView.setQuery("", false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.i("Search(Erik)", "Query Text Change");
                List<String> fuzzySearch = searchAllBusinessesNames(s);

                final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "businesses"});
                for (int i = 0; i < fuzzySearch.size(); i++) {
                    c.addRow(new Object[]{i, fuzzySearch.get(i)});
                    searchAdapter.changeCursor(c);
                }
                return false;
            }
        });
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

    public List<String> searchAllBusinessesNames(String pattern) {
        List<String> results = new ArrayList<>();

        thisAllBusinesses.Search(pattern);
        for(int i=0; i<thisAllBusinesses.SizeFiltered(); i++) {
            try {
                results.add(thisAllBusinesses.GetFiltered(i).getName());
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

    public List<Gosale.CouponsByLocation> getCouponsFromSearchPattern(String pattern) {
        List<Gosale.CouponsByLocation> list = new ArrayList<>();

        try {
            HashMap<String, Integer> alreadyThere = new HashMap<>();
            Gosale.ThisCouponsList c = Gosale.CouponsListInit(pattern);
            for (int i = 0; i < c.Size(); i++) {
                go.gosale.Gosale.CouponsByLocation a = c.Get(i);
                if(alreadyThere.containsKey(a.getId_business_id())) {
                    list.add(a);
                } else {
                    int size = list.size() + 1;
                    alreadyThere.put(a.getId_business_id(), size);
                    list.add(a);
                }
            }

        } catch(Exception e) {

        }
        return list;
    }
}
