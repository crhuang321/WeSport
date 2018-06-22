/*Manager.java
 * Manager类：管理员登录成功页面
 * 1.界面包括：所有参赛运动员信息列表、六个比赛组列表、赛事编排所需按钮
 * 2.功能包括：连接数据库读取运动员和管理员信息、设置与重置赛事安排情况
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
public class Manager extends JFrame implements ActionListener{
	//用于数据库连接
	public static Connection connection = null;
	public static Statement statement = null;
	public static ResultSet resultset = null; 
	
	public static String DRIVER;
	public static String DATABASE;
	public static String USERNAME;
	public static String PASSWORD;
	
	private JFrame managerFrame;
	private MenuItem aboutUs = new MenuItem("About");
	private Button back = new Button("注销");
	private Button see = new Button("查看成绩");
	private JLabel rTitle=new JLabel("运动员列表 | 账号-姓名-性别-年龄-组号：参赛项目",JLabel.CENTER);
	private JLabel markLabel1 = new JLabel("请选择运动员安排比赛!",JLabel.CENTER);
	public static String manName;
	public static String manId;
	public static String manPhone;
	private JLabel labelManagerName;
	private JLabel labelManagerPhone;
	private JLabel labelManagerId;

	private JButton reset = new JButton("重置");
	private JButton submit = new JButton("提交");
	
	private List displayInMan = new List(300);
	private JLabel One = new JLabel("7—8岁男子组",JLabel.CENTER);
	private JLabel Two = new JLabel("7—8岁女子组",JLabel.CENTER);
	private JLabel Three = new JLabel("9—10岁男子组",JLabel.CENTER);
	private JLabel Four = new JLabel("9—10岁女子组",JLabel.CENTER);
	private JLabel Five = new JLabel("11—12岁男子组",JLabel.CENTER);
	private JLabel Six = new JLabel("11—12岁女子组",JLabel.CENTER);
	private List one = new List(10);	
	private List two = new List(10);
	private List three = new List(10);
	private List four = new List(10);
	private List five = new List(10);
	private List six = new List(10);
	
	//每个比赛组的赛事编排：运动员增减
	private JButton oneAdd = new JButton("添加");
	private JButton oneSub = new JButton("移出");
	private JButton twoAdd = new JButton("添加");
	private JButton twoSub = new JButton("移出");
	private JButton threeAdd = new JButton("添加");
	private JButton threeSub = new JButton("移出");
	private JButton fourAdd = new JButton("添加");
	private JButton fourSub = new JButton("移出");
	private JButton fiveAdd = new JButton("添加");
	private JButton fiveSub = new JButton("移出");
	private JButton sixAdd = new JButton("添加");
	private JButton sixSub = new JButton("移出");
	
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
	//管理员构造方法
	public Manager(String manId){
		//读取配置文件
		try {
			readConfig();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			showMan(manId);
		}catch (Exception ex) {
			ex.printStackTrace();
		}//获取管理员信息
		
		try {
			showAthInMan();
		} catch (Exception e) {
			e.printStackTrace();
		}//获取运动员信息
		
		labelManagerName = new JLabel("\t姓名：" + manName + "\t");
		labelManagerId = new JLabel("\t账号：" + manId + "\t");
		labelManagerPhone = new JLabel("\t电话：" + manPhone + "\t");
		
		oneAdd.addActionListener(this);
		oneSub.addActionListener(this);
		twoAdd.addActionListener(this);
		twoSub.addActionListener(this);
		threeAdd.addActionListener(this);
		threeSub.addActionListener(this);
		fourAdd.addActionListener(this);
		fourSub.addActionListener(this);
		fiveAdd.addActionListener(this);
		fiveSub.addActionListener(this);
		sixAdd.addActionListener(this);
		sixSub.addActionListener(this);
		
		Panel pManager = new Panel();
		pManager.setLayout(new FlowLayout());
		pManager.add(labelManagerName);
		pManager.add(labelManagerId);
		pManager.add(labelManagerPhone);
		
		Panel pTitle = new Panel();
		pTitle.setLayout(new BorderLayout());
		pTitle.add("West", back); back.addActionListener(this);
		pTitle.add("Center", pManager); 
		pTitle.add("East", see); see.addActionListener(this);
		
		Panel buttons = new Panel();
		buttons.setLayout(new FlowLayout());
		buttons.add(reset);	reset.addActionListener(this);
		buttons.add(submit);	submit.addActionListener(this);
		
		Panel left = new Panel();
		left.setLayout(new BorderLayout());
		left.add("North", rTitle);	left.add("Center", displayInMan);	
		
		Panel ppOne = new Panel();
		ppOne.setLayout(new BorderLayout());
		ppOne.add("West", oneAdd); ppOne.add("Center", One); ppOne.add("East", oneSub);

		Panel ppTwo = new Panel();
		ppTwo.setLayout(new BorderLayout());
		ppTwo.add("West", twoAdd); ppTwo.add("Center", Two); ppTwo.add("East", twoSub);

		Panel ppThree = new Panel();
		ppThree.setLayout(new BorderLayout());
		ppThree.add("West", threeAdd); ppThree.add("Center", Three); ppThree.add("East", threeSub);

		Panel ppFour = new Panel();
		ppFour.setLayout(new BorderLayout());
		ppFour.add("West", fourAdd); ppFour.add("Center", Four); ppFour.add("East", fourSub);

		Panel ppFive = new Panel();
		ppFive.setLayout(new BorderLayout());
		ppFive.add("West", fiveAdd); ppFive.add("Center", Five); ppFive.add("East", fiveSub);

		Panel ppSix = new Panel();
		ppSix.setLayout(new BorderLayout());
		ppSix.add("West", sixAdd); ppSix.add("Center", Six); ppSix.add("East", sixSub);


		Panel pOne = new Panel();
		pOne.setLayout(new BorderLayout());
		pOne.add("North", ppOne);	pOne.add("Center",one);
		
		Panel pTwo = new Panel();
		pTwo.setLayout(new BorderLayout());
		pTwo.add("North", ppTwo);	pTwo.add("Center",two);
		
		Panel pThree = new Panel();
		pThree.setLayout(new BorderLayout());
		pThree.add("North", ppThree);	pThree.add("Center",three);
		
		Panel pFour = new Panel();
		pFour.setLayout(new BorderLayout());
		pFour.add("North", ppFour);	pFour.add("Center",four);
		
		Panel pFive = new Panel();
		pFive.setLayout(new BorderLayout());
		pFive.add("North", ppFive);	pFive.add("Center",five);
		
		Panel pSix = new Panel();
		pSix.setLayout(new BorderLayout());
		pSix.add("North", ppSix);	pSix.add("Center",six);
		
		Panel pManage = new Panel();
		pManage.setLayout(new GridLayout(6,1));
		pManage.add(pOne); pManage.add(pTwo);
		pManage.add(pThree); pManage.add(pFour);
		pManage.add(pFive); pManage.add(pSix);
		
		Panel right = new Panel();
		right.setLayout(new BorderLayout());
		right.add("North", markLabel1);
		right.add("Center", pManage);
		right.add("South", buttons);
		
		Panel down = new Panel();
		down.setLayout(new GridLayout(1,2));
		down.add(left);	down.add(right);
		
		managerFrame = new JFrame("体育赛事管理系统（管理员）");
		managerFrame.setSize(1000,700);
		managerFrame.setLocation(220,90);
		//设置菜单——关于
		Menu help = new Menu("Help");
		help.add(aboutUs); aboutUs.addActionListener(this);
		MenuBar bar = new MenuBar();
		bar.add(help);
		managerFrame.setMenuBar(bar);
		
		managerFrame.setLayout(new BorderLayout());  
		managerFrame.add("North", pTitle);
		managerFrame.add("Center", down);
		JLabel none = new JLabel(" ");
		managerFrame.add("South", none);
		pack(); managerFrame.setVisible(true);
		managerFrame.setResizable(false);
		managerFrame.addWindowListener(new InnerWindowCloser());//窗口关闭
	}
	//显示运动员列表
	public void showAthInMan() throws Exception {
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		String query ="select * from athlete;";
		resultset = statement.executeQuery(query);
		displayInMan.removeAll();
		while(resultset.next()) {
			String iitem = new String("");
			iitem = resultset.getString("id") + "-" +
					resultset.getString("name") + "-" +
					resultset.getString("sex") + "-" +
					resultset.getString("age") + "-" +
					resultset.getString("group_no") + ":" + resultset.getString("item");
			displayInMan.add(iitem);
		} 
		statement.close();
		statement = null;
	}
	//获取管理员信息
	public void showMan(String man) throws Exception{
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		String query ="select * from user where id = '" + man + "';";
		resultset = statement.executeQuery(query);
		while(resultset.next()) {
			manName = resultset.getString("name");
			manPhone = resultset.getString("phone");
			manId = resultset.getString("id");
		}
		statement.close();
		statement = null;
	}
	//在安排表中删除所有安排信息
	public void delAthInArrange() throws Exception {
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		String query ="delete from arrange;";
		statement.executeUpdate(query);
		statement.close();
		statement = null;
	}
	//提交赛事安排
	public void markAthArrange(String athId, String con) throws Exception{
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		String query ="insert into arrange(ath_no,contest)" + " values('" + athId + "','" + con + "');";
		statement.executeUpdate(query);
		statement.close();
		statement = null;
	}
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource() == reset){
			if(JOptionPane.showConfirmDialog(null, "确认重置赛事安排？", "", JOptionPane.YES_NO_OPTION) == 0){
				//重置赛事安排
				for(int i=one.getItemCount()-1; i>=0; i--){
					displayInMan.add(one.getItem(0));	one.remove(one.getItem(0));
				}
				for(int i=two.getItemCount()-1; i>=0; i--){
					displayInMan.add(two.getItem(0));	two.remove(two.getItem(0));
				}
				for(int i=three.getItemCount()-1; i>=0; i--){
					displayInMan.add(three.getItem(0));	three.remove(three.getItem(0));
				}
				for(int i=four.getItemCount()-1; i>=0; i--){
					displayInMan.add(four.getItem(0));	four.remove(four.getItem(0));
				}
				for(int i=five.getItemCount()-1; i>=0; i--){
					displayInMan.add(five.getItem(0));	five.remove(five.getItem(0));
				}
				for(int i=six.getItemCount()-1; i>=0; i--){
					displayInMan.add(six.getItem(0));	six.remove(six.getItem(0));
				}
			}
		}else if(ae.getSource() == submit){
			try{
				if(JOptionPane.showConfirmDialog(null, "确认提交？", "", JOptionPane.YES_NO_OPTION) == 0) {
					String[] s = new String[20];
					//提交赛事安排
					delAthInArrange();				
						for(int i=one.getItemCount()-1; i>=0; i--) {
						s = one.getItem(i).toString().split("-");
						markAthArrange(s[0], "Boy7_8");
					}
					for(int i=two.getItemCount()-1; i>=0; i--) {
						s = two.getItem(i).toString().split("-");
						markAthArrange(s[0], "Girl7_8");
					}
					for(int i=three.getItemCount()-1; i>=0; i--) {
						s = three.getItem(i).toString().split("-");
						markAthArrange(s[0], "Boy9_10");
					}
					for(int i=four.getItemCount()-1; i>=0; i--) {
						s = four.getItem(i).toString().split("-");
						markAthArrange(s[0], "Girl9_10");
					}
					for(int i=five.getItemCount()-1; i>=0; i--) {
						s = five.getItem(i).toString().split("-");
						markAthArrange(s[0], "Boy11_12");
					}
					for(int i=six.getItemCount()-1; i>=0; i--) {
						s = six.getItem(i).toString().split("-");
						markAthArrange(s[0], "Girl11_12");
					}
				}
			}catch (Exception ex) {
				ex.printStackTrace();
			}
		}else if(ae.getSource() == back){
			//返回登录界面重新登录
			if(JOptionPane.showConfirmDialog(null, "真的要注销？", "", JOptionPane.YES_NO_OPTION) == 0) {
				WeSport weSport = new WeSport();
				managerFrame.dispose();
			}
		}else if(ae.getSource() == see){//查看最终成绩
			SeeGrade grade = new SeeGrade();
		}else if(ae.getSource() == aboutUs){
			AboutUs au = new AboutUs();
		}else if(ae.getSource() == oneAdd){
			one.add(displayInMan.getSelectedItem());
			displayInMan.remove(displayInMan.getSelectedIndex());
		}else if(ae.getSource() == twoAdd){
			two.add(displayInMan.getSelectedItem());
			displayInMan.remove(displayInMan.getSelectedIndex());
		}else if(ae.getSource() == threeAdd){
			three.add(displayInMan.getSelectedItem());
			displayInMan.remove(displayInMan.getSelectedIndex());
		}else if(ae.getSource() == fourAdd){
			four.add(displayInMan.getSelectedItem());
			displayInMan.remove(displayInMan.getSelectedIndex());
		}else if(ae.getSource() == fiveAdd){
			five.add(displayInMan.getSelectedItem());
			displayInMan.remove(displayInMan.getSelectedIndex());
		}else if(ae.getSource() == sixAdd){
			six.add(displayInMan.getSelectedItem());
			displayInMan.remove(displayInMan.getSelectedIndex());
		}else if(ae.getSource() == oneSub){
			displayInMan.add(one.getSelectedItem());
			one.remove(one.getSelectedIndex());
		}else if(ae.getSource() == twoSub){
			displayInMan.add(two.getSelectedItem());
			two.remove(two.getSelectedIndex());
		}else if(ae.getSource() == threeSub){
			displayInMan.add(three.getSelectedItem());
			three.remove(three.getSelectedIndex());
		}else if(ae.getSource() == fourSub){
			displayInMan.add(four.getSelectedItem());
			four.remove(four.getSelectedIndex());
		}else if(ae.getSource() == fiveSub){
			displayInMan.add(five.getSelectedItem());
			five.remove(five.getSelectedIndex());
		}else if(ae.getSource() == sixSub){
			displayInMan.add(six.getSelectedItem());
			six.remove(six.getSelectedIndex());
		}
	}
}