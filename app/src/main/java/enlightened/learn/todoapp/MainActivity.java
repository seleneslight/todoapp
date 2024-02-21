package enlightened.learn.todoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> items = new ArrayList<String>();
    private Button btnAddItem;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String res = sharedPref.getString("meow", "");
        String[] itemsArray = res.split(",");
        items.addAll(Arrays.asList(itemsArray));
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        StringBuilder stringBuilder = new StringBuilder();
        for (String s: items) {
            stringBuilder.append(s).append(",");
        }
        editor.putString("meow",stringBuilder.toString());
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAddItem = findViewById(R.id.btnAddItem);
        lvItems = (ListView) findViewById(R.id.lvItems);

        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Czy wykonałeś poniższe zadanie?");
                        builder.setTitle("Wykonanie zadania");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Tak", (DialogInterface.OnClickListener) (dialog, which) -> {

                            items.remove(pos);
                            itemsAdapter.notifyDataSetChanged();
                        });
                        builder.setNegativeButton("Nie", (DialogInterface.OnClickListener) (dialog, which) -> {
                            dialog.cancel();
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }

                });
        btnAddItem.setOnClickListener(
                new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onAddItem(v);
                    }
                }
        );
    }


    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        if(!itemText.equals("")){
            itemsAdapter.add(itemText);
            etNewItem.setText("");
        }
    }

}