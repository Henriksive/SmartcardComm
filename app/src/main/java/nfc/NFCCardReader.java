package nfc;

import android.annotation.TargetApi;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Arrays;

import shared.Converter;

/**
 * Created by Henri on 16.09.2015.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class NFCCardReader implements NfcAdapter.ReaderCallback {
    private static final String TAG = "NFCCardReader";
    // AID for card service.
    private static final String NFC_CARD_AID = "0102030405060708090007";


    // "OK" status word sent in response to SELECT AID command (0x9000)
    private static final byte[] SELECT_OK_SW = {(byte) 0x90, (byte) 0x00};
    private WeakReference<CardCallback> cCardCallBack;
    public String transmitString = "8000000000";

    public NFCCardReader(CardCallback cardCallback){
        cCardCallBack = new WeakReference<CardCallback>(cardCallback);
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        Log.i(TAG, "New tag discovered.");

        IsoDep isoDep = IsoDep.get(tag);
        if (isoDep != null) {
            try {
                isoDep.connect();

                byte[] command = Converter.BuildSelectApdu(NFC_CARD_AID);
                Log.i(TAG, "Sending: " + Converter.ByteArrayToHexString(command));
                byte[] result = isoDep.transceive(command);

                int resultLength = result.length;
                byte[] statusWord = {result[resultLength-2], result[resultLength-1]};
                byte[] payload = Arrays.copyOf(result, resultLength - 2);
                if (Arrays.equals(SELECT_OK_SW, statusWord)) {
                    byte[] command2 = Converter.HexStringToByteArray(transmitString);
                    // The remote NFC device will immediately respond with its stored account number
                    //String accountNumber = new String(payload, "UTF-8");
                    Log.i(TAG, "SENDING: " + Converter.ByteArrayToHexString(command2));
                    result = isoDep.transceive(command2);
                    Log.i(TAG, "RECEIVING : " + Converter.ByteArrayToHexString(result));
                    // Inform CardReaderFragment of received account number
                    cCardCallBack.get().onInfoReceived(Converter.ByteArrayToHexString(result));
                }
            } catch (IOException e) {
                Log.e(TAG, "Could not communicate with NFC card: " + e.toString());
            }
        } else {
            Log.i(TAG, "IsoDep is null.");
        }
    }

    public interface CardCallback {
        public void onInfoReceived(String info);
    }
}
