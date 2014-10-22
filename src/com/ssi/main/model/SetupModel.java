package com.ssi.main.model;

import org.apache.log4j.Logger;

public class SetupModel {
    private static final Logger LOG = Logger.getLogger(SetupModel.class);
    
    public String[] getVoiceList(){
        
        String[] strList = new String[]{
            "小燕 青年女声 中英文（普通话）xiaoyan"
            ,"小宇 青年男声 中英文（普通话） xiaoyu"
            ,"小研 青年女声 中英文（普通话） vixy"
            ,"小琪 青年女声 中英文（普通话） vixq"
            ,"小峰 青年男声 中英文（普通话） vixf"
            ,"小梅 青年女声 中英文（粤语） vixm"
            ,"小莉 青年女声 中英文（台湾普通话） vixl"
            ,"小蓉 青年女声 汉语（四川话） vixr"
            ,"小芸 青年女声 汉语（东北话） vixyun"
            ,"小坤 青年男声 汉语（河南话） vixk"
            ,"小强 青年男声 汉语（湖南话） vixqa"
            ,"小莹 青年女声 汉语（陕西话） vixying"
            ,"小新 童年男声 汉语（普通话） vixx"
            ,"楠楠 童年女声 汉语（普通话） vinn"
            ,"老孙 老年男声 汉语（普通话） vils"
            ,"凯瑟琳 青年女声 英文 catherine"
            ,"亨利 青年男声 英文 henry"
            ,"玛丽 青年女声 英文 vimary"
        };
        
        return strList;
    }
}
