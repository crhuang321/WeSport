/*AboutUs.java
 * AboubUs类：关于本体育赛事管理系统的一些说明和介绍
 * 1.界面包括系统说明和介绍、版权信息
 */
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import org.ho.yaml.Yaml;
public class AboutUs extends JFrame{
	private JFrame aboutFrame;
	private String aboutContent;
	private class InnerWindowCloser extends WindowAdapter{//窗口关闭
		public void windowClosing(WindowEvent we) {
			aboutFrame.dispose();
		}
	}
	//读取配置文件
	public void readConfig() throws FileNotFoundException {
        File dumpFile=new File(System.getProperty("user.dir") + "/_config.yml");
        Map database =Yaml.loadType(dumpFile, HashMap.class);
        for(Object key:database.keySet()){
        	if(key.toString().equals("ABOUT.content")) {
        		aboutContent = database.get(key).toString();
    		}
        }
    }
	public AboutUs() {
		aboutFrame = new JFrame("About");
		aboutFrame.setSize(500,400);
		aboutFrame.setLocation(460,200);
		
		//读取配置文件
		try {
			readConfig();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Label none = new Label();
		
		JLabel title = new JLabel("<html><br />体操赛事管理系统</html>", JLabel.CENTER);
		title.setFont(new Font("简体中文", Font.PLAIN, 20));
		
		JLabel content = new JLabel(aboutContent,JLabel.CENTER);
		Panel _content = new Panel();
		_content.add(content);
		
		Panel pContent = new Panel();
		pContent.setLayout(new BorderLayout());
		pContent.add("Center",_content);
		
		JLabel copyright = new JLabel("Copyright © 2018 - 2018 CoolR.All Rights Reserved.",JLabel.CENTER);
		Panel pCopyright = new Panel();
		pCopyright.setLayout(new GridLayout(3,1));
		pCopyright.add(copyright); pCopyright.add(none);
		
		aboutFrame.setLayout(new BorderLayout()); 
		aboutFrame.add("North", title);
		aboutFrame.add("Center", pContent);
		aboutFrame.add("South", pCopyright);
		pack(); aboutFrame.setVisible(true);
		aboutFrame.addWindowListener(new InnerWindowCloser());//窗口关闭
	}
	public static void main(String[] args) {
		AboutUs au = new AboutUs();
	}
}