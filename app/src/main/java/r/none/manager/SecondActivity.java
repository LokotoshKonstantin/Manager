package r.none.manager;

import android.annotation.SuppressLint;
import android.content.Intent;
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



public class SecondActivity extends AppCompatActivity {
    static class Item {
        String productSec;
        String priceSec;

        Item(String product, String price) {
            this.productSec = product;
            this.priceSec = price;
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
        final ListView items2 = (ListView) findViewById(R.id.items2);
        final ItemsAdapter adapter = new ItemsAdapter();

        this.setTitle("PEMOHT");//getResources().getText(R.id.name));

        items2.setAdapter(adapter);
        addSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.add(new Item(productSec.getText().toString(), (priceSec.getText().toString())));

            }
        });
    }

    private class ItemsAdapter extends ArrayAdapter<Item> {
        ItemsAdapter() {
            super(SecondActivity.this, R.layout.item2);
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            @SuppressLint("ViewHolder") final View view = getLayoutInflater().inflate(R.layout.item2, null);
            final Item item = getItem(position);
            ((TextView) view.findViewById(R.id.productSec)).setText(item.productSec);
            ((TextView) view.findViewById(R.id.priceSec)).setText(String.valueOf(item.priceSec) + " руб.");
            return view;
        }
    }

    public void onBack(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}



