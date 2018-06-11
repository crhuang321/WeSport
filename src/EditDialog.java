/*EditDialog.java
 * EditDialog类：裁判修改信息
 * 1.界面：裁判信息输入框、确认和取消按钮
 * 2.功能：连接数据库、修改裁判信息
 */
import java.awt.*;
import javax.swing.*;

import org.ho.yaml.Yaml;

import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
public class EditDialog extends Dialog implements ActionListener{
	//用于数据库连接
	public static Connection connection = null;
	public static Statement statement = null;
	public static ResultSet resultset = null;
	
	public static String DRIVER;
	public static String DATABASE;
	public static String USERNAME;
	public static String PASSWORD;
	//修改裁判信息面板设置
	private JFrame editFrame;
	private Button ok=new Button("确定");
	private Button cancel=new Button("取消");
	private Label labelName=new Label("姓名",Label.LEFT);
	private Label labelTelephone=new Label("电话",Label.LEFT);
	private Label labelId=new Label("账号",Label.LEFT);
	TextField refTextName=new TextField(20);
	TextField refTextId=new TextField(20);
	TextField refTextPhone=new TextField(20);
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
	public EditDialog(Frame parent,String title) {
		super(parent,title,true);
		//读取配置文件
		try {
			readConfig();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Panel pName=new Panel();
		pName.setLayout(new FlowLayout());
		pName.add(labelName);
		pName.add(refTextName);

		Panel pId=new Panel();
		pId.add(labelId);
		pId.add(refTextId);

		Panel pTelephone=new Panel();
		pTelephone.add(labelTelephone);
		pTelephone.add(refTextPhone);
		
		Panel pRefereeInfo=new Panel();
		pRefereeInfo.setLayout(new GridLayout(3,1));
		pRefereeInfo.add(pName);
		pRefereeInfo.add(pTelephone);
		pRefereeInfo.add(pId);
		
		Panel pButton=new Panel();
		pButton.setLayout(new FlowLayout());
		pButton.add(ok);		ok.addActionListener(this);
		pButton.add(cancel);		cancel.addActionListener(this);

		refTextName.setText(Referee.refName);
		refTextId.setText(Referee.refId);
		refTextPhone.setText(Referee.refPhone); 

		editFrame = new JFrame("修改个人信息");
		editFrame.setSize(300,200);
		editFrame.setLocation(560,260);
		editFrame.setLayout(new BorderLayout());
		editFrame.add("Center",pRefereeInfo);
		editFrame.add("South",pButton);
		pack(); editFrame.setVisible(true);
		editFrame.setResizable(false);
		editFrame.addWindowListener(new WindowCloser());
	}
	//更新裁判信息
	public void updateReferee() throws Exception{
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		String query ="update user set name = '" + refTextName.getText() + "' where id = '" + Referee.refId + "';";
		statement.executeUpdate(query);
		query ="update user set phone = '" + refTextPhone.getText() + "' where id = '" + Referee.refId + "';";
		statement.executeUpdate(query);
		query ="update user set id = '" + refTextId.getText() + "' where id = '" + Referee.refId + "';";
		statement.executeUpdate(query);
		statement.close();
		statement = null;
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==ok) {
			try {
				updateReferee();
				Referee.refId = refTextId.getText();
				Referee.refName = refTextName.getText();
				Referee.refPhone = refTextPhone.getText();
			}catch (Exception ex) {
				ex.printStackTrace();
			}
			editFrame.dispose();
			Referee ref = new Referee(Referee.refId);
		}else if(e.getSource()==cancel) {
			editFrame.dispose();
			Referee ref = new Referee(Referee.refId);
		}
	}
	private class WindowCloser extends WindowAdapter{
		public void WindowClosing(WindowEvent e) {
			Referee ref = new Referee(Referee.refId);
			editFrame.dispose();
		}
	}
}