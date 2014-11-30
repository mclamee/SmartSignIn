package com.wicky.tdl.widget;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.ssi.i18n.I18NUtil;

public class MyButtonRender implements TableCellRenderer
{
    private JButton button;

    public MyButtonRender()
    {
        this.initButton();

    }

    private void initButton()
    {
        this.button = new JButton();
        // 设置按钮的大小及位置。
//        this.button.setBounds(0, 0, 50, 15);
    }

    @Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
            int column)
    {
        // 只为按钮赋值即可。也可以作其它操作，如绘背景等。
        this.button.setText(I18NUtil.getInstance().getString("table.cell.editor.btn_more"));

        return this.button;
    }

}

 