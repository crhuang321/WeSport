/*Leader.java
 * Leader类：裁判登录成功页面
 * 1.界面：所有参赛运动员信息列表、裁判打分所需的输入框、信息修改和提交打分的按钮
 * 2.功能：连接数据库读取运动员信息，修改裁判信息、打分、提交打分
 */
import java.awt.*;
import javax.swing.*;
import org.ho.yaml.Yaml;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.List;
public class Referee extends JFrame implements ActionListener{
	//用于数据库连接
	public static Connection connection = null;
	public static Statement statement = null;
	public static ResultSet resultset = null; 
	
	public static String DRIVER;
	public static String DATABASE;
	public static String USERNAME;
	public static String PASSWORD;
	//裁判面板设置
	private JFrame refereeFrame;
	private MenuItem aboutUs = new MenuItem("About");
	private Button back = new Button("注销");
	private Button see = new Button("查看成绩");
	private JLabel rTitle=new JLabel("运动员列表 | 账号-姓名-性别-年龄-组号：参赛项目",JLabel.CENTER);
	private JLabel markLabel1 = new JLabel("双击左侧项目可查看已有成绩!",JLabel.CENTER);
	private JLabel markLabel2 = new JLabel("请选中左侧运动员项目进行评分！",JLabel.CENTER);
	private JLabel nB = new JLabel("正常分");
	private JLabel pB = new JLabel("惩罚分");
	private JLabel dB = new JLabel("奖励分");
	private JTextField nF = new JTextField(15);
	private JTextField pF = new JTextField(15);
	private JTextField dF = new JTextField(15);
	public static String refName;
	public static String refId;
	public static String refPhone;
	private JLabel labelRefereeName;
	private JLabel labelRefereePhone;
	private JLabel labelRefereeId;

	private JButton edit = new JButton("修改信息");
	private JButton mark = new JButton("提交评分");
	
	private List displayInRef = new List(10);
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
	//裁判构造方法
	public Referee(String refId){
		//读取配置文件
		try {
			readConfig();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			showRef(refId);
		}catch (Exception ex) {
			ex.printStackTrace();
		}//获取裁判信息
		
		try {
			showAthInRef();
		} catch (Exception e) {
			e.printStackTrace();
		}//获取运动员信息
		
		labelRefereeName = new JLabel("\t姓名：" + refName + "\t");
		labelRefereeId = new JLabel("\t账号：" + refId + "\t");
		labelRefereePhone = new JLabel("\t电话：" + refPhone + "\t");
		
		Panel pReferee = new Panel();
		pReferee.setLayout(new FlowLayout());
		pReferee.add(labelRefereeName);
		pReferee.add(labelRefereeId);
		pReferee.add(labelRefereePhone);
		
		Panel pTitle = new Panel();
		pTitle.setLayout(new BorderLayout());
		pTitle.add("West", back); back.addActionListener(this);
		pTitle.add("Center", pReferee); 
		pTitle.add("East", see); see.addActionListener(this);
		
		Panel buttons = new Panel();
		buttons.setLayout(new FlowLayout());
		buttons.add(edit);	edit.addActionListener(this);
		buttons.add(mark);	mark.addActionListener(this);
		
		Panel left = new Panel();
		left.setLayout(new BorderLayout());
		left.add("North", rTitle);	left.add("Center", displayInRef);	
		displayInRef.addMouseListener(mouseListener);
		
		Panel normalScore = new Panel();
		normalScore.setLayout(new FlowLayout());
		normalScore.add(nB);	normalScore.add(nF);
		
		Panel PScore = new Panel();
		PScore.setLayout(new FlowLayout());
		PScore.add(pB);	PScore.add(pF);
		
		Panel DScore = new Panel();
		DScore.setLayout(new FlowLayout());
		DScore.add(dB);	DScore.add(dF);
		
		Panel pMarkLabel = new Panel();
		pMarkLabel.setLayout(new GridLayout(2,1));
		pMarkLabel.add(markLabel1);	pMarkLabel.add(markLabel2);
		
		Panel pmark = new Panel();
		pmark.setLayout(new GridLayout(4,1));
		pmark.add(pMarkLabel);
		pmark.add(normalScore);
		pmark.add(PScore);
		pmark.add(DScore);
		
		Panel right = new Panel();
		right.setLayout(new BorderLayout());
		right.add("Center", pmark);
		right.add("South", buttons);
		
		Panel down = new Panel();
		down.setLayout(new GridLayout(1,2));
		down.add(left);	down.add(right);
		
		refereeFrame = new JFrame("体育赛事管理系统（裁判）");
		refereeFrame.setSize(1000,700);
		refereeFrame.setLocation(220,90);
		//设置菜单——关于
		Menu help = new Menu("Help");
		help.add(aboutUs); aboutUs.addActionListener(this);
		MenuBar bar = new MenuBar();
		bar.add(help);
		refereeFrame.setMenuBar(bar);
		
		refereeFrame.setLayout(new BorderLayout());  
		refereeFrame.add("North", pTitle);
		refereeFrame.add("Center", down);
		JLabel none = new JLabel(" ");
		refereeFrame.add("South", none);
		pack(); refereeFrame.setVisible(true);
		refereeFrame.setResizable(false);
		refereeFrame.addWindowListener(new InnerWindowCloser());//窗口关闭
	}
	//显示运动员列表
	public void showAthInRef() throws Exception {
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		String query ="select * from athlete;";
		resultset = statement.executeQuery(query);
		displayInRef.removeAll();
		while(resultset.next()) {
			String iitem = new String("");
			iitem = resultset.getString("id") + "-" +
					resultset.getString("name") + "-" +
					resultset.getString("sex") + "-" +
					resultset.getString("age") + "-" +
					resultset.getString("group_no") + ":" + resultset.getString("item");
			displayInRef.add(iitem);
			} 
		statement.close();
		statement = null;
	}
	//获取裁判信息
	public void showRef(String ref) throws Exception{
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		String query ="select * from user where id = '" + ref + "';";
		resultset = statement.executeQuery(query);
		while(resultset.next()) {
			refName = resultset.getString("name");
			refPhone = resultset.getString("phone");
			refId = resultset.getString("id");
		}
		statement.close();
		statement = null;
	}
	//提交成绩
	public void markAthScore(String athId) throws Exception{
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		String query ="insert into score(ath_no,n_score,p_score,d_score)"
				+ " values('" + athId + "','" + nF.getText() + "','" + pF.getText() + "','" + dF.getText() + "');";
		statement.executeUpdate(query);
		statement.close();
		statement = null;
	}
	//在成绩表中查找
	public boolean findAthScore(String athId) throws Exception{
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		String query ="select * from score where ath_no = '" + athId + "';";
		resultset = statement.executeQuery(query);
		while(resultset.next()) {
			return true;
		}
		statement.close();
		statement = null;
		return false;
	}
	//查询成绩
	public void fAS(String athId) throws Exception{
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		String query ="select * from score where ath_no = '" + athId + "';";
		resultset = statement.executeQuery(query);
        if(resultset.next()) {
	        nF.setText(resultset.getString("n_score"));
	        pF.setText(resultset.getString("p_score"));
	        dF.setText(resultset.getString("d_score"));
        }else {
			nF.setText("");
		    pF.setText("");
		    dF.setText("");
	    }
		statement.close();
		statement = null;
	}
	 // 双击鼠标事件
    MouseListener mouseListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent mouseEvent) {
        		String[] s = new String[20];
			s = displayInRef.getSelectedItem().toString().split("-");
			try {
				fAS(s[0]);	//查看成绩
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    };
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource() == edit){
			EditDialog dlg = new EditDialog(this,"修改裁判信息");
			dlg.dispose();
			refereeFrame.dispose();
		}else if(ae.getSource() == mark){
			try{
				if(JOptionPane.showConfirmDialog(null, "确认提交？", "", JOptionPane.YES_NO_OPTION) == 0) {
					String[] s = new String[20];
					s = displayInRef.getSelectedItem().toString().split("-");
					if(findAthScore(s[0])) {//如果存在成绩，提示不可重复打分
						JOptionPane.showMessageDialog(null, "不可重复打分！", "", JOptionPane.YES_NO_OPTION);
					}else {
						markAthScore(s[0]);//如果没有成绩，插入一条记录
					}
				}
			}catch (Exception ex) {
				ex.printStackTrace();
			}
		}else if(ae.getSource() == back){
			//返回登录界面
			if(JOptionPane.showConfirmDialog(null, "真的要注销？", "", JOptionPane.YES_NO_OPTION) == 0) {
				WeSport weSport = new WeSport();
				refereeFrame.dispose();
			}
		}else if(ae.getSource() == see){
			SeeGrade grade = new SeeGrade();
		}else if(ae.getSource() == aboutUs){
			AboutUs au = new AboutUs();
		}
	}
}