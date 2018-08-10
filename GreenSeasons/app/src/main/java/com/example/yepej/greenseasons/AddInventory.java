package com.example.yepej.greenseasons;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddInventory extends AppCompatActivity
{
    InstanceInfo info;
    String[] inventoryList;
    int[] selectionList;
    int[] colorList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory);

        info = InstanceInfo.getInstance();

        ListView listView = ((ListView) findViewById(R.id.inventoryListView));
        getItems();
        setColorsList();
        CustomAdapter adapter = new CustomAdapter(inventoryList);
        listView.setAdapter(adapter);
    }

    //Gets all inventory items from DB
    private void getItems()
    {
        PostSender sendPostData = new PostSender();

        try
        {
            String data = URLEncoder.encode("method", info.getEncodeFormat()) + "=" + URLEncoder.encode("getItems", info.getEncodeFormat());
            data += "&" + URLEncoder.encode("type", info.getEncodeFormat()) + "=" + URLEncoder.encode("", info.getEncodeFormat());
            String serverResponse = sendPostData.execute("http://" + info.getServerIP() + "/ds.php", data).get();
            parseResponse(serverResponse);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //Puts the data returned into an array
    private void parseResponse(String serverResponse)
    {
        //Searches for everything between start and end of the server response
        Pattern p = Pattern.compile("start(.*?)end");
        Matcher m = p.matcher(serverResponse);

        while (m.find())
        {
            inventoryList = m.group(1).split(",");
        }

        selectionList = new int[inventoryList.length];
        colorList = new int[inventoryList.length];
    }

    //Sets all elements in the colors array to black
    private void setColorsList()
    {
        for (int i = 0; i < colorList.length; i++)
        {
            colorList[i] = Color.BLACK;
        }
    }

    static class ViewHolder
    {
        TextView holderText;
        Spinner holderSpinner;
    }

    //region Custom Adapter
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
                convertView = getLayoutInflater().inflate(R.layout.custom_add_inventory_layout, null);
                holder = new ViewHolder();


                holder.holderText = ((TextView) convertView.findViewById(R.id.text));
                holder.holderSpinner = ((Spinner) convertView.findViewById(R.id.spinner));

                convertView.setTag(holder);
            }
            else
            {
                holder = ((ViewHolder) convertView.getTag());
            }

            holder.holderText.setText(items[position]);
            holder.holderText.setTag(position);
            //Saves its position in listview as a tag
            holder.holderSpinner.setTag(position);

            //Sets the spinner that is being rendered to its saved selection
            if (((int) holder.holderSpinner.getTag()) == position)
            {
                holder.holderSpinner.setSelection(selectionList[position]);
                holder.holderText.setTextColor(colorList[position]);
            }

            //Allows for ViewHolder to be passed into spinner listner
            final ViewHolder finalHolder = holder;


            holder.holderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int qtySelected, long id)
                {
                    int selectedItem = (int) finalHolder.holderSpinner.getTag();

                    if ((int)finalHolder.holderText.getTag() == selectedItem && qtySelected > 0)
                    {
                        finalHolder.holderText.setTextColor(Color.RED);
                    }
                    else
                    {
                        finalHolder.holderText.setTextColor(Color.BLACK);
                    }

                    selectionList[selectedItem] = qtySelected;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });

            return convertView;
        }
    }
    //endregion

    //region Custom Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            String data = URLEncoder.encode("method", info.getEncodeFormat()) + "=" + URLEncoder.encode("updateInventory", info.getEncodeFormat());
            int count = 0;


            for (int i = 0; i < selectionList.length; i++)
            {
                if (selectionList[i] != 0)
                {
                    count++;
                    data += "&" + URLEncoder.encode("qty" + count, info.getEncodeFormat()) + "=" + URLEncoder.encode(Integer.toString(selectionList[i]), info.getEncodeFormat());
                    data += "&" + URLEncoder.encode("itemID" + count, info.getEncodeFormat()) + "=" + URLEncoder.encode(Integer.toString(i + 1), info.getEncodeFormat());
                }
            }

            data += "&" + URLEncoder.encode("count", info.getEncodeFormat()) + "=" + URLEncoder.encode(Integer.toString(count), info.getEncodeFormat());


            PostSender sendPostData = new PostSender();
            String serverResponse = sendPostData.execute("http://" + info.getServerIP() + "/ds.php", data).get();
            Log.i("test", serverResponse);
            checkServerResponse(serverResponse);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return super.onOptionsItemSelected(item);
    }

    private void checkServerResponse(String response)
    {
        if (response.trim().equalsIgnoreCase("success"))
        {
            Toast.makeText(this, "Added inventory items", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Error in adding inventory items.", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion
}
