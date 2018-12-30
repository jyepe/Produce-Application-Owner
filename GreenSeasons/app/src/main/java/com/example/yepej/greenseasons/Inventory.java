package com.example.yepej.greenseasons;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URLEncoder;

public class Inventory extends AppCompatActivity
{

    String[] inventoryList;
    InstanceInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        info = InstanceInfo.getInstance();
        ListView inventoryListView = ((ListView) findViewById(R.id.inventoryListView));
        sendPostData();
        CustomAdapter adapter = new CustomAdapter(inventoryList);
        inventoryListView.setAdapter(adapter);
    }

    private void sendPostData()
    {
        PostSender postSender = new PostSender();

        try
        {
            String data = URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode("getItems", "UTF-8");
            data += "&" + URLEncoder.encode("type", info.getEncodeFormat()) + "=" + URLEncoder.encode("getOnHand", info.getEncodeFormat());

            String serverResponse = postSender.execute("http://" + info.getServerIP() + "/ds.php", data).get();
            parseResponse(serverResponse);
        }
        catch (Exception e)
        {
            Log.i("fail", e.toString());
        }
    }

    private void parseResponse(String serverResponse)
    {
        serverResponse = serverResponse.replace("end", "").replace("start", "");
        inventoryList = serverResponse.split("\n");
    }

    static class ViewHolder
    {
        TextView inventoryText;
    }

    //This class is used to create a custom listview
    public class CustomAdapter extends BaseAdapter
    {

        String[] items;

        public CustomAdapter(String[] list)
        {
            items = list;
        }

        @Override
        public int getCount()
        {
            return items.length;
        }

        @Override
        public Object getItem(int position)
        {
            return position;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder = null;

            if (convertView == null)
            {
                convertView = getLayoutInflater().inflate(R.layout.custom_inventory_layout, null);
                holder = new ViewHolder();

                holder.inventoryText = convertView.findViewById(R.id.inventoryText);

                convertView.setTag(holder);
            }
            else
            {
                holder = ((ViewHolder) convertView.getTag());
            }

            holder.inventoryText.setText(items[position].split(",")[0] + " x" + items[position].split(",")[1]);

            return convertView;
        }
    }
}
