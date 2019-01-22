package com.example.yepej.greenseasons;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    InstanceInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info = InstanceInfo.getInstance();
        info.setServerIP("192.168.1.109");
        //info.setServerIP("10.1.10.73");
        setOptionListListner();
    }

    private void setOptionListListner()
    {
        ListView optionListView = ((ListView) findViewById(R.id.optionsList));

        optionListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (i == 0)
                {
                    Intent myIntent = new Intent(getApplicationContext(), OptionActivity.class);
                    startActivity(myIntent);
                }
                else if (i == 1)
                {
                    Intent myIntent = new Intent(getApplicationContext(), AddInventory.class);
                    startActivity(myIntent);
                }
                else if (i == 2)
                {
                    Intent myIntent = new Intent(getApplicationContext(), Inventory.class);
                    startActivity(myIntent);
                }
                else if (i == 3)
                {
                    Intent myIntent = new Intent(getApplicationContext(), Companies.class);
                    startActivity(myIntent);
                }
            }
        });
    }
}
