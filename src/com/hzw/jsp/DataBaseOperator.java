package com.hzw.jsp;
/**
 * ���ݿ������
 * ���ڽ������ݿ����ӵĿ����ϴ�һ��������ݿ����ӳصķ�ʽ���������ݿ�����
 * ���ڱ�ʾ�������ݿ��������С����������õ���ģʽ���������ݿ������
 * ��Ϊ��ʵ�����������ݿ����ֻ����һ�����ݿ�����
 * @author NEWSTART
 *
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.hzw.demo.Student;

public class DataBaseOperator {
	// ���ݿ����ӱ����������������ݿ�����
	Connection conn = null;
	// ���ݿ�·��
	String driver = "com.mysql.jdbc.Driver";
	// ���ݿ��û���
	String uname = "root";
	// ���ݿ�����
	String pwd = "123456";
	String url = "jdbc:mysql://129.204.49.63/Student?characterEncoding=utf-8";
	// ʵ�������������洢Ψһʵ��������ʽ
	static DataBaseOperator instance = null;

	// ���캯����Ϊʵ�ֵ���ģʽ�������캯������Ϊprivate����
	private DataBaseOperator() {
		Init();
	}

	// ��ʼ������
	void Init() {
		// ���ݿ�����
		try {
			Class.forName(driver);
			// ������������ݿ�����
			try {
				conn = DriverManager.getConnection(url, uname, pwd);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// ��øõ���ʵ���ķ���
	public static DataBaseOperator getInstance() {
		// ��������ڸ�ʵ���򴴽�����������instance��
		if (instance == null) {
			instance = new DataBaseOperator();
		}
		// ��������򷵻ظ�ʵ��
		return instance;
	}

	// ���ݿ���뷽��
	public void insert(Student st) throws SQLException {
		// ��studentʵ���л�ȡ��Ϣ
		int id = st.getId();
		String name = st.getName();
		int age = st.getAge();
		String gender = st.getGender();
		String major = st.getMajor();
		int teamNum = st.getTeam();
		// �����������ݿ��SQL���
		String sql = "insert into studentinfo(id,name,teamNum,age,gender,major) values (" + id + ",'" + name + "',"
				+ teamNum + "," + age + ",'" + gender + "','" + major + "');";
		System.out.println(sql);
		// ִ�����ݿ����
		Statement statement = null;
		statement = conn.createStatement();
		statement.executeUpdate(sql);
		if (statement != null) {
			statement.close();
		}
	}

	// ���ݿ�ɾ��
	public void delete(String id) {
		// ִ�����ݿ����
		Statement statement = null;
		try {
			String sql = "delete from studentinfo where id='" + id + "'";
			statement = conn.createStatement();
			statement.executeUpdate(sql);
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ���ݿ���²���
	public void update(Student st) {
		try {
			// ��studentʵ���л�ȡ��Ϣ
			int id = st.getId();
			String name = st.getName();
			int age = st.getAge();
			String gender = st.getGender();
			String major = st.getMajor();
			int teamNum = st.getTeam();
			// �����������ݿ��SQL���
			String sql = "update studentinfo set id=" + id + ",name='" + name + "',teamNum=" + teamNum + "," + "age="
					+ age + ",gender='" + gender + ",major='" + major + "');";
			System.out.println(sql);
			// ִ�����ݿ����
			Statement statement = null;
			statement = conn.createStatement();
			statement.executeUpdate(sql);
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ���ݿ��ѯ����
	public Set<Student> searchStudents(String id, String name, String teamNum) throws SQLException {
		Statement statement = null;
		ResultSet resultSet = null;
		// ���ݲ�ѯ������ѯ
		String sql = "select * from studentinfo";
		statement = conn.createStatement();
		Set<Student> sts = new HashSet<Student>();
		if (id == null)
			id = "";
		if (name == null)
			name = "";
		if (teamNum == null)
			teamNum = "";
		if (id == "")
			sql = sql + " where id like '%'";
		else
			sql = sql + " where id = " + id;
		if (name == "")
			sql = sql + " and name like '%'";
		else
			sql = sql + " and name = '" + name + "'";
		if (teamNum == "")
			sql = sql + " and teamNum like '%'";
		else
			sql = sql + " and teamNum = " + teamNum;
		System.out.println(sql);
		// ִ��SQL
		resultSet = statement.executeQuery(sql);
		// ������ѯ���
		while (resultSet.next()) {
			Student student = new Student();
			student.setId(resultSet.getInt("id"));
			student.setName(resultSet.getString("name"));
			student.setAge(resultSet.getInt("age"));
			student.setGender(resultSet.getString("gender"));
			student.setMajor(resultSet.getString("major"));
			student.setTeam(resultSet.getInt("teamNum"));
			sts.add(student);
		}
		if (resultSet != null) {
			resultSet.close();
		}
		if (statement != null) {
			statement.close();
		}
		return sts;

	}

	// ���ݿ��ѯ֮�༶��ѯ
	public java.util.Map<Integer, String> searchTeams(String teamNum, String baseRoom) {
		try {
			Statement statement = null;
			ResultSet resultSet = null;
			statement = conn.createStatement();
			java.util.Map<Integer, String> teams = new HashMap<Integer, String>();
			if (teamNum == null)
				teamNum = "";
			if (baseRoom == null)
				baseRoom = "";
			if (teamNum == "" && baseRoom == "") {
				resultSet = statement.executeQuery("select * from Teams");
			}
			if (teamNum != "" && baseRoom == "") {
				resultSet = statement.executeQuery("select * from Teams where teamNum=" + teamNum + "");
			}
			if (teamNum == "" && baseRoom != "") {
				resultSet = statement.executeQuery("select * from Teams where baseRoom='" + baseRoom + "'");
			}
			if (teamNum != "" && baseRoom != "") {
				resultSet = statement.executeQuery(
						"select * from Teams where teamNum=" + teamNum + " and baseRoom='" + baseRoom + "'");
			}
			while (resultSet.next()) {
				teams.put(resultSet.getInt("teamNum"), resultSet.getString("baseRoom"));
			}
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			return teams;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

}
