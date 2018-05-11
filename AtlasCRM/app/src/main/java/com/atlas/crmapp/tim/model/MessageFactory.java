package com.atlas.crmapp.tim.model;


import android.util.Log;

import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMSNSSystemElem;
import com.tencent.imsdk.TIMSNSSystemType;

/**
 * 消息工厂
 */
public class MessageFactory {

    private MessageFactory() {}


    /**
     * 消息工厂方法
     */
    public static Message getMessage(TIMMessage message){
        switch (message.getElement(0).getType()){
            case Text:
            case Face:
                return new TextMessage(message);
            case Image:
                return new ImageMessage(message);
            case Sound:
                return new VoiceMessage(message);
//            case Video:
//                return new VideoMessage(message);
//            case GroupTips:
//                return new GroupTipMessage(message);
//            case File:
//                return new FileMessage(message);
            case Custom:
                return new CustomMessage(message);
//            case UGC:
//                return new UGCMessage(message);
            case SNSTips:
//                return new SystemConversation(new SystemBean());
                if (message.getElement(0) instanceof TIMSNSSystemElem){
                    Log.i("CommunicationFragment"," = TIMSNSSystemElem");
//                    if(((TIMSNSSystemElem)message.getElement(0)).getSubType() == TIMSNSSystemType.TIM_SNS_SYSTEM_DEL_FRIEND){
                    if(((TIMSNSSystemElem)message.getElement(0)).getSubType() == TIMSNSSystemType.TIM_SNS_SYSTEM_ADD_FRIEND_REQ){
                        return new SystemMessage(message);
                    };
                }

            default:
                return null;
        }
    }



}
