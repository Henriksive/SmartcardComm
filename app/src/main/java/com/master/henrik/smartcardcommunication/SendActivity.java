package com.master.henrik.smartcardcommunication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import msd.MsdReaderFragment;
import nfc.CardReaderFragment;

public class SendActivity extends AppCompatActivity {

    Button nfcTransmitButton;
    Button msdTransmitButton;
    EditText inputField;
    EditText selInputField;
    TextView outputField;
    boolean isInNFCMode;
    boolean isInSDMode;
    CardReaderFragment cardReaderFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        isInNFCMode = false;
        isInSDMode = false;
        setup();
        setupButtonListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send, menu);
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


    public void setup(){
        inputField = (EditText)findViewById(R.id.edtInput);
        selInputField = (EditText)findViewById(R.id.edtSelInput);
        outputField = (TextView)findViewById(R.id.txtOutput);
        nfcTransmitButton = (Button)findViewById(R.id.btnTransmitnfc);
        msdTransmitButton = (Button)findViewById(R.id.btnTransmitsd);
    }

    public void setupButtonListeners(){
        //SendButton
        nfcTransmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    initCardCommunication();
            }
        });

        msdTransmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMsdCardCommunication();
            }
        });
    }

    private void initMsdCardCommunication(){
        String input = inputField.getText().toString();
        String selInput = selInputField.getText().toString();
        if(!isInSDMode) {
            MsdReaderFragment msdReaderFragment = new MsdReaderFragment();
            msdReaderFragment.enableSDcommunication(getApplicationContext(), this);
            msdReaderFragment.currentActivity = this;
            msdReaderFragment.transmitString = input;
            msdReaderFragment.SD_CARD_AID = selInput;
            outputField.setText("Waiting for output from mSD...");
            isInSDMode = true;
            setInputStatus(false);
            nfcTransmitButton.setEnabled(false);
            msdTransmitButton.setText("Stop transmit to mSD card");
        }
        else{
            isInSDMode = false;
            setInputStatus(true);
            nfcTransmitButton.setEnabled(true);
            msdTransmitButton.setText("Transmit to mSD card");
            outputField.setText("");
        }

    }

    private void initCardCommunication(){
        String input = inputField.getText().toString();
        String selInput = selInputField.getText().toString();
        //setup NFC reader
        if(cardReaderFragment == null) {
            cardReaderFragment = new CardReaderFragment();
            cardReaderFragment.currentActivity = this;
        }
        if(!isInNFCMode) {
            cardReaderFragment.transmitString = input;
            cardReaderFragment.selString = selInput;
            //Start reading NFC
            nfcTransmitButton.setText("Disable NFC");
            isInNFCMode = true;
            cardReaderFragment.enableReaderMode();
            setInputStatus(false);
            msdTransmitButton.setEnabled(false);
            outputField.setText("Waiting for output from NFC...");
        }
        else{
            //Stop reading NFC
            cardReaderFragment.disableReaderMode();
            isInNFCMode = false;
            setInputStatus(true);
            msdTransmitButton.setEnabled(true);
            nfcTransmitButton.setText("Transmit to NFC Card");
            outputField.setText("");
        }
    }

    private void setInputStatus(boolean status){
        inputField.setEnabled(status);
        selInputField.setEnabled(status);
    }
}
