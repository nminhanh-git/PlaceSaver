package corp.nminhanh.placesaver;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClothesFragment extends Fragment {

    public ClothesFragment() {
        // Required empty public constructor
    }

    ListView itemListView;

    DatabaseHandler database;
    ArrayList<Item> itemArrayList = new ArrayList<Item>();
    ItemAdapter adapter;
    Item anEditItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_list, container, false);

        database = new DatabaseHandler(this.getContext());
        itemListView = (ListView) rootView.findViewById(R.id.list_view);
        adapter = new ItemAdapter(getActivity(), itemArrayList);
        itemListView.setAdapter(adapter);

        itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Item temp = (Item) parent.getItemAtPosition(position);
                anEditItem = new Item();
                anEditItem.setId(temp.getId());
                anEditItem.setType(temp.getType());
                anEditItem.setName(temp.getName());
                anEditItem.setDescription(temp.getDescription());
                anEditItem.setPath(temp.getPath());


                return false;
            }
        });

        refreshList();

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerForContextMenu(itemListView);
        refreshList();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (getUserVisibleHint()) {
            switch (item.getItemId()) {
                case R.id.edit_item:
                    Intent editIntent = new Intent(getActivity(), EditDetailActivity.class);
                    editIntent.putExtra("edit item", anEditItem);
                    editIntent.putExtra("request", "edit");
                    startActivity(editIntent);
                    return true;
                case R.id.delete_item:
                    showWarningDeleteDialog();
                    return true;
            }
        }
        return super.onContextItemSelected(item);
    }

    public void refreshList() {
        itemArrayList.clear();
        itemArrayList.addAll(database.getAllQueryItem("clothes"));
        adapter.notifyDataSetChanged();
    }

    public void showWarningDeleteDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.deleteItem(anEditItem);
                refreshList();
                Toast.makeText(getActivity(), "Deleted!", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setMessage("This item will be deleted");
        AlertDialog deleteDialog = dialogBuilder.create();
        deleteDialog.show();
    }
}
