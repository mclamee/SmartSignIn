package com.ssi.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

import com.ssi.view.AuthFrame;
import com.ssi.view.MainView;
import com.ssi.view.RecordView;
import com.ssi.view.SetupView;
import com.ssi.view.SignInView;
import com.wicky.util.ConfigUtil;
import com.wicky.util.StringUtil;


public class Application {
    
    static{
        // initialize configurations at first
        ConfigUtil.init();
    }
    
    public static Logger LOG = Logger.getLogger(Application.class);
    
    public static JFrame MAIN_FRAME;
    public static JFrame AUTH_FRAME;
    
    public static RecordView RECORD_VIEW;
    public static SetupView SETUP_VIEW;
    public static SignInView SIGNIN_VIEW;
    
    /**
     * Demo入口函数.
     * @param args
     */
    public static void main(String args[]) {
        // check application authorizations 
        if(Application.authorization()){
            Application.initMainFrame();
        }else{
            Application.initAuthFrame();
        }
    }

    public static void initAuthFrame() {
        AUTH_FRAME = new AuthFrame();
    }

    public static void initMainFrame() {
        MAIN_FRAME = new MainView();
        RECORD_VIEW = new RecordView();
        SETUP_VIEW = new SetupView();
        SIGNIN_VIEW = new SignInView();
    }   
    
    public static boolean authorization() {
        try {
            String user = ConfigUtil.get("user");
            String authKey = ConfigUtil.get("authKey");
            String decoded = new String(new BASE64Decoder().decodeBuffer(authKey));
            LOG.debug("Checking Authorization Information!");
            URL url = new URL("http://mcvalidate.sinaapp.com/?authKey="+decoded+"&user="+user);
            InputStream inputStream = url.openConnection().getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();
            if(!StringUtil.isEmpty(line) && line != "false"){
                int idxOfD = line.indexOf("D");
                int idxOfA = line.indexOf("a");
                if(idxOfD != -1 && idxOfA != -1){
                    String pd = line.substring(0, idxOfD);
                    String pa = line.substring(idxOfD + 5, idxOfA);
                    if(!StringUtil.isEmpty(pa)){
                        int idxOfPa11 = line.indexOf(pa+"11");
                        if(idxOfPa11 != -1){
                            String pm = line.substring(idxOfPa11 + pa.length() + 2, line.length() - 4);
                            
                            SimpleDateFormat fmt = new SimpleDateFormat("yyyy/M/d");
                            String source = (Integer.valueOf(pm) - 5541) + "/"+ (Integer.valueOf(pa) - 7) + "/" + (Integer.valueOf(pd) - 91);
                            String todayStr = fmt.format(new Date());
                            if(source.equals(todayStr)){
                                LOG.debug("Authorized to '" + user + "' successfully!");
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (NumberFormatException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOG.debug("Authorization failed!");
        return false;
    }
}
