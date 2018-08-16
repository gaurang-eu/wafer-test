package com.wafermessenger.gaurangpatel;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wafermessenger.gaurangpatel.data.CountryData;
import com.wafermessenger.gaurangpatel.util.ServerUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import util.InternetUtil;

public class ListActivity extends AppCompatActivity {

    String url = "https://restcountries.eu/rest/v2/all";
    ArrayList<CountryData> countryDataArrayList;
    ListView lvCountry;
    RelativeLayout rlLoader;
    TextView tvMsg;
    ProgressBar pb;
    CountryAdapter countryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        lvCountry = (ListView) findViewById(R.id.list1);
        rlLoader = (RelativeLayout) findViewById(R.id.list_rl_loading);
        tvMsg = (TextView) findViewById(R.id.list_tv);
        pb = (ProgressBar) findViewById(R.id.list_progress);

//        countryAdapter =new CountryAdapter(ListActivity.this, R.layout.list_item, countryDataArrayList);
        countryDataArrayList = new ArrayList<CountryData>();

        // Create a ListView-specific touch listener. ListViews are given special treatment because
        // by default they handle touches for their list items... i.e. they're in charge of drawing
        // the pressed state (the list selector), handling list item clicks, etc.
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        lvCountry,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    countryAdapter.remove(countryAdapter.getItem(position));
                                }
                                countryAdapter.notifyDataSetChanged();
                            }
                        });

        lvCountry.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        lvCountry.setOnScrollListener(touchListener.makeScrollListener());


    }

    @Override
    protected void onResume() {
        super.onResume();
//        setAdapter();
        if ((new InternetUtil(ListActivity.this)).isNetworkAvailableSimple()) {
            showLoader();
            (new DataAsyncTask(this.url)).execute();
        } else {
            networkIssue("Some network issue, try later.");
        }


    }

    private void parseResponse(String strResponse) {

        try {
            JSONArray jArrCountry = new JSONArray(strResponse);
            int len = jArrCountry.length();
            countryDataArrayList = new ArrayList<CountryData>(len);
            for (int i = 0; i < len; i++) {
                JSONObject objCountry = jArrCountry.getJSONObject(i);
                CountryData cd = new CountryData();
                cd.setName(objCountry.getString("name"));
                JSONArray jaCurency = objCountry.getJSONArray("currencies");
                JSONArray jaLang = objCountry.getJSONArray("languages");
                cd.setCurrency(jaCurency.getJSONObject(0).getString("name"));
                cd.setLanguage(jaLang.getJSONObject(0).getString("name"));
                countryDataArrayList.add(cd);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setAdapter() {
        CountryData cd = new CountryData();
        cd.setName("India");
        cd.setCurrency("Rupe");
        cd.setLanguage("Hindi");
        CountryData cd2 = new CountryData();
        cd2.setName("Japan");
        cd2.setCurrency("Yen");
        cd2.setLanguage("Japaniess");
        CountryData cd3 = new CountryData();
        cd3.setName("USA");
        cd3.setCurrency("Dollor");
        cd3.setLanguage("English");
        countryDataArrayList.add(cd);
        countryDataArrayList.add(cd2);
        countryDataArrayList.add(cd3);

        hideLoader();

        countryAdapter = new CountryAdapter(ListActivity.this, R.layout.list_item, countryDataArrayList);
        lvCountry.setAdapter(countryAdapter);
    }

    private void showLoader() {
        rlLoader.setVisibility(View.VISIBLE);
        pb.setVisibility(View.VISIBLE);
        tvMsg.setText("Loading...");
        lvCountry.setVisibility(View.GONE);
    }

    private void hideLoader() {
        rlLoader.setVisibility(View.GONE);
        tvMsg.setText("Loading...");
        lvCountry.setVisibility(View.VISIBLE);
    }

    private void networkIssue(String msg) {
        rlLoader.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);
        tvMsg.setText(msg);
        lvCountry.setVisibility(View.GONE);
    }

    static class ViewHolder {
        private TextView tvCountry, tvCurrency, tvLanguage;

    }

    private class CountryAdapter extends ArrayAdapter<CountryData> {

        private int listItemLayout;

        public CountryAdapter(Context context, int layoutId, ArrayList<CountryData> itemList) {
            super(context, layoutId, itemList);
            this.listItemLayout = layoutId;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            return super.getView(position, convertView, parent);
            CountryData cd = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(listItemLayout, parent, false);
                viewHolder.tvCountry = (TextView) convertView.findViewById(R.id.tv_country);
                viewHolder.tvCurrency = (TextView) convertView.findViewById(R.id.tv_currency);
                viewHolder.tvLanguage = (TextView) convertView.findViewById(R.id.tv_language);

                convertView.setTag(viewHolder); // view lookup cache stored in tag
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // Populate the data into the template view using the data object
            viewHolder.tvCountry.setText(cd.getName());
            viewHolder.tvCurrency.setText(cd.getCurrency());
            viewHolder.tvLanguage.setText("Language:" + cd.getLanguage());

            // Return the completed view to render on screen
            return convertView;

        }
    }

    class DataAsyncTask extends AsyncTask<Void, Void, String> {

        String url;
        String Tag = DataAsyncTask.class.getSimpleName();
        boolean flag = false;

        public DataAsyncTask(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e(Tag, "onPreExecute");
        }

        @Override
        protected String doInBackground(Void... voids) {
            Log.e(Tag, "doInBackground");
            try {
                ServerUtil serverUtil = new ServerUtil();
                flag = true;
                return serverUtil.getDataFromUrl(this.url);
            } catch (Exception e) {
                networkIssue("Some error occured, try later");
                e.printStackTrace();
                return null;

            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(Tag, "onPostExecute");
            if (s == null) {
                networkIssue("No response from Server, try later");
            } else {
                Log.e(Tag, s);
                parseResponse(s);
                setAdapter();
//                Log.e(Tag, "List size = " + countryDataArrayList.size());
            }
        }
    }
}



