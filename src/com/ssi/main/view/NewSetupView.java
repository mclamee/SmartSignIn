/**
 * package com.ssi.main.view
 * class com.ssi.main.view.NewSetupView
 * Created on 2014年10月21日, 下午6:05:13
 * @author williamz
 *
 * Copyright (c) 2013, Synnex and/or its affiliates. All rights reserved.
 * SYNNEX PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.ssi.main.view;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import com.ssi.main.Application;

public class NewSetupView extends JPanel {
    public NewSetupView() {
        Dimension frameSize = Application.MAIN_FRAME.getSize();
        int frameWidth = (int)frameSize.getWidth();
        int frameHeight = (int)frameSize.getHeight();
        
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        
        JDesktopPane desktopPane = new JDesktopPane();
        add(desktopPane);
        
        JInternalFrame internalFrame = new JInternalFrame("New JInternalFrame");
        internalFrame.setBounds(242, 112, 315, 264);
        desktopPane.add(internalFrame);
        internalFrame.setVisible(true);
    }
    private static final long serialVersionUID = -7094863373847170608L;
}
