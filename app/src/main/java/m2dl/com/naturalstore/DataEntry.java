package m2dl.com.naturalstore;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;


public class DataEntry extends ActionBarActivity implements View.OnTouchListener{

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
        comment.setOnTouchListener(this);
//        TextView name = new TextView(this);
//        name.setText("Kevin");
//        RelativeLayout dynamicLayout = (RelativeLayout) findViewById(R.id.DynalicLayout);
//        dynamicLayout.addView(name);
    }

    public boolean onTouch(View v, MotionEvent event) {
//        InputMethodManager inputMgr = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
//        inputMgr.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        return true;
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
