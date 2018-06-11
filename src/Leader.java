/*Leader.java
 * Leader类：队长登录成功页面
 * 1.界面：队长、队医和教练信息、运动员信息列表、更新队长队医教练信息按钮、增删和更新运动员按钮
 * 2.功能：连接数据库读写运动员信息、读取队长队医和教练信息、更新信息
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
import java.awt.List;
public class Leader extends JFrame implements ActionListener{
	//用于数据库连接
	public static Connection connection = null;
	public static Statement statement = null;
	public static ResultSet resultset = null;
	
	public static String DRIVER;
	public static String DATABASE;
	public static String USERNAME;
	public static String PASSWORD;
	//队长面板设置
	private JFrame leaderFrame;
	private MenuItem aboutUs = new MenuItem("About"); 
	private Button back = new Button("注销");
	private Button see = new Button("查看成绩");
	public String noGroup = new StringBuffer("").toString();
	private Label topTitle=new Label("队伍编号：" + noGroup,Label.CENTER);
	
	private Button labelLeader = new Button("更新队长信息");
	private Label labelLeaderName = new Label("\t\t\t姓名 \t");
	private JTextField leaderName = new JTextField(25);
	private Label labelLeaderPhone = new Label("\t\t\t电话 \t");
	private JTextField leaderPhone = new JTextField(25);
	private Label labelLeaderId = new Label("\t\t\t账号 \t");
	private JTextField leaderId = new JTextField(25);

	private Button labelDoctor = new Button("更新队医信息");
	private Label labelDoctorName = new Label("\t\t\t姓名 \t");
	private JTextField doctorName = new JTextField(25);
	private Label labelDoctorPhone = new Label("\t\t\t电话 \t");
	private JTextField doctorPhone = new JTextField(25);
	private Label labelDoctorId = new Label("\t\t\t账号 \t");
	private JTextField doctorId = new JTextField(25);
	
	private Button labelTeacher = new Button("更新教练信息");
	private Label labelTeacherName = new Label("\t\t\t姓名 \t");
	private JTextField teacherName = new JTextField(25);
	private Label labelTeacherPhone = new Label("\t\t\t电话 \t");
	private JTextField teacherPhone = new JTextField(25);
	private Label labelTeacherId = new Label("\t\t\t账号 \t");
	private JTextField teacherId = new JTextField(25);

	private Button add = new Button("增加运动员");
	private Button update = new Button("更新列表");
	private Button sub = new Button("移除运动员");
	private List display = new List(10);
	private class InnerWindowCloser extends WindowAdapter{//窗口关闭
		public void windowClosing(WindowEvent we) {
			System.exit(0);
		}
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
	//队长构造方法
	public Leader(String leId){
		//读取配置文件
		try {
			readConfig();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try{
			noGroup = readLeader(leId);
			topTitle = new Label("队伍编号：" + noGroup,Label.CENTER);
		}catch (Exception ex) {
			ex.printStackTrace();
		}//获取队伍信息
		
		try{
			showAth();
		}catch (Exception ex) {
			ex.printStackTrace();
		}//读取运动员信息表
		
		Panel pTitle = new Panel();
		pTitle.setLayout(new BorderLayout());
		pTitle.add("West", back); back.addActionListener(this);
		pTitle.add("Center", topTitle); 
		pTitle.add("East", see); see.addActionListener(this);
		
		Panel pplN = new Panel();pplN.setLayout(new FlowLayout());pplN.add(labelLeaderName);pplN.add(leaderName);
		Panel pplI = new Panel();pplI.setLayout(new FlowLayout());pplI.add(labelLeaderId);pplI.add(leaderId);
		Panel pplP = new Panel();pplP.setLayout(new FlowLayout());pplP.add(labelLeaderPhone);pplP.add(leaderPhone);
		
		Panel ppdN = new Panel();ppdN.setLayout(new FlowLayout());ppdN.add(labelDoctorName);	ppdN.add(doctorName);
		Panel ppdI = new Panel();ppdI.setLayout(new FlowLayout());ppdI.add(labelDoctorId);	ppdI.add(doctorId);
		Panel ppdP = new Panel();ppdP.setLayout(new FlowLayout());ppdP.add(labelDoctorPhone);	ppdP.add(doctorPhone);
		
		Panel pptN = new Panel();pptN.setLayout(new FlowLayout());pptN.add(labelTeacherName);	pptN.add(teacherName);
		Panel pptI = new Panel();pptI.setLayout(new FlowLayout());pptI.add(labelTeacherId);	pptI.add(teacherId);
		Panel pptP = new Panel();pptP.setLayout(new FlowLayout());pptP.add(labelTeacherPhone);pptP.add(teacherPhone);
		
		Panel pLeader = new Panel();
		pLeader.setLayout(new GridLayout(4,1));
		pLeader.add(labelLeader); labelLeader.addActionListener(this);
		pLeader.add(pplN);
		pLeader.add(pplI);
		pLeader.add(pplP);
		
		Panel pDoctor = new Panel();
		pDoctor.setLayout(new GridLayout(4,1));
		pDoctor.add(labelDoctor);labelDoctor.addActionListener(this);
		pDoctor.add(ppdN);
		pDoctor.add(ppdI);
		pDoctor.add(ppdP);

		Panel pTeacher= new Panel();
		pTeacher.setLayout(new GridLayout(4,1));
		pTeacher.add(labelTeacher);labelTeacher.addActionListener(this);
		pTeacher.add(pptN);
		pTeacher.add(pptI);
		pTeacher.add(pptP);

		Panel show = new Panel();
		show.setLayout(new GridLayout(3,1));
		show.add(pLeader);
		show.add(pDoctor);	
		show.add(pTeacher);	
		
		Panel buttons = new Panel();
		buttons.setLayout(new FlowLayout());
		buttons.add(add);	add.addActionListener(this);
		buttons.add(update);	update.addActionListener(this);	
		buttons.add(sub);	sub.addActionListener(this);	
		
		Panel pdisplay = new Panel();
		pdisplay.setLayout(new BorderLayout());
		Label dispTop = new Label("运动员列表 | 账号-姓名-性别-年龄-组号：参赛项目");
		pdisplay.add("North", dispTop);
		pdisplay.add("Center", display);
		
		Panel center = new Panel();
		center.setLayout(new GridLayout(1,2));
		center.add(show);	center.add(pdisplay);
		
		leaderFrame = new JFrame("体操赛事管理系统（队长）");
		leaderFrame.setSize(1000,700);
		leaderFrame.setLocation(220,90);
		
		//设置菜单——关于
		Menu help = new Menu("Help");
		help.add(aboutUs); aboutUs.addActionListener(this);
		MenuBar bar = new MenuBar();
		bar.add(help);
		leaderFrame.setMenuBar(bar);
		
		leaderFrame.setLayout(new BorderLayout());
		leaderFrame.add("North", pTitle); 	
		leaderFrame.add("Center", center); 	
		leaderFrame.add("South", buttons);
		pack(); leaderFrame.setVisible(true); 
		leaderFrame.setResizable(false);
		leaderFrame.addWindowListener(new InnerWindowCloser());//窗口关闭
	}
	//显示运动员列表
	public void showAth() throws Exception {
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		String query ="select * from athlete where group_no = '" + noGroup + "';";
		resultset = statement.executeQuery(query);
		display.removeAll();
		while(resultset.next()) {
			String iitem = new String("");
			iitem = resultset.getString("id") + "-" +
					resultset.getString("name") + "-" +
					resultset.getString("sex") + "-" +
					resultset.getString("age") + "-" +
					resultset.getString("group_no") + ":" + resultset.getString("item");
			display.add(iitem);
		} 
		statement.close();
		statement = null;
	}
	//读取队长、队医、教练、队伍号等信息
	public String readLeader(String idL) throws Exception{
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		String query ="select * from user where id = '" + idL + "';";
		resultset = statement.executeQuery(query);
		String group = new String();
		//队伍号、队长信息
		while(resultset.next()) {
			group = resultset.getString("group_no");
			leaderName.setText(resultset.getString("name"));
			leaderId.setText(resultset.getString("id"));
			leaderPhone.setText(resultset.getString("phone"));
		}
		//队医信息
		query ="select * from user where statu = '队医' and group_no = '" + group + "';";
		resultset = statement.executeQuery(query);
		while(resultset.next()) {
			doctorName.setText(resultset.getString("name"));
			doctorId.setText(resultset.getString("id"));
			doctorPhone.setText(resultset.getString("phone"));
		}
		//教练信息
		query ="select * from user where statu = '教练' and group_no = '" + group + "';";
		resultset = statement.executeQuery(query);
		while(resultset.next()) {
			teacherName.setText(resultset.getString("name"));
			teacherId.setText(resultset.getString("id"));
			teacherPhone.setText(resultset.getString("phone"));
		}
		statement.close();
		statement = null;
		return group;
	}
	//更新队长信息
	public void updateLeader() throws Exception{
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		String query ="update user set name = '" + leaderName.getText() + "' where statu = '队长' and group_no ='1';";
		statement.executeUpdate(query);
		query ="update user set id = '" + leaderId.getText() + "' where statu = '队长' and group_no ='1';";
		statement.executeUpdate(query);
		query ="update user set phone = '" + leaderPhone.getText() + "' where statu = '队长' and group_no ='1';";
		statement.executeUpdate(query);
		statement.close();
		statement = null;
	}
	//更新队医信息
	public void updateDoctor() throws Exception{
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		String query ="update user set name = '" + doctorName.getText() + "' where statu = '队医' and group_no ='1';";
		statement.executeUpdate(query);
		query ="update user set id = '" + doctorId.getText() + "' where statu = '队医' and group_no ='1';";
		statement.executeUpdate(query);
		query ="update user set phone = '" + doctorPhone.getText() + "' where statu = '队医' and group_no ='1';";
		statement.executeUpdate(query);
		statement.close();
		statement = null;
	}
	//更新教练信息
	public void updateTeacher() throws Exception{
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		String query ="update user set name = '" + teacherName.getText() + "' where statu = '教练' and group_no ='1';";
		statement.executeUpdate(query);
		query ="update user set id = '" + teacherId.getText() + "' where statu = '教练' and group_no ='1';";
		statement.executeUpdate(query);
		query ="update user set phone = '" + teacherPhone.getText() + "' where statu = '教练' and group_no ='1';";
		statement.executeUpdate(query);
		statement.close();
		statement = null;
	}
	//在数据库删除指定运动员
	public void subAth(String sp) throws Exception {
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		String query ="delete from athlete where id = '" + sp + "';";
		statement.executeUpdate(query);
		statement.close();
		statement = null;
	}
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource() == add){
			AddAthlete dlg=new AddAthlete(this,"添加运动员");
			dlg.dispose();
			try{
				showAth();
			}catch (Exception ex) {
				ex.printStackTrace();
			}
		}else if(ae.getSource() == sub){
			try{
				String[] s = new String[20];
				s = display.getSelectedItem().toString().split("-");
				display.remove(display.getSelectedIndex());
				subAth(s[0]);
			}catch (Exception ex) {
				ex.printStackTrace();
			}
		}else if(ae.getSource() == update) {
			try{
				showAth();
			}catch (Exception ex) {
				ex.printStackTrace();
			}
		}else if(ae.getSource() == labelLeader) {
			try{
				updateLeader();//更新队长信息
				JOptionPane.showMessageDialog(null, "更新队长信息成功！", "", JOptionPane.YES_NO_OPTION);  
			}catch (Exception ex) {
				ex.printStackTrace();
			}
		}else if(ae.getSource() == labelDoctor) {
			try{
				updateDoctor();//更新队医信息
				JOptionPane.showMessageDialog(null, "更新队医信息成功！", "", JOptionPane.YES_NO_OPTION);
			}catch (Exception ex) {
				ex.printStackTrace();
			}
		}else if(ae.getSource() == labelTeacher) {
			try{
				updateTeacher();//更新教练信息
				JOptionPane.showMessageDialog(null, "更新教练信息成功！", "", JOptionPane.YES_NO_OPTION);
			}catch (Exception ex) {
				ex.printStackTrace();
			}
		}else if(ae.getSource() == back) {
			//返回登录界面
			if(JOptionPane.showConfirmDialog(null, "真的要注销？", "", JOptionPane.YES_NO_OPTION) == 0) {
				WeSport weSport = new WeSport();
				leaderFrame.dispose();
			}
		}else if(ae.getSource() == see) {
			//查看成绩页面
			SeeGrade grade = new SeeGrade();
		}else if(ae.getSource() == aboutUs) {
			AboutUs au = new AboutUs();
		}
	}
//	public static void main(String args[]){
//		Leader leader = new Leader(new String("101"));
//	}
}