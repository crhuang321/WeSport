/*AddAthlete.java
 * AddAthlete类：增加运动员
 * 1.界面：运动员信息输入框、确认和取消按钮
 * 2.连接数据库，写运动员信息、信息验证
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
class AddAthlete extends Dialog implements ActionListener{ 
	//用于数据库连接
	public static Connection connection = null;
	public static Statement statement = null;
	public static ResultSet resultset = null;
	
	public static String DRIVER;
	public static String DATABASE;
	public static String USERNAME;
	public static String PASSWORD;
	//添加运动员所需，每次重新赋值
	public String LName = new String();
	public String LId = new String();
	public String LSex = new String();
	public String LAge = new String();
	public String LGroup = new String("");
	public String LItem = new String("");//将参加的所有项目拼为一个字符串
	
	private JButton ok=new JButton("确定");
	private JButton cancel=new JButton("取消");
	private JLabel labelName=new JLabel("姓名",JLabel.LEFT);
	private JLabel labelId=new JLabel("账号",JLabel.LEFT);
	private JLabel labelSex=new JLabel("性别",JLabel.LEFT);
	private JLabel labelAge=new JLabel("年龄",JLabel.LEFT);
	private JLabel labelGroup=new JLabel("组号",JLabel.LEFT);
	private JFrame addAthlete;//面板
	//比赛项目
	private JLabel labelEvent=new JLabel("比赛项目",JLabel.CENTER);
	private JCheckBox checkBox01 = new JCheckBox("单杠");
	private JCheckBox checkBox02 = new JCheckBox("双杠");
	private JCheckBox checkBox03 = new JCheckBox("吊环");
	private JCheckBox checkBox04 = new JCheckBox("跳马");
	private JCheckBox checkBox05 = new JCheckBox("鞍马");
	private JCheckBox checkBox06 = new JCheckBox("蹦床");
	private JCheckBox checkBox07 = new JCheckBox("平衡木");
	private JCheckBox checkBox08 = new JCheckBox("自由体操");

	TextField textName = new TextField(20);
	TextField textSex = new TextField(20);
	TextField textAge = new TextField(20);
	TextField textId = new TextField(20);
	TextField textGroup = new TextField(20);
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
	public AddAthlete(Frame parent,String title) {
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
		pName.add(textName);

		Panel pAge=new Panel();
		pAge.add(labelAge);
		pAge.add(textAge);
		
		Panel pSex=new Panel();
		pSex.add(labelSex);
		pSex.add(textSex);
		
		Panel pId=new Panel();
		pId.add(labelId);
		pId.add(textId);

		Panel pGroup=new Panel();
		pGroup.add(labelGroup);
		pGroup.add(textGroup);

		Panel pAthleteInfo=new Panel();
		pAthleteInfo.setLayout(new GridLayout(5,1));
		pAthleteInfo.add(pName);
		pAthleteInfo.add(pId);
		pAthleteInfo.add(pSex);
		pAthleteInfo.add(pAge);
		pAthleteInfo.add(pGroup);
		
		Panel pEvent = new Panel();
		pEvent.setLayout(new GridLayout(4,2));
		pEvent.add(checkBox01);
		pEvent.add(checkBox02);
		pEvent.add(checkBox03);
		pEvent.add(checkBox04);
		pEvent.add(checkBox05);
		pEvent.add(checkBox06);
		pEvent.add(checkBox07);
		pEvent.add(checkBox08);
		
		Panel pButton=new Panel();
		pButton.setLayout(new FlowLayout());
		pButton.add(ok); ok.addActionListener(this);
		pButton.add(cancel);	 cancel.addActionListener(this);
		
		addAthlete = new JFrame("添加运动员");
		addAthlete.setSize(250,320);
		addAthlete.setLocation(585,200);		
		addAthlete.setLayout(new BorderLayout());
		addAthlete.add("North", pAthleteInfo);
		addAthlete.add("Center", pEvent);
		addAthlete.add("South",pButton);
		addAthlete.addWindowListener(new WindowCloser());
		addAthlete.setResizable(false);
		pack(); addAthlete.setVisible(true);
	}
	//添加运动员：写数据库
	public void addAth(String query) throws Exception{
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		int Res = statement.executeUpdate(query);
		System.out.println(Res > 0 ? "插入数据成功" : "插入数据失败");

		statement.close();
		statement = null;
	}
	//读数据库：查找运动员LId是否已经存在
	public boolean findAth(String newId) throws Exception {
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		String query ="select id from athlete";
		resultset = statement.executeQuery(query);
		while(resultset.next()) {
			if(resultset.getString("id").equals(newId))
				return true;
		} 
		statement.close();
		statement = null;
		return false;
	}
	//将比赛项目拼接为一个字符串
	public void itemToString() {
		if(checkBox01.isSelected()) { LItem+=checkBox01.getText(); LItem+="-";}
		if(checkBox02.isSelected()) { LItem+=checkBox02.getText(); LItem+="-";}
		if(checkBox03.isSelected()) { LItem+=checkBox03.getText(); LItem+="-";}
		if(checkBox04.isSelected()) { LItem+=checkBox04.getText(); LItem+="-";}
		if(checkBox05.isSelected()) { LItem+=checkBox05.getText(); LItem+="-";}
		if(checkBox06.isSelected()) { LItem+=checkBox06.getText(); LItem+="-";}
		if(checkBox07.isSelected()) { LItem+=checkBox07.getText(); LItem+="-";}
		if(checkBox08.isSelected()) { LItem+=checkBox08.getText(); LItem+="-";}
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==ok) {
			LName = textName.getText();
			LId = textId.getText();
			LAge = textAge.getText();
			LSex = textSex.getText();
			LGroup = textGroup.getText();
			itemToString();
			try {
				if(LId.equals("") || LName.equals("")) {//用户没有输入账号或者姓名，账号号为主键不能为空
					JOptionPane.showMessageDialog(null, "你必须输入姓名和账号!", "", JOptionPane.ERROR_MESSAGE);
				}else {
					String query = "INSERT INTO `wesport`.`athlete` (`id`, `name`, `sex`, `age`, `group_no`, `item`)"
							+ "VALUES ('" + LId + "','"
							+ LName + "','"
							+ LSex + "','"
							+ LAge + "','"
							+ LGroup + "','"
							+ LItem + "');";
					try {
						if(findAth(LId)){//账号不能重复
							JOptionPane.showMessageDialog(null, "该账号已存在!", "", JOptionPane.ERROR_MESSAGE);
						}else {
							addAth(query);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					addAthlete.setVisible(false);
				}
			}catch (Exception ex) {
				ex.printStackTrace();
			}
		}else if(e.getSource()==cancel) {
			addAthlete.setVisible(false);
		}
	}
	private class WindowCloser extends WindowAdapter{
		public void WindowClosing(WindowEvent e) {
			AddAthlete.this.setVisible(false);
		}
	}
}