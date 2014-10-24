package com.ssi.main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.FrameBorderStyle;

import sun.misc.BASE64Decoder;

import com.ssi.main.model.AuthorizationException;
import com.ssi.main.view.AuthView;
import com.ssi.main.view.MainView;
import com.ssi.main.view.RecordView;
import com.ssi.main.view.SetupView;
import com.ssi.main.view.SignInView;
import com.ssi.main.view.StaffView;
import com.ssi.util.StringUtil;
import com.vguang.VguangApi;

public class Application {

    static {
        // initialize configurations at first
        SSIConfig.init();
    }

    public static Logger LOG = Logger.getLogger(Application.class);

    public static MainView MAIN_FRAME;
    public static AuthView AUTH_FRAME;

    public static JPanel MAIN_VIEW;
    public static RecordView RECORD_VIEW;
    public static StaffView STAFF_VIEW;
    public static SetupView SETUP_VIEW;
    public static SignInView SIGNIN_VIEW;

    public static Boolean debugMode = false;

    /**
     * Demo入口函数.
     * 
     * @param args
     */
    public static void main(String args[]) {
        // check arguments
        if (args != null && args.length > 0 && args[0] != null) {
            String debug = args[0];
            if ("-debug".equalsIgnoreCase(debug)) {
                debugMode = true;
                LOG.info("! DEBUG MODE IS ON !");
            }
        }

        // setup lnf
        setupApplicationStyle();

        // check application authorizations
        if (!debugMode || Boolean.TRUE.equals(SSIConfig.getBoolean("debug.authorization"))) {
            try {
                Application.authorization();
                Application.initMainFrame();
            } catch (AuthorizationException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                Application.initAuthFrame();
            }
        } else {
            Application.initMainFrame();
        }
        LOG.info("------------------------- SYSTEM STANDBY -------------------------");
    }

    public static void initAuthFrame() {
        LOG.info("> initializing auth frame ...");
        AUTH_FRAME = new AuthView();
        LOG.info("> initializing auth frame ... OK!");
    }

    public static void initMainFrame() {
        LOG.info("> initializing main frames ...");
        MAIN_FRAME = new MainView();
        MAIN_VIEW = MAIN_FRAME.getMainJpanel();
        SETUP_VIEW = new SetupView();
        RECORD_VIEW = new RecordView();
        STAFF_VIEW = new StaffView();
        SIGNIN_VIEW = new SignInView();

        // setup window close hook method
        MAIN_FRAME.addWindowListener(RECORD_VIEW.getWindowListener());
        MAIN_FRAME.addWindowListener(STAFF_VIEW.getWindowListener());
        MAIN_FRAME.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SSIConfig.save();
            }
        });

        LOG.info("> initializing main frames ... OK!");

        // init starting page
        String startupView = SSIConfig.get("system.startup.view");
        if (!StringUtil.isEmpty(startupView)) {
            switchView(getViewByName(startupView));
        }
    }

    /**
     * @author williamz@synnex.com
     */
    public static JPanel getViewByName(String startupView) {
        switch (startupView) {
        case "SignInView":
            return Application.SIGNIN_VIEW;
        case "RecordView":
            return Application.RECORD_VIEW;
        case "StaffView":
            return Application.STAFF_VIEW;
        case "SetupView":
            return Application.SETUP_VIEW;
        default:
            return Application.MAIN_VIEW;
        }
    }

    public static void authorization() throws AuthorizationException {
        String user = SSIConfig.get("auth.user");
        String authKey = SSIConfig.get("auth.key");
        BASE64Decoder decoder = new BASE64Decoder();
        String decoded = null;
        try {
            decoded = new String(decoder.decodeBuffer(authKey));
        } catch (IOException e) {
            throw new AuthorizationException("Authorization Failed! Illegal authrization key.");
        }
        LOG.debug("Checking Authorization Information!");
        URL url = null;
        try {
            url = new URL("http://mcvalidate.sinaapp.com/?authKey=" + decoded + "&user=" + user);
        } catch (MalformedURLException e) {
            throw new AuthorizationException("Authorization Failed! Please check your user-name and/or authrization key.");
        }
        try {
            URLConnection conn = url.openConnection();
            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();
            if (!StringUtil.isEmpty(line) && !"false".equals(line)) {
                int idxOfD = line.indexOf("D");
                int idxOfA = line.indexOf("a");
                if (idxOfD != -1 && idxOfA != -1) {
                    String pd = line.substring(0, idxOfD);
                    String pa = line.substring(idxOfD + 5, idxOfA);
                    if (!StringUtil.isEmpty(pa)) {
                        int idxOfPa11 = line.indexOf(pa + "11");
                        if (idxOfPa11 != -1) {
                            String pm = line.substring(idxOfPa11 + pa.length() + 2, line.length() - 4);

                            SimpleDateFormat fmt = new SimpleDateFormat("yyyy/M/d");
                            try {
                                String source = (Integer.valueOf(pm) - 5541) + "/" + (Integer.valueOf(pa) - 7) + "/"
                                        + (Integer.valueOf(pd) - 91);
                                String todayStr = fmt.format(new Date());
                                if (source.equals(todayStr)) {
                                    LOG.debug("Authorized to '" + user + "' successfully!");
                                    return;
                                }
                            } catch (NumberFormatException e) {
                                throw new AuthorizationException(
                                        "Authorization Failed! Please check your user-name and/or authrization key.");
                            }
                        }

                    }
                }
            }
            throw new AuthorizationException("Authorization failed! Response is null or false.");
        } catch (IOException e) {
            throw new AuthorizationException("Network issue! Please check your Internet connection.");
        }
    }

    private static void setupApplicationStyle() {
        try {
            BeautyEyeLNFHelper.frameBorderStyle = FrameBorderStyle.translucencyAppleLike;
            BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
            UIManager.put("RootPane.setupButtonVisible", false);
            BeautyEyeLNFHelper.launchBeautyEyeLNF();
        } catch (final Exception r) {
        }

        final Font font = new Font(SSIConfig.get("font"), Font.PLAIN, 12);
        UIManager.put("Frame.titleFont", font);
        UIManager.put("Menu.font", font);
        UIManager.put("MenuItem.font", font);
        UIManager.put("TitledBorder.font", font);
        UIManager.put("InternalFrame.font", font);
        UIManager.put("InternalFrame.titleFont", font);
        UIManager.put("Table.font", font);
        UIManager.put("TableHeader.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("Label.font", font);
        UIManager.put("List.font", font);
    }

    public static void switchView(JPanel view) {
        LOG.info("> Switched To View: " + view.getClass().getSimpleName());
        Container mContentPanel = Application.MAIN_FRAME.getContentPane();
        mContentPanel.remove(mContentPanel.getComponent(0));
        mContentPanel.add(view, BorderLayout.CENTER);
        mContentPanel.revalidate();
        mContentPanel.repaint();
    }

    public static void closeDevice() {
        if (!debugMode || Boolean.TRUE.equals(SSIConfig.getBoolean("debug.scanner"))) {
            // 关闭设备
            VguangApi.closeDevice();
        }
    }

    public static void applySettingsAndOpenDevice() {
        // 初始化数据
        Application.SIGNIN_VIEW.initDataMap();

        if (!debugMode || Boolean.TRUE.equals(SSIConfig.getBoolean("debug.scanner"))) {
            // 应用设置
            Application.SETUP_VIEW.applySetting();
            // 打开设备
            VguangApi.openDevice();
        }
    }
}
