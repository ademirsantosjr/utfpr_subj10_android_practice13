package br.edu.utfpr.contextmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ListView listViewNames;
    private EditText editTextName;
    private ImageButton imageButton1, imageButton2;

    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;

    private int editedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewNames = findViewById(R.id.listViewNames);
        editTextName = findViewById(R.id.editTextName);
        imageButton1 = findViewById(R.id.imageButton1);
        imageButton2 = findViewById(R.id.imageButton2);

        imageButton2.setVisibility(View.INVISIBLE);

        populateList();

        registerForContextMenu(listViewNames);
    }

    private void populateList() {
        list = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

        listViewNames.setAdapter(adapter);
    }

    public void add(View view) {
        String phrase = editTextName.getText().toString();

        if (phrase.trim().isEmpty()) {
            return;
        }

        editTextName.setText(null);

        if (editedPosition == -1) {
            list.add(phrase);
        } else {
            list.remove(editedPosition);
            list.add(editedPosition, phrase);

            imageButton1.setImageResource(android.R.drawable.ic_input_add);
            imageButton2.setVisibility(View.INVISIBLE);

            editedPosition = -1;

            listViewNames.setEnabled(true);
        }

        Collections.sort(list);

        adapter.notifyDataSetChanged();
    }

    public void cancel(View view) {
        editTextName.setText(null);

        imageButton1.setImageResource(android.R.drawable.ic_input_add);
        imageButton2.setVisibility(View.INVISIBLE);

        listViewNames.setEnabled(true);

        editedPosition = -1;
    }

    private void edit(int position) {
        String phrase = list.get(position);

        editTextName.setText(phrase);
        editTextName.setSelection(editTextName.getText().length());

        imageButton1.setImageResource(android.R.drawable.ic_menu_save);
        imageButton2.setVisibility(View.VISIBLE);

        listViewNames.setEnabled(false);

        editedPosition = position;
    }

    private void delete(int position) {
        list.remove(position);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.main_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;

        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){
            case R.id.menuItemEdit:
                edit(info.position);
                return true;

            case R.id.menuItemDelete:
                delete(info.position);
                return true;

            default:
                return super.onContextItemSelected(item);
        }

    }
}