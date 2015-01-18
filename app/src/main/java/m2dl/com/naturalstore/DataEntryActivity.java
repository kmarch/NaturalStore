package m2dl.com.naturalstore;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import m2dl.com.naturalstore.mail.MultiThread;
import m2dl.com.naturalstore.mail.MultiThread;
import m2dl.com.naturalstore.parser.XMLReader;
import m2dl.com.naturalstore.parser.XMLSaver;


public class DataEntryActivity extends ActionBarActivity implements View.OnClickListener {

    private Spinner spinner;
    private XMLSaver xmlSaver;
    private XMLReader xmlInitializer;
    private TextView viewChoises;
    private TextView viewComment;
    private String gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);
        viewChoises = (TextView) findViewById(R.id.LabelSpinnerChoices);
        viewComment = (TextView) findViewById(R.id.CommentSetter);
        spinner = (Spinner) findViewById(R.id.SpinnerChoice);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        gps = bundle.getDouble(getResources().getString(R.string.picture_location_longitude)) + "";
        gps += ":" + bundle.getDouble(getResources().getString(R.string.picture_location_latitude));
        initComponentsValues();
    }

    private void initComponentsValues() {
        xmlInitializer =  new XMLReader(this);
        xmlSaver = new XMLSaver(this);
        String [] initArray;
        initArray = initSpinnerArray(xmlInitializer.getDoc().getFirstChild());
        ((Button) findViewById(R.id.SendButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.CancelButton)).setOnClickListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        setSpinnerValues(initArray);
        spinner.setOnItemSelectedListener(createListener());
        viewChoises.setText(R.string.defaultChoices);
        viewComment.setText(R.string.defaultComment);
    }

    private void setSpinnerValues(String[] initArray) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, initArray);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.SendButton) {
            xmlSaver.saveXML();
            new MultiThread(this,gps,viewComment.getText().toString()).execute("");
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            initComponentsValues();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    public void changeValues() {
        String itemSelect  = spinner.getSelectedItem().toString();
        Document doc = xmlInitializer.getDoc();
        String [] initArray;
        Node node = doc.getElementsByTagName(itemSelect).item(0);
        xmlSaver.appendNode(node.getNodeName());
        if(node != null && node.getChildNodes().getLength() == 1) {
            initArray = new String [1];
            initArray[0] = node.getFirstChild().getNodeValue();
            setSpinnerValues(initArray);
            xmlSaver.appendValue(initArray[0]);
        } else if(node != null){
            initArray = initSpinnerArray(node);
            setSpinnerValues(initArray);
        }
        spinner.setOnItemSelectedListener(createListener());
    }

    private String[] initSpinnerArray(Node node) {
        String[] initArray;
        initArray = new String[node.getChildNodes().getLength()+1];
        initArray[0] = getResources().getString(R.string.choiceMessage);
        for(int i = 1; i<= node.getChildNodes().getLength(); i++ ){
            initArray[i] = node.getChildNodes().item(i-1).getNodeName();
        }
        return initArray;
    }

    public AdapterView.OnItemSelectedListener createListener() {
        return new AdapterView.OnItemSelectedListener() {
            private int check = 0;

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (check!=0) {
                    viewChoises.append(spinner.getSelectedItem().toString()+'\n');
                    changeValues();

                }
                check++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        };
    }

}
