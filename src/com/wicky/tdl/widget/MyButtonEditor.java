package com.wicky.tdl.widget;  
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

import com.wicky.tdl.IDataVector;
import com.wicky.tdl.ISubDataVector;
import com.wicky.tdl.SimpleTableModel;
import com.wicky.tdl.SimpleTodoTable;
  
/** 
 * 自定义一个往列里边添加按钮的单元格编辑器。最好继承DefaultCellEditor，不然要实现的方法就太多了。 
 *  
 */  
public class MyButtonEditor extends DefaultCellEditor  
{  
  
    /** 
     * serialVersionUID 
     */  
    private static final long serialVersionUID = -6546334664166791132L;  
  
    private JButton button;

    private SimpleTodoTable table;  
  
    public MyButtonEditor(SimpleTodoTable table)  
    {  
        // DefautlCellEditor有此构造器，需要传入一个，但这个不会使用到，直接new一个即可。   
        super(new JTextField());  
        this.table = table;
  
        // 设置点击几次激活编辑。   
        this.setClickCountToStart(1);  
  
        this.initButton();  
    }  
  
    private void initButton()  
    {  
        this.button = new JButton();  
  
        // 设置按钮的大小及位置。   
        this.button.setBounds(0, 0, 50, 15);  
  
        // 为按钮添加事件。这里只能添加ActionListner事件，Mouse事件无效。   
        this.button.addActionListener(new ActionListener()  
        {  
            public void actionPerformed(ActionEvent e)  
            {  
                // 触发取消编辑的事件，不会调用tableModel的setValue方法。   
                MyButtonEditor.this.fireEditingCanceled();  
  
                // 这里可以做其它操作。   
                // 可以将table传入，通过getSelectedRow,getSelectColumn方法获取到当前选择的行和列及其它操作等。   
                SimpleTableModel model = (SimpleTableModel) table.getModel();
                Object data = model.getData().getValueAt(table.getSelectedRow(), table.getSelectedColumn());
                if(data instanceof IDataVector){
                    IDataVector<ISubDataVector> idata = (IDataVector<ISubDataVector>)data;
                    table.showDialog(idata.getTitle(), idata.getMessage(), idata);
                }
            }
        });  
  
    }  
  
    /** 
     * 这里重写父类的编辑方法，返回一个JPanel对象即可（也可以直接返回一个Button对象，但是那样会填充满整个单元格） 
     */  
    @Override  
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)  
    {  
        // 只为按钮赋值即可。也可以作其它操作。   
        this.button.setText("Click To See More");
        return this.button;
    }  
  
    /** 
     * 重写编辑单元格时获取的值。如果不重写，这里可能会为按钮设置错误的值。 
     */  
    @Override  
    public Object getCellEditorValue()  
    {  
        return this.button.getText();  
    }  
  
} 