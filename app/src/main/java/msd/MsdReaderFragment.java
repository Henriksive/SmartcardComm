package msd;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.master.henrik.smartcardcommunication.R;

import org.simalliance.openmobileapi.*;

import shared.Converter;

/**
 * Created by Henrik on 16.09.2015.
 */
public class MsdReaderFragment implements  SEService.CallBack{
    private final static String TAG = "MsdReaderFragment";
    private SEService seService;
    // AID for card service.
    private static final String SD_CARD_AID = "0102030405060708090007";
    public static Activity currentActivity;
    public static String transmitString;

    public void enableSDcommunication(Context ctx, Activity activity){
        Log.i(TAG, "Trying to setup service.");

        try{
            seService = new SEService(ctx, this);
        }
        catch (SecurityException e){
            Log.i(TAG, "Security exception " + e.toString());
        }
        catch (Exception e){
            Log.i(TAG, "Exception " + e.toString());
        }


        //seService.CallBack(serviceEstablished());
        //Reader[] readers = seService.getReaders();
        //for(Reader r : readers){
        //    Log.i(TAG, r.toString());
        //}
        Log.i(TAG, "Done setting up service.");
    }

    public void serviceEstablished(){

    }

    @Override
    public void serviceConnected(SEService seService) {
        Log.i(TAG,"Connected");
        try{
            Reader[] readers = seService.getReaders();
            if(readers.length < 1) {
                Log.i(TAG, "No readers found");
                return;
            }
            Log.i(TAG, "Create Session from the first reader");
            Session session = readers[0].openSession();

            Log.i(TAG, "Create Logical channel with session");
            byte[] command = Converter.BuildSelectApdu(SD_CARD_AID);
            Channel channel = session.openLogicalChannel(command);

            Log.i(TAG, "Select APDU App");
            byte[] respApdu = channel.transmit(new byte[]{(byte) 0x90, 0x10, 0x00, 0x00, 0x00});

            Log.i(TAG, "Transmitting " + transmitString);
            byte[] command2 = Converter.HexStringToByteArray("0807000000");
            byte[] respApdu2 = channel.transmit(command2);

            Log.i(TAG, "Close channel");
            channel.close();

            final String resp = Converter.ByteArrayToHexString(respApdu) + "\n" + Converter.ByteArrayToHexString(respApdu2);
            currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView outputField = (TextView) currentActivity.findViewById(R.id.txtOutput);
                    outputField.setText("Responded with: \n" + resp);
                }
            });


        }
        catch (final Exception e){
            Log.i(TAG, "Exception " + e.toString());
            currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView outputField = (TextView) currentActivity.findViewById(R.id.txtOutput);
                    outputField.setText("Exception " + e.toString());
                }
            });
        }
    }
}
