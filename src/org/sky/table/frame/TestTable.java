package org.sky.table.frame;  
  
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.sky.table.editor.MyButtonEditor;
import org.sky.table.render.MyButtonRender;
  
public class TestTable  
{  
  
    private JFrame frame;  
    private JTable table;  
  
    /** 
     * Launch the application. 
     */  
    public static void main(String[] args)  
    {  
        EventQueue.invokeLater(new Runnable()  
        {  
            public void run()  
            {  
                try  
                {  
                    TestTable window = new TestTable();  
                    window.frame.setVisible(true);  
                }  
                catch (Exception e)  
                {  
                    e.printStackTrace();  
                }  
            }  
        });  
    }  
  
    /** 
     * Create the application. 
     */  
    public TestTable()  
    {  
        this.initialize();  
    }  
  
    /** 
     * Initialize the contents of the frame. 
     */  
    private void initialize()  
    {  
        this.frame = new JFrame();  
        this.frame.setBounds(100, 100, 450, 300);  
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        this.frame.getContentPane().setLayout(null);  
  
        JPanel panel = new JPanel();  
        panel.setBounds(10, 10, 414, 242);  
        this.frame.getContentPane().add(panel);  
        panel.setLayout(null);  
  
        JScrollPane scrollPane = new JScrollPane();  
        scrollPane.setBounds(10, 10, 394, 222);  
        panel.add(scrollPane);  
  
        this.table = new JTable();  
        scrollPane.setViewportView(this.table);  
  
        this.table.setModel(new DefaultTableModel()  
        {  
            @Override  
            public Object getValueAt(int row, int column)  
            {  
                return (row + 1) * (column + 1);  
            }  
  
            @Override  
            public int getRowCount()  
            {  
                return 3;  
            }  
  
            @Override  
            public int getColumnCount()  
            {  
                return 3;  
            }  
  
            @Override  
            public void setValueAt(Object aValue, int row, int column)  
            {  
                System.out.println(aValue + "  setValueAt");  
            }  
  
            @Override  
            public boolean isCellEditable(int row, int column)  
            {  
                // 带有按钮列的功能这里必须要返回true不然按钮点击时不会触发编辑效果，也就不会触发事件。   
                if (column == 2)  
                {  
                    return true;  
                }  
                else  
                {  
                    return false;  
                }  
            }  
        });  
  
        this.table.getColumnModel().getColumn(2).setCellEditor(new MyButtonEditor());  
  
        this.table.getColumnModel().getColumn(2).setCellRenderer(new MyButtonRender());  
  
        this.table.setRowSelectionAllowed(false);// 禁止表格的选择功能。不然在点击按钮时表格的整行都会被选中。也可以通过其它方式来实现。   
    }  
} 