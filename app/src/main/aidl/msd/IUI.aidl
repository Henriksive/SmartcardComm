package msd;

interface IUI
{
	// Service Management
	void UI_Init(String appName, in byte[] appSignature); 
	void UI_Finalize();

	int UI_GetLastError();

    // PIN Popup methods
    void UI_PinPopup(String title, String summary, int validityTimeout);
    
    // Generic UI - See documentation
    void UI_GenericUI(int uiType, String title, String summary, in byte[]params);
}
