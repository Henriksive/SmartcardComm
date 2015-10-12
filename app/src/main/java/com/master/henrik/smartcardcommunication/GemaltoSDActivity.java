package com.master.henrik.smartcardcommunication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import msd.IAPDU;
import msd.IConfig;
import msd.IMinidriver;
import msd.IUI;

public class GemaltoSDActivity extends AppCompatActivity {

    private static TextView mInfoView;

    private static Context m_ctx;
    private static Activity m_activity;
    private static PowerManager.WakeLock m_wakeLock;

    public static ProgressDialog m_dialogWait = null;
    private static String m_p12File = "";
    private static String m_p12Password = "";
    private static String m_sWait = "Wait...";
    private static String m_sResult = "";
    protected static int m_pkiType = -1;
    protected static int m_readerType = 0;
    protected static byte m_otpProfile = (byte) 0;

    private static IMinidriver m_mdService = null;

    private static IUI m_uiService = null;
    private static IConfig m_configService = null;
    private static int m_mdSessionId = 0;


    private final static String TAG = "GemaltoActivity";


    private static IAPDU m_apduService = null;
    private static int m_apduSessionId = 0;
    private static byte[] testAppSignature = {};
    private static String testAppPackage = "SmartCardCommunication";

    private static ServiceConnection m_apduConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            m_apduService = IAPDU.Stub.asInterface(service);
            Log.i(TAG, "onServiceCOnnected()");
            try {
                m_apduSessionId = m_apduService.APDU_Init(testAppPackage, testAppSignature);
            } catch (RemoteException e) {
                Log.i(TAG, "Failed to initialize APDU service: " + e.toString());

            }
        }

        // Called when the connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
            Log.i(TAG, "Disconnected");
            m_apduService = null;
        }
    };


    static private void connectAPDUService() {
        Log.i(TAG, "connectAPDUService()");
        try {
            if (m_apduService == null) {
                try {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.gemalto.minidriver", "com.gemalto.minidriver.CardModuleService"));
                    intent.setAction(IAPDU.class.getName());

                    m_ctx.bindService(intent, m_apduConnection, Context.BIND_AUTO_CREATE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(TAG, "Exception in connectAPDUService()1: " + e.toString());
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "Exception in connectAPDUService()2: " + e.toString());
            m_apduService = null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gemalto_sd);
        m_ctx = getApplicationContext();

        try{
            Signature[] sigs = m_ctx.getPackageManager().getPackageInfo(m_ctx.getPackageName(), PackageManager.GET_SIGNATURES).signatures;
            testAppSignature = sigs[0].toByteArray();
        }
        catch (Exception e){
            Log.i(TAG, e.toString());
        }
        testAppPackage = m_ctx.getPackageName();
        Log.i(TAG, "testAppPackage: " + testAppPackage);
        String signature = "";
        for(byte b : testAppSignature){
            signature += b;
        }
        Log.i(TAG, "testAppSignature: " + signature);

        connectAPDUService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gemalto_sd, menu);
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
