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
		
//		JLabel content = new JLabel("<html><p><br>"
//				+ "体操赛事管理系统是一个管理体操比赛的信息系统。该系统的使用者为<br>"
//				+ "运动员代表队队长、体操赛事裁判和系统管理员。运动员代表队队长登<br>"
//				+ "录系统输入本队运动员信息及参赛项目。系统管理员登录系统安排比赛<br>"
//				+ "场次。体操赛事裁判登录系统对运动员表现进行评分。按照年龄和姓别<br>"
//				+ "分为7-8岁男子组、7-8岁女子组、9-10岁男子组、9-10岁女子组、<br>"
//				+ "11-12岁男子组、11-12岁女子组，共六个比赛组。该系统能够为整个<br>"
//				+ "体操赛事提供人员信息存取、赛事编排和成绩公布等服务。<br>"
//				+ "<br>账号说明: <br>队长———10xx \t\t队医——20xx \t\t教练——30xx <br>"
//				+ "运动员——40xx \t\t裁判——50xx \t\t管理员—60xx"
//				+ "<br><br>欢迎使用。</p></html>",JLabel.CENTER);
		
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
