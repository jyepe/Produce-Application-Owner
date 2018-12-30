package com.example.yepej.greenseasons;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.net.URLEncoder;

public class Companies extends AppCompatActivity
{
    InstanceInfo info;
    String[] companiesArray;
    PostSender postSender = new PostSender();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme2);
        super.onCreate(savedInstanceState);
        info = InstanceInfo.getInstance();
        setContentView(R.layout.companies);
        ListView companiesListView = findViewById(R.id.companiesList);

        sendPostData();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                companiesArray);
        companiesListView.setAdapter(adapter);
        setListViewListner(companiesListView);
    }



    private void sendPostData()
    {
        try
        {
            String data = URLEncoder.encode("method", info.getEncodeFormat()) + "=" + URLEncoder.encode("getCompanies", info.getEncodeFormat());

            String serverResponse = postSender.execute("http://" + info.getServerIP() + "/ds.php", data).get();
            parseResponse(serverResponse);

        } catch (Exception e)
        {
            Log.i("fail", e.toString());
        }
    }

    private void parseResponse(String serverResponse)
    {
        companiesArray = serverResponse.split("\n");
    }

    private void setListViewListner(ListView companiesListView)
    {
        companiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent myIntent = new Intent(getApplicationContext(), ItemsList.class);
                myIntent.putExtra("UID", Integer.toString(i + 1));
                startActivity(myIntent);
            }
        });
    }
}
