package msd;

interface IConfig
{
	// Service Management
	void CONFIG_Init(String appName, in byte[] appSignature); 
	void CONFIG_Finalize();

	int CONFIG_GetLastError();

    boolean CONFIG_GetSoftPkiEnabled();
    boolean CONFIG_SetSoftPkiEnabled(boolean isEnabled);
    
    int CONFIG_GetDefaultPkiService();
    boolean CONFIG_SetDefaultPkiService(int defaultPki);
    
    boolean CONFIG_GetSecuredWirelessEnabled();
    boolean CONFIG_SetSecuredWirelessEnabled(boolean isEnabled);
    
    boolean CONFIG_GetSecuredUiEnabled();
    boolean CONFIG_SetSecuredUiEnabled(boolean isEnabled);
    
    int CONFIG_GetPinKeyboardType();
    boolean CONFIG_SetPinKeyboardType(int keyboardType);
    
    int CONFIG_GetLanguage();
    boolean CONFIG_SetLanguage(int language);

    boolean CONFIG_GetOmapiEnabled();
    boolean CONFIG_SetOmapiEnabled(boolean isEnabled);

    boolean CONFIG_GetPersistentCacheEnabled();
    boolean CONFIG_SetPersistentCacheEnabled(boolean isEnabled);

    boolean CONFIG_GetWaitPkiEnabled();
    boolean CONFIG_SetWaitPkiEnabled(boolean isEnabled);
}

