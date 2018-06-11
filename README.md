## WeSport

### 体操赛事管理系统，一个管理体操比赛的信息系统。
该系统能够为整个体操赛事提供人员信息存取、赛事编排和成绩公布等服务。

### 1.系统设计
- 实现方案：基于 **Java语言** 和 **MySql** 数据库
- 文件说明：
	- WeSport.jar 体操赛事管理系统应用程序
	- src
		- WeSport.java 系统入口模块
		- SignUp.java 注册模块
		- Leader.java 队长模块
		- AddAthlete.java 增加运动员模块
		- Referee.java 裁判模块
		- EditDialog.java 修改裁判信息模块
		- Manager.java 管理员模块
		- SeeGrade.java 查看成绩模块
		- AboutUs.java 关于模块
	- wesport_lib
		- jyaml-1.3.jar
		- mysql-connector-java-8.0.11.jar
	- images 系统图例
	- _config.yml 配置文件
	- README.md 

### 2.系统说明
- 该系统的使用者：
   - 运动员代表队队长
   - 体操赛事裁判
   - 系统管理员
- 功能：
    - 运动员代表队队长登录系统输入本队运动员信息及参赛项目
    - 系统管理员登录系统安排比赛场次
    - 体操赛事裁判登录系统对运动员表现进行评分
- 赛事安排：按照年龄和性别分成六个比赛组
    - 7-8岁男子组
    - 7-8岁女子组
    - 9-10岁男子组
    - 9-10岁女子组
    - 11-12岁男子组
    - 11-12岁女子组
- 账号说明: 
    - 队长——10xx 
    - 队医——20xx  
    - 教练——30xx
    - 运动员——40xx 
    - 裁判——50xx 
    - 管理员——60xx
- 数据库设计
	- User 表
	
		| id   | name | phone | statu | password | group_no |
		| :--: | :--: | :---: | :---: | :------: | :------: |
		| 101| Tom  | 101   | 队长   | 101 | 1|
		| ...|...|...|...|...|...|
	- Athlete 表
		
		| id   | name | sex | age | group_no | item |
		| :--: | :--: | :---: | :---: | :------: | :------: |
		| 401| 小明  | 男   | 9   | 1 | 单杠-吊环-跳马-鞍马-蹦床-|
		| ...|...|...|...|...|...|
	- Arrange 表
		
		| ath_no | contest | 
		| :--: | :--: | 
		| 403| Boy7_8  |
		| ...|...|
	- Score 表
		
		| ath_no   | n_score | p_score | d_score | final_score |
		| :--: | :--: | :---: | :---: | :------: | 
		| 401| 9  | 1   | 2   | 10 | 
		| ...|...|...|...|...|
		
### 3.使用手册
- 开发人员
	- 如何获取
		- 下载软件包，配置相应的数据库文件即可使用
	- 配置文件
		- _config.yml: 数据库的相关配置。
		- _about.yml: 系统的相关信息和说明。
		- 
- 参赛队长
	- 打开系统
	![avatar](/images/start.png)
	<br>输入账号和密码即可登录。如果你是新用户请点击注册按钮注册你的信息。
	- 帮助
	![avatar](/images/help.png)
	<br>左上角Help-About菜单项为本系统的相关信息及说明。
	- 队长登录
	![avatar](/images/leader.png)
	<br>如图，队长可以编辑队伍信息和增删参赛运动员，以及查看最终成绩。
	- 增加运动员
	![avatar](/images/addathlete.png)
	- 查看成绩
	![avatar](/images/seegrade.png)
	
- 赛事裁判
	- 裁判登录
	![avatar](/images/referee.png)
	<br>如图，裁判可以修改自己的信息，可以为参赛运动员打分并提交，以及查看最终成绩。
	- 修改信息<br>
	![avatar](/images/refereechange.png)
	
- 系统管理员
	- 管理员登录
	![avatar](/images/manager.png)
	<br>如图，管理员可以编排比赛，共分六个比赛组，也可以查看最终成绩。
 	- 赛事编排
 	![avatar](/images/managerathlete.png)
 	
### 4.关于作者
Copyright © 2018 - 2018 CoolR.All Rights Reserved.