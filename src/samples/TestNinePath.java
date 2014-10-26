package samples;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jb2011.ninepatch4j.NinePatch;

/**
 * 本类用于测试从Android中引入的NinePatch（九格图）技术的可行性.
 * 
 * @author jb2011@163.com
 * @version 1.0
 */
class TestNinePath extends JPanel
{
    //NinePatch作为全局对象，提高性能
    private NinePatch mPatch;
    
    public TestNinePath()
    {
        super (new BorderLayout());

        //*** 关键代码：读取9格图 START
        try{
//            InputStream stream = this .getClass().getResourceAsStream(
////                    "content_bg2.9.png"
//                    "res/removeable/content_bg2.9.png"
//                    );
//            
            BufferedImage read = ImageIO.read(new File("res/removeable/content_bg2.9.png"));
            
            mPatch = NinePatch.load(read, true /* is9Patch*/, false /* convert */);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //*** 关键代码：读取9格图 END
        
        //加入一个面板，用于演示
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        //该面板设置成背景透明
        p.setOpaque(false);
        this.add(p);
        
        //加入演示组件
        p.add(new JButton("JButton 1"));
        p.add(new JButton("JButton 2"));
        p.add(new JButton("JButton 3"));
        p.add(new JButton("JButton 4"));
    }

    /**
     * 重写父类方法，以便实现自定义背景的绘制.
     */
    @Override protected void paintComponent(Graphics g) 
    {
        Graphics2D g2 = (Graphics2D) g;
        Rectangle clip = g2.getClipBounds();
        
        //*** 关键代码：使用9格图 START
        //使用9格图绘制面板的背景
        mPatch.draw(g2, clip.x, clip.y, clip.width, clip.height);
        //*** 关键代码：使用9格图 END
    }
    
    public static void main(final String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
			public void run() {
                JFrame frame = new JFrame();
                frame.setContentPane(new JPanel(new BorderLayout()));
                ((JPanel)frame.getContentPane()).setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                frame.getContentPane().add(new TestNinePath(), BorderLayout.CENTER);
                frame.setSize(300,250);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}