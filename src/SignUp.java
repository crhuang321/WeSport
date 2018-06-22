/*SignUp.java
 * SignUp类：用户注册
 * 1.界面：用户注册所需的相应信息输入框、取消和确认按钮
 * 2.功能：连接数据库，将新用户信息写入数据库
 */
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.*;
import org.ho.yaml.Yaml;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
class SignUp extends Dialog implements ActionListener{
	//用于数据库连接
	public static Connection connection = null;
	public static Statement statement = null;
	public static ResultSet resultset = null;
	
	public static String DRIVER;
	public static String DATABASE;
	public static String USERNAME;
	public static String PASSWORD;
	//注册用户所需，每次注册重新赋值
	public String UName = new String();
	public String UId = new String();
	public String UPhone = new String();
	public String UPassword = new String();
	public String statu = new String();
	public String UGroup = new String();
	
	//注册面板
	private JFrame signUpFrame;
    public JRadioButton statu1=new JRadioButton("队长");  
    public JRadioButton statu2=new JRadioButton("裁判"); 
    public JRadioButton statu3=new JRadioButton("管理员"); 
    public JRadioButton statu4=new JRadioButton("队医"); 
    public JRadioButton statu5=new JRadioButton("教练"); 
	private JButton ok=new JButton("确定");
	private JButton cancel=new JButton("取消");
	private JLabel labelName=new JLabel("姓名",JLabel.LEFT);
	private JLabel labelTelephone=new JLabel("电话",JLabel.LEFT);
	private JLabel labelId=new JLabel("账号",JLabel.LEFT);
	private JLabel labelPassword=new JLabel("密码",JLabel.LEFT);
	private JLabel labelGroup=new JLabel("队号",JLabel.LEFT);
	JTextField textName=new JTextField(20);
	JTextField textId=new JTextField(20);
	JTextField textPassword=new JTextField(20);
	JTextField textPhone=new JTextField(20);
	JTextField textGroup=new JTextField(20);
	
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
	//构造方法
	public SignUp(Frame parent,String title) {
		super(parent,title,true);
		//读取配置文件
		try {
			readConfig();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//身份状态单选
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(statu1);		statu1.addActionListener(this); 
		buttonGroup.add(statu2);		statu2.addActionListener(this); 
		buttonGroup.add(statu4);		statu4.addActionListener(this); 
		buttonGroup.add(statu5);		statu5.addActionListener(this); 
		buttonGroup.add(statu3);		statu3.addActionListener(this); 
		
		Panel pStatu = new Panel();
		pStatu.setLayout(new FlowLayout());
		pStatu.add(statu1); 
		pStatu.add(statu2); 
		pStatu.add(statu4); 
		pStatu.add(statu5); 
		pStatu.add(statu3);
		
		Panel pName=new Panel();
		pName.setLayout(new FlowLayout());
		pName.add(labelName);
		pName.add(textName);

		Panel pId=new Panel();
		pId.add(labelId);
		pId.add(textId);
		
		Panel pPassword=new Panel();
		pPassword.add(labelPassword);
		pPassword.add(textPassword);
		
		Panel pGroup=new Panel();
		pGroup.add(labelGroup);
		pGroup.add(textGroup);
		textGroup.setText("0");

		Panel pTelephone=new Panel();
		pTelephone.add(labelTelephone);
		pTelephone.add(textPhone);
		
		Panel pUserInfo=new Panel();
		pUserInfo.setLayout(new GridLayout(5,1));
		pUserInfo.add(pName);
		pUserInfo.add(pTelephone);
		pUserInfo.add(pId);
		pUserInfo.add(pPassword);
		pUserInfo.add(pGroup);
		
		Panel pButton=new Panel();
		pButton.setLayout(new FlowLayout());
		pButton.add(ok);		ok.addActionListener(this);
		pButton.add(cancel);		cancel.addActionListener(this);
		
		signUpFrame = new JFrame("新用户注册");
		signUpFrame.setSize(400,350);
		signUpFrame.setLocation(520,250);
		signUpFrame.setLayout(new BorderLayout());
		signUpFrame.add("North", pStatu);
		signUpFrame.add("Center",pUserInfo);
		signUpFrame.add("South",pButton);
		pack(); 	signUpFrame.setVisible(true);
		signUpFrame.setResizable(false);
		signUpFrame.addWindowListener(new WindowCloser());
	}
	//新用户注册：写数据库
	public void signUp(String query) throws Exception{
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		int Res = statement.executeUpdate(query);
		statement.close();
		statement = null;
	}
	//读数据库：查找Id是否已经存在
	public boolean findId(String newId) throws Exception {
		Class.forName(DRIVER);
		String url =DATABASE;
		connection = DriverManager.getConnection(url,USERNAME,PASSWORD);
		statement = (Statement)connection.createStatement();
		String query ="select id from user";
		resultset = statement.executeQuery(query);
		while(resultset.next()) {
			if(resultset.getString("id").equals(newId))
				return true;
		} 
		statement.close();
		statement = null;
		return false;
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==statu1) {
			statu = "队长";
		}else if(e.getSource()==statu2) {
			statu = "裁判";
		}else if(e.getSource()==statu3) {
			statu = "管理员";
		}else if(e.getSource()==statu4) {
			statu = "队医";
		}else if(e.getSource()==statu5) {
			statu = "教练";
		}else if(e.getSource()==ok) {
			UName = textName.getText();
			UId = textId.getText();
			UPhone = textPhone.getText();
			UPassword = textPassword.getText();
			UGroup = textGroup.getText();
			try {
				if(UId.equals("")) {//用户没有输入账号号，账号号为主键不能为空
					JOptionPane.showMessageDialog(null, "你必须输入账号!", "", JOptionPane.ERROR_MESSAGE);
				}else {
					String query = "INSERT INTO `wesport`.`user` (`id`, `name`, `phone`, `statu`, `password`, `group_no`)"
							+ "VALUES ('" + UId + "','"
							+ UName + "','"
							+ UPhone + "','"
							+ statu + "','"
							+ UPassword + "','"
							+ UGroup + "');";
					try {
						if(findId(UId)){//账号号不能重复
							JOptionPane.showMessageDialog(null, "该账号已存在!", "", JOptionPane.ERROR_MESSAGE);
						}else {
							signUp(query);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					signUpFrame.setVisible(false);	
				}
			}catch (Exception ex) {
				ex.printStackTrace();
			}
		}else if(e.getSource()==cancel) {
			signUpFrame.setVisible(false);
		}
	}
	private class WindowCloser extends WindowAdapter{
		public void WindowClosing(WindowEvent e) {
			SignUp.this.setVisible(false);
		}
	}
}