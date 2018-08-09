package com.example.yepej.greenseasons;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;



public class OptionActivity extends AppCompatActivity
{
    InstanceInfo info;
    String[] orders;
    ListView orderListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        orderListView = ((ListView) findViewById(R.id.orders));
        setListListner(orderListView);
        info = InstanceInfo.getInstance();
        sendPostdata();
    }

    private void setListListner(ListView orderList)
    {
        orderList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent myIntent = new Intent(getApplicationContext(), OrderDetail.class);
                myIntent.putExtra("OrderId", orders[i].split(",")[1]);
                startActivity(myIntent);
            }
        });
    }

    private void sendPostdata()
    {
        PostSender postSender = new PostSender();

        try
        {
            String data = URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode("getMasterOrders", "UTF-8");

            String serverResponse = postSender.execute("http://" + info.getServerIP() + "/ds.php", data).get();
            parseResponse(serverResponse);
        }
        catch (Exception e)
        {
            Log.i("fail", e.toString());
        }
    }

    private void parseResponse(String response)
    {
        if (!response.equalsIgnoreCase("0 results\n"))
        {
            orders = response.split("\n");
            CustomAdapter adapter = new CustomAdapter(orders);
            orderListView.setAdapter(adapter);
        }
        else
        {
            TextView noOrdersText = ((TextView) findViewById(R.id.noOrdersText));
            noOrdersText.setAlpha(1f);
        }
    }

    static class ViewHolder
    {
        TextView customerLabel;
        TextView orderLabel;
        TextView totalLabel;
        TextView contactLabel;

        TextView customer;
        TextView order;
        TextView total;
        TextView contact;
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
                convertView = getLayoutInflater().inflate(R.layout.custom_layout, null);
                holder = new ViewHolder();

                holder.customerLabel = convertView.findViewById(R.id.customerNameLabel);
                holder.orderLabel = convertView.findViewById(R.id.orderIDLabel);
                holder.totalLabel = convertView.findViewById(R.id.priceLabel);
                holder.contactLabel = convertView.findViewById(R.id.contactNameLabel);

                holder.customer = convertView.findViewById(R.id.customerName);
                holder.order = convertView.findViewById(R.id.orderNumber);
                holder.total = convertView.findViewById(R.id.total);
                holder.contact = convertView.findViewById(R.id.contactName);

                convertView.setTag(holder);
            }
            else
            {
                holder = ((ViewHolder) convertView.getTag());
            }

            holder.contact.setText(items[position].split(",")[0]);
            holder.order.setText(items[position].split(",")[1]);
            holder.total.setText("$" + items[position].split(",")[2]);
            holder.customer.setText(items[position].split(",")[3]);

            return convertView;
        }
    }
}
