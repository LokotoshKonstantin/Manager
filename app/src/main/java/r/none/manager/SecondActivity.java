package r.none.manager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;



public class SecondActivity extends AppCompatActivity {
    private DataBase dbHelper;
    private ItemsAdapter adapter;
    private String tableName;
    static class Item {
        String product;
        String price="0";

        Item(String product, String price) {
            this.product = product;
            this.price = price;
        }
    }
    private void saveChanges(String prd,String prc){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("list",prd+","+prc);
        Log.d("Tabler",prd+","+prc);
        db.insert(tableName, null, cv);
    }

    private void readTable(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try{
        Cursor c =db.query(tableName, null, null, null, null, null, null);
        if(c.moveToFirst()){
            int ls_ind = c.getColumnIndex("list");
            do{
                String ls = c.getString(ls_ind);
                Log.d("TableReaded",ls);
                String[] part = ls.split(",");
               adapter.add(new Item(part[0],part[1]));
            } while (c.moveToNext());
        }}
        catch (Exception e){
            Log.d("Table", "Creating Table///");
            db.execSQL("create table "+tableName+" ("
                    + "id integer primary key autoincrement,"
                    + "list text"
                    +");");
            readTable();
        }
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);

        final EditText productSec = (EditText) findViewById(R.id.productSec);
        final EditText priceSec = (EditText) findViewById(R.id.priceSec);
        final Button addSec = (Button) findViewById(R.id.addSec);
        final ListView items2 = (ListView) findViewById(R.id.items);

        tableName=(String)getIntent().getSerializableExtra("TableName");
        dbHelper = new DataBase(this);
        adapter = new ItemsAdapter();
        readTable();
        this.setTitle(tableName);//getResources().getText(R.id.name));
        items2.setAdapter(adapter);
        addSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.add(new Item(productSec.getText().toString(), (priceSec.getText().toString())));
                saveChanges(productSec.getText().toString(),priceSec.getText().toString().equals("")?"0 ":priceSec.getText().toString());
            }
        });
    }

    private class ItemsAdapter extends ArrayAdapter<Item> {
        ItemsAdapter() {
            super(SecondActivity.this, R.layout.item);
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            @SuppressLint("ViewHolder") final View view = getLayoutInflater().inflate(R.layout.item, null);
            final Item item = getItem(position);
            final TextView nameTbl = (TextView) view.findViewById(R.id.name);
            nameTbl.setText(item.product);
            final TextView priceTxt = (TextView) view.findViewById(R.id.price);
            priceTxt.setText(item.price + " руб.");
            Button nextBtn = (Button) view.findViewById(R.id.nextTable);
            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToNextActivity(item.product,item.price);

                }
            });
            return view;
        }
    }

    public void onBack(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void goToNextActivity(String nm, String pr) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("TableName",nm);
        intent.putExtra("PriceTable",pr);
        startActivity(intent);
    }
}



