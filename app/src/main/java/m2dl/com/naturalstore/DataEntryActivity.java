package m2dl.com.naturalstore;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class DataEntryActivity extends ActionBarActivity implements View.OnTouchListener{

    private Spinner spinner;
    private TextView comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);
        spinner = (Spinner) findViewById(R.id.SpinnerChoise);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        comment = (TextView) findViewById(R.id.CommentSetter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String[] arrayType = getResources().getStringArray(R.array.types);
                RelativeLayout dynamicLayout = (RelativeLayout) findViewById(R.id.DynalicLayout);
                dynamicLayout.removeAllViews();
                if (spinner.getSelectedItem().equals(arrayType[0])) {
                    addSizeOption(dynamicLayout);
                } else {
                    addAnimalTypeOption(dynamicLayout);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    public boolean onTouch(View v, MotionEvent event) {

        return true;
    }

    public void addSizeOption(RelativeLayout dynamicLayout) {
        TextView sizeLabel = new TextView(this);
        sizeLabel.setText("Taille");
        Spinner spinnerSize = new Spinner(this);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.plantsSize));
        dynamicLayout.addView(sizeLabel);
        spinnerSize.setAdapter(spinnerArrayAdapter);
        dynamicLayout.addView(spinnerSize);
    }

    public void addAnimalTypeOption(RelativeLayout dynamicLayout) {
        TextView typeLabel = new TextView(this);
        typeLabel.setText("Type");
        Spinner spinnerType = new Spinner(this);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.animalType));
        dynamicLayout.addView(typeLabel);
        spinnerType.setAdapter(spinnerArrayAdapter);
        dynamicLayout.addView(spinnerType);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_entry, menu);
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
