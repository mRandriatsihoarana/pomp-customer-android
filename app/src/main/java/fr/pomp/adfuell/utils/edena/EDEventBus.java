package fr.pomp.adfuell.utils.edena;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by edena on 13/01/2017.
 * catch and send event easily
 * compile 'org.greenrobot:eventbus:3.0.0'
 */

public class EDEventBus {

    public static final String EVENT_NETWORK = "event_network";

    String mSenderID;
    Object mResultValue;

    EventBus mEventBus;
    static EDEventBus mInstance;
    Object mObjectRegister;


    EDEventBus(Object objectRegister){
        mObjectRegister = objectRegister;
        mEventBus = EventBus.getDefault();
    }

    public EDEventBus(String mSenderID, Object mResultValue) {
        this.mSenderID = mSenderID;
        this.mResultValue = mResultValue;
    }

    public static EDEventBus getInstance(Object objectRegister){
            mInstance = new EDEventBus(objectRegister);
            return mInstance;
        }


    /**
     * register the event onresume
     * and create an methode to recuperate the event with this annotation
     * @Subscribe(threadMode = ThreadMode.MAIN)
       public void doThis(EDEventBus intentServiceResult) {
            textView.set_text((String)intentServiceResult.getResultValue());
     }
     */
    public void register(){
        mEventBus.register(mObjectRegister);
    }

    /**
     * unregister the event onpause
     */
    public void unRegister(){
        mEventBus.unregister(mObjectRegister);
    }

    /**
     * Send an event
     * @param event
     */
    public void post(EDEventBus event){
        mEventBus.post(event);
    }

    /**
     * Send an event repetly
     * @param event
     */
    public void postStick(EDEventBus event){
        mEventBus.postSticky (event);
    }
         public EDEventBus(String resultCode, String resultValue) {
            mSenderID = resultCode;
            mResultValue = resultValue;
        }

        public String getSenderID() {
            return mSenderID;
        }

        public Object getResultValue() {
            return mResultValue;
        }
}
