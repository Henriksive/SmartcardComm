package msd;

import android.os.IBinder;
import android.os.RemoteException;

/**
 * Created by Henri on 18.09.2015.
 */
public class ConnectionService implements IAPDU {
    @Override
    public int APDU_Init(String appName, byte[] appSignature) throws RemoteException {
        return 0;
    }

    @Override
    public void APDU_Finalize(int sessionId) throws RemoteException {

    }

    @Override
    public int APDU_GetLastError() throws RemoteException {
        return 0;
    }

    @Override
    public boolean APDU_BeginTransaction(int sessionId) throws RemoteException {
        return false;
    }

    @Override
    public boolean APDU_EndTransaction(int sessionId) throws RemoteException {
        return false;
    }

    @Override
    public int[] APDU_ListReaders(int sessionId) throws RemoteException {
        return new int[0];
    }

    @Override
    public boolean APDU_IsCardPresent(int sessionId, int readerId) throws RemoteException {
        return false;
    }

    @Override
    public boolean APDU_Connect(int sessionId, int readerId) throws RemoteException {
        return false;
    }

    @Override
    public boolean APDU_Disconnect(int sessionId) throws RemoteException {
        return false;
    }

    @Override
    public boolean APDU_IsConnected(int sessionId) throws RemoteException {
        return false;
    }

    @Override
    public byte[] APDU_Transmit(int sessionId, byte[] apduCommand, int timeout) throws RemoteException {
        return new byte[0];
    }

    @Override
    public byte[] APDU_GenericCommand(int sessionId, int cmdId, byte[] data) throws RemoteException {
        return new byte[0];
    }

    @Override
    public IBinder asBinder() {
        return null;
    }
}
