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

public class OrderDetail extends AppCompatActivity
{
    String[] orderDetailList;
    InstanceInfo info;
    String orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ListView detailList = ((ListView) findViewById(R.id.detailList));
        orderID = getIntent().getStringExtra("OrderId");
        info = InstanceInfo.getInstance();
        sendPostData();
        CustomAdapter adapter = new CustomAdapter(orderDetailList);
        detailList.setAdapter(adapter);
    }

    private void sendPostData()
    {
        PostSender postSender = new PostSender();

        try
        {
            String data = URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode("getOrders", "UTF-8");
            data += "&" + URLEncoder.encode("orderID", info.getEncodeFormat()) + "=" + URLEncoder.encode(orderID, info.getEncodeFormat());

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
        orderDetailList = serverResponse.split("\n");
    }


    static class ViewHolder
    {
        TextView detailText;
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
                convertView = getLayoutInflater().inflate(R.layout.custom_detail_layout, null);
                holder = new ViewHolder();

                holder.detailText = convertView.findViewById(R.id.detailText);

                convertView.setTag(holder);
            }
            else
            {
                holder = ((ViewHolder) convertView.getTag());
            }

            holder.detailText.setText(items[position].split(",")[0] + " x" + items[position].split(",")[1] + " @ $" + items[position].split(",")[2]);

            return convertView;
        }
    }
}
