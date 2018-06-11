/*SeeGrade.java
 * SeeGrade类：查看最终成绩的页面
 * 1.界面为一个JTable，显示最终的所有参赛队员的成绩等信息
 * 2.功能：读取数据库运动员基本信息、计算成绩
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableColumn;
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
public class SeeGrade extends JFrame{
	//用于数据库连接
	public static Connection connection = null;
	public static Statement statement = null;
	public static ResultSet resultset = null;
	
	public static String DRIVER;
	public static String DATABASE;
	public static String USERNAME;
	public static String PASSWORD;
	//成绩面板设置
	private JFrame gradeFrame;
	private class InnerWindowCloser extends WindowAdapter{//窗口关闭
		public void windowClosing(WindowEvent we) {
			gradeFrame.dispose();
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
	//得到运动员基本信息
	public void getAth(String _obj[][]) throws Exception{
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		
		String query ="select * from athlete";
		resultset = statement.executeQuery(query);
		int i = 0;
		while(resultset.next()) {
			_obj[i][0] = resultset.getString("id");
			_obj[i][1] = resultset.getString("name");
			_obj[i][2] = resultset.getString("sex");
			_obj[i][3] = resultset.getString("age");
			_obj[i][4] = resultset.getString("group_no");
			_obj[i][5] = resultset.getString("item");
			i++;
		}
		statement.close();
		statement = null;
	}
	//计算账号为 athId 的运动员的总分和名次
	public void calculateScore(int i, String _obj[][]) throws Exception{
		String athId = _obj[i][0];
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		
		String query ="update score set final_score = n_score + d_score - p_score where ath_no = '"+athId+"';";
		statement.executeUpdate(query);
		
		query ="select * from score where ath_no = '" + athId + "';";
		resultset = statement.executeQuery(query);
		while(resultset.next()) {
			_obj[i][6] = resultset.getString("n_score");
			_obj[i][7] = resultset.getString("p_score");
			_obj[i][8] = resultset.getString("d_score");
			_obj[i][9] = resultset.getString("final_score");
		}
		statement.close();
		statement = null;
	}
	//成绩面板构造方法
	public SeeGrade()  {
		//读取配置文件
		try {
			readConfig();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//设置JTable的列名
		String[] columnNames =
			{ "账号", "姓名", "性别", "年龄", "组号", "参赛项目", "正常分", "惩罚分", "奖励分", "总分"};  
		int NUM = 30;
		String[][] obj = new String[NUM][10];
		//读取运动员基本信息
		try {
			getAth(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//读取运动员各项成绩和总分
		for(int i=0; i<NUM; i++) {
			try {
				calculateScore(i, obj);
			} catch (Exception e) {				
				e.printStackTrace();
			}
		}
	    //JTable的构造方法  
	    JTable table = new JTable(obj, columnNames); 
	    table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
	    //设置JTable列的宽度和高度
	    TableColumn column = null;  
	    int colunms = table.getColumnCount();  
	    	column = table.getColumnModel().getColumn(0); column.setPreferredWidth(60);
	    	column = table.getColumnModel().getColumn(1); column.setPreferredWidth(60);
	    	column = table.getColumnModel().getColumn(2); column.setPreferredWidth(60);
	    	column = table.getColumnModel().getColumn(3); column.setPreferredWidth(60);
	    	column = table.getColumnModel().getColumn(4); column.setPreferredWidth(60);
	    	column = table.getColumnModel().getColumn(5); column.setPreferredWidth(455);
	    	column = table.getColumnModel().getColumn(6); column.setPreferredWidth(60);
	    	column = table.getColumnModel().getColumn(7); column.setPreferredWidth(60);
	    	column = table.getColumnModel().getColumn(8); column.setPreferredWidth(60);
	    	column = table.getColumnModel().getColumn(9); column.setPreferredWidth(60);

	    	// 设置JTable自动调整列表的状态，此处设置为关闭  
	    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  
	    
	    //用JScrollPane装载JTable，这样超出范围的列就可以通过滚动条来查看  
	    JScrollPane scroll = new JScrollPane(table);  
	    scroll.setSize(300, 200);  

	    gradeFrame = new JFrame("体操赛事管理系统（查看成绩）");
		gradeFrame.setSize(1000,700);
		gradeFrame.setLocation(220,90);
		gradeFrame.setLayout(new BorderLayout());  
		gradeFrame.add("Center", scroll);
		pack(); gradeFrame.setVisible(true);
		gradeFrame.setResizable(false);
		gradeFrame.addWindowListener(new InnerWindowCloser());//窗口关闭
	}  
//	public static void main(String args[]){
//		SeeGrade grade = new SeeGrade();
//	}
}