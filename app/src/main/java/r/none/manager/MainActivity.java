package r.none.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {
    private DataBase dbHelper;
    private ItemsAdapter adapter;
    static class Item {
        String name;
        String price = "0";

        Item(String name, String price) {
            this.name = name;
            this.price = price;
        }
    }

    private void saveChanges(String prd,String prc){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("list",prd+","+prc);
        db.insert("Start", null, cv);
    }

    private void readTable(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c =db.query("Start", null, null, null, null, null, null);
        if(c.moveToFirst()){
            int ls_ind = c.getColumnIndex("list");
            do{
                String ls = c.getString(ls_ind);
                String[] part = ls.split(",");
                adapter.add(new Item(part[0],part[1]));
            } while (c.moveToNext());
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        final EditText name = (EditText) findViewById(R.id.name);
        final String price = "0"; // final EditText price = (EditText) findViewById(R.id.price);
        final Button add = (Button) findViewById(R.id.add);
        final ListView items = (ListView) findViewById(R.id.items);
        adapter = new ItemsAdapter();
        dbHelper = new DataBase(this);
        readTable();
        items.setAdapter(adapter);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.add(new Item(name.getText().toString(), price)); //(price.getText().toString())));
                saveChanges(name.getText().toString(),price);

            }
        });
    }

    private class ItemsAdapter extends ArrayAdapter<Item> {
        ItemsAdapter() {
            super(MainActivity.this, R.layout.item);
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            @SuppressLint("ViewHolder") final View view = getLayoutInflater().inflate(R.layout.item, null);
            final Item item = getItem(position);
            final TextView nameTbl = (TextView) view.findViewById(R.id.name);
            nameTbl.setText(item.name);
            final TextView priceTxt = (TextView) view.findViewById(R.id.price);
            priceTxt.setText(item.price + " руб.");
            Button nextBtn = (Button) view.findViewById(R.id.nextTable);
            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToSecondActivity(nameTbl.getText().toString(),priceTxt.getText().toString());
                }
            });
            return view;
        }


    }

    public void goToSecondActivity(String nm, String pr) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("TableName",nm);
        intent.putExtra("PriceTable",pr);
        startActivity(intent);
    }

}
