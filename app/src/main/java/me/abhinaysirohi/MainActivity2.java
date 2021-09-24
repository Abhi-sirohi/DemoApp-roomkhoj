package me.abhinaysirohi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity2 extends AppCompatActivity {

    RecyclerView rv;
    MyPageAdapter adapter;
    DatabasePagingOptions<Messdemo1> optionsnew1;
    Boolean checkadapter = true;
    MyAdapter adapter2;
    ProgressBar P;
    TextView tvemptydata;
    Query baseQuery;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); //For night mode theme
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); //For day mode theme
        setContentView(R.layout.activity_main2);

        P = (ProgressBar) findViewById(R.id.progress);
        tvemptydata = (TextView) findViewById(R.id.tvemptydata);

        //LayoutManager set
        rv = (RecyclerView) findViewById(R.id.recview);
        LinearLayoutManager lv = new LinearLayoutManager(this);
        rv.setLayoutManager(lv);
        mSwipeRefreshLayout = findViewById(R.id.swipereferesh) ;
        mSwipeRefreshLayout.setColorSchemeResources(R.color.pcolorlight,R.color.pcolor);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            checkadapter=true;
            adapter.refresh();
            switchAdapter();

        });

        switchAdapter();
        //Reading data from firebase db
//        FirebaseRecyclerOptions<Messdemo1> options =
//                new FirebaseRecyclerOptions.Builder<Messdemo1>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("classic").orderByChild("tname"), Messdemo1.class).build();


        //set Adapter
//        adapter = new MyPageAdapter(optionsnew1, P, tvemptydata,mSwipeRefreshLayout);
//        rv.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int totalNumberOfItem = adapter.getItemCount();
//                Log.d("abhi", "onItemRangeInserted: itemcount =   "+totalNumberOfItem);

            }
        });


    }

    private DatabasePagingOptions<Messdemo1> readingData() {
        // The "base query" is a query with no startAt/endAt/limit clauses that the adapter can use
// to form smaller queries for each page.
        baseQuery = FirebaseDatabase.getInstance().getReference().child("classic");

// This configuration comes from the Paging Support Library
// https://developer.android.com/reference/androidx/paging/PagedList.Config
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(10)
                .setPrefetchDistance(5)
                .setPageSize(10)
                .build();

// The options for the adapter combine the paging configuration with query information
// and application-specific options for lifecycle, etc.
        DatabasePagingOptions<Messdemo1> optionsnew = new DatabasePagingOptions.Builder<Messdemo1>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery, config, Messdemo1.class)
                .build();
        return optionsnew;
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        adapter.stopListening();
    }
    public void switchAdapter(){
//        checkadapter=true;
        optionsnew1 = readingData();
        adapter = new MyPageAdapter(optionsnew1, P, tvemptydata,mSwipeRefreshLayout,checkadapter);
        rv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()!=0){
                    searchreasult(query);
                    return false;

                }else {
                    checkadapter=false;
                    adapter.refresh();
                    switchAdapter();
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length()!=0){
                searchreasult(newText);
                return false;

                }else {
                    checkadapter=false;
                    adapter.refresh();
                    switchAdapter();
                    return false;
                }

            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    private void searchreasult(String query) {
        if (query.length()==0){
            checkadapter =true;
            tvemptydata.setVisibility(View.GONE);
            switchAdapter();
        }else {

            FirebaseRecyclerOptions<Messdemo1> options =
                    new FirebaseRecyclerOptions.Builder<Messdemo1>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("classic").orderByChild("tname").startAt(query).endAt(query + "\uf8ff"), Messdemo1.class)
                            .build();
            checkadapter=false;
            adapter2 = new MyAdapter(options, P, tvemptydata,checkadapter);
            adapter2.startListening();
            rv.setAdapter(adapter2);
        }
    }
}