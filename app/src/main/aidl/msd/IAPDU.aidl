package msd;

interface IAPDU
{
    // Service management
	int APDU_Init(String appName, in byte[] appSignature); 
	void APDU_Finalize(int sessionId);

	// Error management
	int APDU_GetLastError();

	// APDU Transaction management
	boolean APDU_BeginTransaction(int sessionId);
	boolean APDU_EndTransaction(int sessionId);

    // APDU service methods
	int[] APDU_ListReaders(int sessionId);
	boolean APDU_IsCardPresent(int sessionId, int readerId);
	boolean APDU_Connect(int sessionId, int readerId);
	boolean APDU_Disconnect(int sessionId);
	boolean APDU_IsConnected(int sessionId);
	byte[] APDU_Transmit(int sessionId, in byte[]apduCommand, int timeout);
	
    // Generic Command (RFU)
    byte[] APDU_GenericCommand(int sessionId, int cmdId, in byte[] data);
}
