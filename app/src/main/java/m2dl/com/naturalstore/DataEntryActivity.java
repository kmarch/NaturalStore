package m2dl.com.naturalstore;

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

import m2dl.com.naturalstore.parser.XMLReader;
import m2dl.com.naturalstore.parser.XMLSaver;


public class DataEntryActivity extends ActionBarActivity implements View.OnTouchListener{

    private Spinner spinner;
    private XMLSaver xmlSaver;
    private XMLReader xmlInitializer;
    private TextView viewChoises;
    private TextView viewComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);
        viewChoises = (TextView) findViewById(R.id.LabelSpinnerChoices);
        viewComment = (TextView) findViewById(R.id.CommentSetter);
        spinner = (Spinner) findViewById(R.id.SpinnerChoice);
        initComponentsValues();
    }

    private void initComponentsValues() {
        xmlInitializer =  new XMLReader(this);
        xmlSaver = new XMLSaver(this);
        String [] initArray;
        initArray = initSpinnerArray(xmlInitializer.getDoc().getFirstChild());
        ((Button) findViewById(R.id.SendButton)).setOnTouchListener(this);
        ((Button) findViewById(R.id.CancelButton)).setOnTouchListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        setSpinnerValues(initArray);
        spinner.setOnItemSelectedListener(createListener());
        viewChoises.setText(R.string.defaultChoices);
        viewComment.setText(R.string.defaultComment);
    }

    private void setSpinnerValues(String[] initArray) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, initArray);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    public boolean onTouch(View v, MotionEvent event) {
        if(v.getId() == R.id.SendButton) {
            xmlSaver.saveXML();
        } else {
            initComponentsValues();
        }
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

    public void changeValues() {
        String itemSelect  = spinner.getSelectedItem().toString();
        Document doc = xmlInitializer.getDoc();
        String [] initArray;
        Node node = doc.getElementsByTagName(itemSelect).item(0);
        if(node != null && node.getChildNodes().getLength() == 1) {
            initArray = new String [1];
            initArray[0] = node.getFirstChild().getNodeValue();
            setSpinnerValues(initArray);
        } else if(node != null){
            initArray = initSpinnerArray(node);
            setSpinnerValues(initArray);
        }
        spinner.setOnItemSelectedListener(createListener());
        xmlSaver.appendNode(node.getNodeName(), node.getNodeValue());
    }

    private String[] initSpinnerArray(Node node) {
        String[] initArray;
        initArray = new String[node.getChildNodes().getLength()+1];
        initArray[0] = "choisissez";
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
