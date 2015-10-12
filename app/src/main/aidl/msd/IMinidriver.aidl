package msd;

interface IMinidriver
{
	// Service Management
	int MD_Init(int cardType, String appName, in byte[] appSignature); 
	void MD_Finalize(int sessionId);
	int MD_GetCardType(int sessionId);

	// Error management
	int MD_GetLastError();
	
	// MD Transaction management
    boolean MD_BeginTransaction(int sessionId);
	boolean MD_EndTransaction(int sessionId);

    // Authentication methods
    boolean MD_IsAdminPersonalized(int sessionId);
    boolean MD_IsUserPersonalized(int sessionId);
    byte[] MD_GetChallenge(int sessionId);
    void MD_ExternalAuthenticate(int sessionId, in byte[] response);
    void MD_AuthenticatePin(int sessionId, byte role, in byte[] pin);
    void MD_Deauthenticate(int sessionId, byte role);
    void MD_ChangeReferenceData(int sessionId, byte mode, byte role, in byte[] oldPin, in byte[] newPin, int maxTries);
    int MD_GetTriesRemaining(int sessionId, byte role);
    boolean MD_IsAuthenticated(int sessionId, byte role);

    // Container management methods
    byte MD_CreateContainer(int sessionId, byte ctrIndex, boolean keyImport, byte keySpec, int keySize, in byte[] keyValue);
    void MD_DeleteContainer(int sessionId, byte ctrIndex);
    byte[] MD_GetContainer(int sessionId, byte ctrIndex);
    
    // Crypto methods
    byte[] MD_RsaDecrypt(int sessionId, byte ctrIndex, byte keyType,  byte paddingType, byte algo, in byte[] encryptedData); 
    byte[] MD_SignData(int sessionId, byte ctrIndex, byte keyType,  byte paddingType, byte algo, in byte[] data);
    byte[] MD_ConstructDHAgreement(int sessionId, byte ctrIndex, in byte[] dataQx, in byte[] dataQy);
    
    // Information methods
    byte[] MD_QueryCapabilities(int sessionId);
    int[] MD_QueryFreeSpace(int sessionId);
    int[] MD_QueryKeySizes(int sessionId);
    int[] MD_QueryKeySizesEx(int sessionId, byte keySpec);
    byte[] MD_GetSerialNumber(int sessionId);
    String MD_GetVersion(int sessionId);
    boolean MD_IsECC(int sessionId);

    // File system management methods
    void MD_CreateDirectory(int sessionId, String path, in byte[] acls);
    void MD_DeleteDirectory(int sessionId, String path);
    void MD_CreateFile(int sessionId, String path, in byte[] acls, int initialSize);
    void MD_CreateFileEx(int sessionId, String path, in byte[] acls, in byte[] data);
    void MD_DeleteFile(int sessionId, String path);
    void MD_WriteFile(int sessionId, String path, in byte[] data);
    byte[] MD_ReadFile(int sessionId, String path, int maxBytesToRead); 
    String[] MD_GetFiles(int sessionId, String path);
    byte[] MD_GetFileProperties(int sessionId, String path);

    // Minidriver Extended methods (V6/V7 spec)
    byte[] MD_GetChallengeEx(int sessionId, byte role);
    byte[] MD_AuthenticateEx(int sessionId, byte mode, byte role, in byte[] pin);
    void MD_DeauthenticateEx(int sessionId, byte roles);
    void MD_ChangeAuthenticatorEx(int sessionId, byte mode, byte oldRole, in byte[] oldPin, byte newRole, in byte[] newPin, int maxTries);
    
    // Property management methods
    byte[] MD_GetContainerProperty(int sessionId, byte ctrIndex, byte property, byte flags);
    void MD_SetContainerProperty(int sessionId, byte ctrIndex, byte property, in byte[] data, byte flags);
    byte[] MD_GetCardProperty(int sessionId, byte property, byte flags);
    void MD_SetCardProperty(int sessionId, byte property, in byte[] data, byte flags);
    
    byte[] MD_GetKeysAndCertificates(int sessionId);
    
    // Generic Command (RFU)
    byte[] MD_GenericCommand(int sessionId, int cmdId, in byte[] data);
}
