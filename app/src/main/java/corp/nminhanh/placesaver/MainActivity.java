package corp.nminhanh.placesaver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    int REQUEST_CODE = 1;
    String type;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager(), this);
        pager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.float_button);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (tabLayout.getSelectedTabPosition()) {
                    case 0:
                        type = "foods";
                        break;
                    case 1:
                        type = "clothes";
                        break;
                    case 2:
                        type = "places";
                        break;
                }
                Intent addIntent = new Intent(MainActivity.this, EditDetailActivity.class);
                addIntent.putExtra("request", "add");
                addIntent.putExtra("type", type);
                startActivityForResult(addIntent, REQUEST_CODE);
            }
        });
    }
}
