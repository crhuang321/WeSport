/*WeSport.java
 * WeSport项目主类：这是一个体操赛事管理系统，实现了整个项目的入口界面及功能。
 * 1.界面包括：系统名称及版权信息、用户名和密码输入框、注册与登录按钮
 * 2.实现功能：新用户注册、用户登录
 * 3.3类用户：参赛队长、赛事裁判、系统管理员
 * 4.使用MySql数据库实现数据存储和流通
*/
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.*; 
import javax.swing.*;
import org.ho.yaml.Yaml;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
public class WeSport extends JFrame implements ActionListener{
	//用于数据库连接
	public static Connection connection = null;
	public static Statement statement = null;
	public static ResultSet resultset = null; 
	
	public static String DRIVER;
	public static String DATABASE;
	public static String USERNAME;
	public static String PASSWORD;

	private static JFrame mainFrame;//主面板
	private MenuItem aboutUs = new MenuItem("About");
	private JLabel topTitle=new JLabel("体操赛事管理系统",JLabel.CENTER);
	private JLabel about = new JLabel("Copyright © 2018 - 2018 CoolR.All Rights Reserved.",JLabel.CENTER);
	private JLabel labelUserId=new JLabel("账号",JLabel.CENTER);
	private JLabel labelPassword=new JLabel("密码",JLabel.CENTER);
	private JTextField textUserId=new JTextField(15);
	private JPasswordField textPassword=new JPasswordField(15);
	private JButton signUp = new JButton("注册");
	private JButton signIn = new JButton("登录");
	private class InnerWindowCloser extends WindowAdapter{//窗口关闭
		public void windowClosing(WindowEvent we) {
			System.exit(0);
		}
	}
	//构造函数
	public WeSport(){
		//读取配置文件
		try {
			readConfig();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//主面板设置
		mainFrame = new JFrame();
		mainFrame.setSize(1000,700);	
		mainFrame.setLocation(220,90);
		Font font =new Font("简体中文", Font.PLAIN, 20);
		labelUserId.setFont(font);	//账号和密码
		labelPassword.setFont(font);
		signUp.setFont(font);	//登录与注册
		signIn.setFont(font);
		topTitle.setFont(new Font("简体中文", Font.PLAIN, 36));
		
		//设置菜单——关于
		Menu help = new Menu("Help");
		help.add(aboutUs);  
		aboutUs.addActionListener(this);
		MenuBar bar = new MenuBar();
		bar.add(help);
		mainFrame.setMenuBar(bar);
		
		//按钮
		Panel buttons = new Panel();
		buttons.setLayout(new FlowLayout());
		buttons.add(signUp);	signUp.addActionListener(this);
		buttons.add(signIn);	signIn.addActionListener(this);
		
		//账号
		Panel pUsername = new Panel();
		pUsername.add(labelUserId);
		pUsername.add(textUserId);textUserId.addActionListener(this);
		
		//密码
		Panel pPassword = new Panel();
		pPassword.add(labelPassword);
		pPassword.add(textPassword);textPassword.addActionListener(this);
		
		//账号密码区
		Panel sign = new Panel();
		sign.setLayout(new GridLayout(4,1));
		Label none = new Label();
		sign.add(none);	sign.add(none);
		sign.add(pUsername);	 sign.add(pPassword);	

		//主面板设置
		mainFrame.setLayout(new GridLayout(4,1));
		mainFrame.add(topTitle);  
		mainFrame.add(sign);  
		mainFrame.add(buttons);	
		mainFrame.add(about);
		pack(); mainFrame.setVisible(true);	mainFrame.setResizable(false);
		mainFrame.addWindowListener(new InnerWindowCloser());//窗口关闭
	}
	//读取配置文件
	public void readConfig() throws FileNotFoundException {
        File dumpFile=new File(System.getProperty("user.dir") + "/_config.yml");
        Map database =Yaml.loadType(dumpFile, HashMap.class);
        for(Object key:database.keySet()){
        		if(key.toString().equals("JDBC.driver")) {
        			DRIVER = database.get(key).toString();
        		}
        		if(key.toString().equals("JDBC.url")) {
        			DATABASE = database.get(key).toString();
        		}
        		if(key.toString().equals("JDBC.username")) {
        			USERNAME = database.get(key).toString();
        		}
        		if(key.toString().equals("JDBC.password")) {
        			PASSWORD = database.get(key).toString();
        		}
        }
    }
	//验证登录
	public int signIn(String oldId, String oldPass) throws Exception{
		//加载连接数据库
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		
		//用户名不存在
		String query1 ="select * from user where id='" + oldId + "';" ;
		resultset = statement.executeQuery(query1);
		if(!resultset.next()) {
			JOptionPane.showMessageDialog(null, "没有信息，请重新输入!", "️", JOptionPane.ERROR_MESSAGE);
			statement.close();
			statement = null;
			return 0;
		}
		//用户名存在，密码错误判断
		String query2 ="select * from user where id='" + oldId +"' and password='" + oldPass + "';" ;
		resultset = statement.executeQuery(query2);
		if(!resultset.next()) {
			JOptionPane.showMessageDialog(null, "密码错误!", "", JOptionPane.ERROR_MESSAGE);
			statement.close();
			statement = null;
			return 1;
		} 
		//账号密码正确：队长登录
		resultset = statement.executeQuery(query2);
		if(resultset.next()) {
			if(resultset.getString("statu").equals("队长")) {
				JOptionPane.showMessageDialog(null, "队长，你好！", "", JOptionPane.YES_NO_OPTION);   
				statement.close();
				statement = null;
				return 2;	
			}else if(resultset.getString("statu").equals("裁判")) {
				JOptionPane.showMessageDialog(null, "裁判，你好！", "", JOptionPane.YES_NO_OPTION);  
				statement.close();
				statement = null;
				return 3;
			}else if(resultset.getString("statu").equals("管理员")) {
				JOptionPane.showMessageDialog(null, "管理员，你好！", "", JOptionPane.YES_NO_OPTION);  
				statement.close();
				statement = null;
				return 4;	
			}else {
				statement.close();
				statement = null;
			}
		}
		return 5;
	}
	//自定义响应事件
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource() == aboutUs){
			AboutUs au = new AboutUs();	//关于面板
		}else if(ae.getSource() == signUp){
			SignUp dlg=new SignUp(this,"新用户注册"); //完成注册
			dlg.dispose();
		}else if(ae.getSource() == signIn){//用户登录
			try {
				switch(signIn(textUserId.getText(), textPassword.getText())) {
				case 0: 	break;	//用户名不存在，提示用户注册
				case	 1: break;	//用户名存在，但是密码错误
				case 2: 
					//队长登录成功
					Leader leader = new Leader(textUserId.getText());
					mainFrame.dispose();		break;
				case 3: 
					//裁判登录成功
					Referee ref = new Referee(textUserId.getText());
					mainFrame.dispose();		break;
				case 4: 	
					//管理员登录成功
					Manager man = new Manager(textUserId.getText());
					mainFrame.dispose();		break;
				default: JOptionPane.showMessageDialog(null, "你没有登录权限！", "", JOptionPane.YES_NO_OPTION); 
					break;
				}
			}catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	//程序入口
	public static void main(String args[]){
		WeSport weSport = new WeSport();
	}
}