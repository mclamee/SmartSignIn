package com.ssi.main;

import java.awt.Font;
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
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

import com.ssi.model.AuthorizationException;
import com.ssi.util.StringUtil;
import com.ssi.view.AuthView;
import com.ssi.view.MainView;
import com.ssi.view.RecordView;
import com.ssi.view.SetupView;
import com.ssi.view.SignInView;

public class Application {

	static {
		// initialize configurations at first
		SSIConfig.init();
	}

	public static Logger LOG = Logger.getLogger(Application.class);

	public static MainView MAIN_FRAME;
	public static AuthView AUTH_FRAME;

	public static RecordView RECORD_VIEW;
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
		if (!debugMode || SSIConfig.getBoolean("debug.authorization") == true) {
			try {
				if (Application.authorization()) {
					Application.initMainFrame();
				}
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
		RECORD_VIEW = new RecordView();
		SETUP_VIEW = new SetupView();
		SIGNIN_VIEW = new SignInView();
		LOG.info("> initializing main frames ... OK!");
	}

	public static boolean authorization() throws AuthorizationException {
		String user = SSIConfig.get("user");
		String authKey = SSIConfig.get("authKey");
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
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String line = reader.readLine();
			if (!StringUtil.isEmpty(line) && line != "false") {
				int idxOfD = line.indexOf("D");
				int idxOfA = line.indexOf("a");
				if (idxOfD != -1 && idxOfA != -1) {
					String pd = line.substring(0, idxOfD);
					String pa = line.substring(idxOfD + 5, idxOfA);
					if (!StringUtil.isEmpty(pa)) {
						int idxOfPa11 = line.indexOf(pa + "11");
						if (idxOfPa11 != -1) {
							String pm = line.substring(idxOfPa11 + pa.length()
									+ 2, line.length() - 4);

							SimpleDateFormat fmt = new SimpleDateFormat("yyyy/M/d");
							try {
								String source = (Integer.valueOf(pm) - 5541)
										+ "/" + (Integer.valueOf(pa) - 7) + "/"
										+ (Integer.valueOf(pd) - 91);
								String todayStr = fmt.format(new Date());
								if (source.equals(todayStr)) {
									LOG.debug("Authorized to '" + user + "' successfully!");
									return true;
								}
							} catch (NumberFormatException e) {
								throw new AuthorizationException(
										"Authorization Failed! Please check your user-name and/or authrization key.");
							}
						}

					}
				}
			}
		} catch (IOException e) {
			throw new AuthorizationException("Network issue! Please check your Internet connection.");
		}
		LOG.debug("Authorization failed!");
		return false;
	}
	
    private static void setupApplicationStyle() {
//        try {
//            BeautyEyeLNFHelper.frameBorderStyle = FrameBorderStyle.translucencyAppleLike;
//            BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
//            UIManager.put("RootPane.setupButtonVisible", false);
//            BeautyEyeLNFHelper.launchBeautyEyeLNF();
//        } catch (final Exception r) {
//        }

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
}
