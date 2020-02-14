package com.example.ringtimer;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private TableLayout tblDetail;
    private int itemCount = 0;
    private TextView itemNum;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        TableLayout tblHead;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tblHead = (TableLayout)this.findViewById(R.id.table1);
        tblDetail = (TableLayout)this.findViewById(R.id.table2);
        Button btnAdd = (Button)this.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new btnAddClickListener());
        itemNum = (TextView) tblHead.findViewById(R.id.count);
        itemNum.setText(String.valueOf(itemCount).concat(getString(R.string.Items)));
        // for Ker-Value pair data
        preferences = getSharedPreferences("MyData", MODE_PRIVATE);
        //editor = preferences.edit();
        int count = preferences.getAll().size();
        for(int i = 0; i<count; i++){
            addRow();
        }
    }

    public void onDestroy()    {
        super.onDestroy();
        //save data into MyData
        editor = preferences.edit();
        editor.clear();
        int count = tblDetail.getChildCount();
        for(int i = 0; i<count; i++) {
            TableRow tr = (TableRow)tblDetail.getChildAt(i);
            TextView txv0 = (TextView)tr.getChildAt(0);
            TextView txv1 = (TextView)tr.getChildAt(1);
            TextView txv2 = (TextView)tr.getChildAt(2);
            editor.putString(i + "", txv0.getText().toString() + txv1.getText().toString() + txv2.getText().toString());
            //editor.commit();
            editor.apply();
        }
        }

    class btnAddClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View arg0)
        {
           addRow();
        }
    }

    @SuppressLint("SetTextI18n")
    private void addRow()
    {
        TableRow Row = new TableRow(this);

        //add Time Range
        TextView timeFrom = new TextView(this);
        TextView timeTo = new TextView(this);
        TextView hyphen = new TextView(this);
        timeFrom.setText("09:00");
        timeFrom.setGravity(Gravity.CENTER);
        timeTo.setText("18:00");
        timeTo.setGravity(Gravity.CENTER);
        hyphen.setText("-");
        hyphen.setGravity(Gravity.CENTER);
        Row.addView(timeFrom);
        Row.addView(hyphen);
        Row.addView(timeTo);

        //add  button
        Button btn = new Button(this);
        btn.setText(R.string.delete_code);
        btn.setOnClickListener(new View.OnClickListener()
                               {
                                   @Override
                                   public void onClick(View view)
                                   {
                                       TableRow tableRow=(TableRow)view.getParent();
                                       tblDetail.removeView(tableRow);
                                       itemCount--;
                                       itemNum.setText(String.valueOf(itemCount).concat(getString(R.string.Items)));
                                   }
                               }
        );
        Row.addView(btn);
        //add row
        Row.setBackgroundResource(R.color.c2);
        //Row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        tblDetail.addView(Row);
        itemCount++;
        itemNum.setText(String.valueOf(itemCount).concat(this.getString(R.string.Items)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
