package msd;

interface IOTP
{
    // Service management
	int OTP_Init(String appName, in byte[] appSignature); 
	void OTP_Finalize(int sessionId);

	// Error management
	int OTP_GetLastError();

	// OTP Transaction management
	boolean OTP_BeginTransaction(int sessionId);
	boolean OTP_EndTransaction(int sessionId);

	// OTP Reader & Select methods
	int[] OTP_ListReaders(int sessionId);
	boolean OTP_SelectApp(int sessionId, int readerId);
	
    // OTP Authentication methods
    byte OTP_GetPinTryCounter(int sessionId);
    boolean OTP_VerifyPin(int sessionId, in byte[] pin);
    boolean OTP_ChangePin(int sessionId, in byte[] oldPin, in byte[] newPin);
    boolean OTP_UnblockPin(int sessionId, in byte[] puk);
    boolean OTP_ResetPin(int sessionId, in byte[] puk, in byte[] newPin);
    byte[] OTP_GetChallenge(int sessionId);
    byte[] OTP_MutualAuthenticate(int sessionId, in byte[] serverData);
    
    // OTP Basic methods
    byte[] OTP_GetTokenId(int sessionId);
    byte OTP_GetOtpProfile(int sessionId);
    byte[] OTP_GetOtpCounter(int sessionId);
    byte[] OTP_ComputeOtp(int sessionId);
    
    // OTP OCRA methods
    byte[] OTP_ComputeOcraOneWay(int sessionId, in byte[] challenge);
    byte[] OTP_ComputeOcraResponseOnly(int sessionId);
    byte[] OTP_GetOcraChallenge(int sessionId);
    byte[] OTP_ComputeOcraMutualChallenge(int sessionId, in byte[] serverData);
    
    // OTP Provisioning methods
    boolean OTP_PutOtpData(int sessionId, in byte[] data);
    boolean OTP_PutTokenId(int sessionId, in byte[] tokenId);
    
    // OTP v2 Interface methods
	boolean OTP_SelectAID(int sessionId, int readerId, in byte[] aid);
	boolean OTP_SetServiceId(int sessionId, byte serviceId);
    byte[] OTP_GetOtp(int sessionId);
    byte[] OTP_GetData(int sessionId, in byte[] tag, int length);
    byte[] OTP_DeleteService(int sessionId, byte serviceId);
    byte[] OTP_VerifyPinEx(int sessionId, in byte[] pin);
    byte[] OTP_ChangePinEx(int sessionId, in byte[] oldPin, in byte[] newPin);
    
    // Generic Command (RFU)
    byte[] OTP_GenericCommand(int sessionId, int cmdId, in byte[] data);
    
}
