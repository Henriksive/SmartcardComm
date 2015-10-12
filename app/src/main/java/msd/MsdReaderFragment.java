package msd;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import com.master.henrik.smartcardcommunication.R;

import org.simalliance.openmobileapi.*;

//import javacard.framework.APDU;

import shared.Converter;

/**
 * Created by Henrik on 16.09.2015.
 */
public class MsdReaderFragment implements SEService.CallBack {
    private final static String TAG = "MsdReaderFragment";
    private SEService seService;
    // AID for card service.
    public static String SD_CARD_AID;
    public static Activity currentActivity;
    public static String transmitString;


    private static IAPDU _apduService = null;
    private static int _apduSessionId = 0;
    private static final String appPackage = "Test Package";
    private static byte[] appSignature = {(byte)0xDA, (byte)0x57, (byte)0xAB, (byte)0x74, (byte)0x5D, (byte)0xB2, (byte)0xD2, (byte)0x62,
            (byte)0x05, (byte)0x3E, (byte)0x8B, (byte)0xB1, (byte)0xC0, (byte)0x5F, (byte)0x71, (byte)0x65
    };
    private ServiceConnection apduConnection;

    public void enableSDcommunication(Context ctx, Activity activity) {

        Log.i(TAG, "Enabled mSD");
        apduConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i(TAG, "onServiceConnected()");
                _apduService = IAPDU.Stub.asInterface(service);
                try {
                    appSignature = Converter.BuildSelectApdu(SD_CARD_AID);
                    _apduSessionId = _apduService.APDU_Init(appPackage, appSignature);
                    _apduService.APDU_BeginTransaction(_apduSessionId);
                } catch (Exception e) {
                    Log.i(TAG, "Exception: " + e.toString());
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i(TAG, "Lost connection");
                _apduService = null;
            }
        };
        connectToApduService(ctx);



        /*

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
        */
    }

    private void connectToApduService(Context context){
        if(_apduService == null){
            Log.i(TAG, "SERVICE IS NULL");
        }
        Log.i(TAG, "connectToApduService");
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("msd","msd.ConnectionService"));
        intent.setAction(IAPDU.class.getName());
        context.bindService(intent, apduConnection, context.BIND_AUTO_CREATE);

    }

    public void serviceEstablished() {

    }

    @Override
    public void serviceConnected(SEService seService) {
        Log.i(TAG, "Connected");
        try {
            Reader[] readers = seService.getReaders();
            if (readers.length < 1) {
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


        } catch (final Exception e) {
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
