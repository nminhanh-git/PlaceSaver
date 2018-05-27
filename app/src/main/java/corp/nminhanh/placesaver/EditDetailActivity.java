package corp.nminhanh.placesaver;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.net.URI;

public class EditDetailActivity extends AppCompatActivity {

    MultiAutoCompleteTextView nameTextView;
    MultiAutoCompleteTextView descriptionTextView;
    ImageView imageView;
    Button saveButton;
    Button addPictureButton;

    Intent EditIntent;
    String type;
    String imagePath;
    DatabaseHandler database;
    Item anEditItem;

    boolean isOnEditMode = false;
    boolean ChangePictureClicked = false;

    final int IMAGE_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_edit);

        EditIntent = this.getIntent();
        type = EditIntent.getStringExtra("type");

        nameTextView = (MultiAutoCompleteTextView) findViewById(R.id.name_text_view);
        descriptionTextView = (MultiAutoCompleteTextView) findViewById(R.id.des_text_view);
        imageView = (ImageView) findViewById(R.id.item_image_edit);
        saveButton = (Button) findViewById(R.id.save_button);
        addPictureButton = (Button) findViewById(R.id.add_picture_button);

        if (EditIntent.getStringExtra("request").equals("add")) {
            saveButton.setText("add");
        } else if (EditIntent.getStringExtra("request").equals("edit")) {
            anEditItem = (Item) EditIntent.getSerializableExtra("edit item");
            saveButton.setText("save");
            addPictureButton.setText("change picture");
            showEditItemInformation();
            isOnEditMode = true;
        }

        database = new DatabaseHandler(this);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameTextView.getText().length() == 0) {
                    Toast.makeText(EditDetailActivity.this, "You need to add name of the item!", Toast.LENGTH_SHORT).show();
                } else if (EditIntent.getStringExtra("request").equals("add")) {
                    addItem();
                    finish();
                } else if (EditIntent.getStringExtra("request").equals("edit")) {
                    editItem();
                    finish();
                }
            }
        });

        addPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnEditMode) {
                    ChangePictureClicked = true;
                }

                if (ContextCompat.checkSelfPermission(EditDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(EditDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Toast.makeText(EditDetailActivity.this, "this app need permission to get the image you want for the item!", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(EditDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, IMAGE_PERMISSION_REQUEST_CODE);
                    }
//                     else {
//                        ActivityCompat.requestPermissions(EditDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, IMAGE_PERMISSION_REQUEST_CODE);
//                    }
                } else {
                    Intent getImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(getImageIntent, 0);
                }
            }
        });

    }

    public void addItem() {
        Item anItem = new Item();
        String name = nameTextView.getText().toString();
        String description = descriptionTextView.getText().toString();

        anItem.setName(name);
        anItem.setDescription(description);
        anItem.setType(type);
        anItem.setPath(imagePath);

        anItem.setId(database.AddItem(anItem));
    }

    public void editItem() {
        String name = nameTextView.getText().toString();
        String description = descriptionTextView.getText().toString();


        anEditItem.setName(name);
        anEditItem.setDescription(description);
        if(ChangePictureClicked) {
            anEditItem.setPath(imagePath);
        }

        int numberAffected = database.updateItem(anEditItem);
        if(numberAffected > 0){
            Toast.makeText(this, "updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public void showEditItemInformation() {
        nameTextView.setText(anEditItem.getName());
        descriptionTextView.setText(anEditItem.getDescription());

        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(anEditItem.getPath())));
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("image", "IMAGE NOT FOUND !!!!");
            Toast.makeText(this, "image not found!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            imagePath = targetUri.toString();
            addPictureButton.setText("Change picture");

            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(imagePath)));
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case IMAGE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent getImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(getImageIntent, 0);
                }
            }
        }
    }
}
