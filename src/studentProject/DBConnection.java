package studentProject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

// Database Connection : create, drop, select, insert, update, delete
public class DBConnection {
	private Connection connection = null;
	private List<Student> list = new ArrayList<>();
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	// Connect Database
	public void connect() {
		Properties properties = new Properties();
		try {
			InputStream fis = new FileInputStream("C:\\java_test\\studentProject\\src\\studentProject\\db.properties");
			properties.load(fis);
		} catch (IOException e) {
			System.out.println(e.getClass().getName() + e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getClass().getName() + e.getMessage());
		}
		try {
			Class.forName(properties.getProperty("driver"));
			connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"),
					properties.getProperty("password"));
		} catch (ClassNotFoundException e) {
			System.out.println("Class.forName load error " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("connection error " + e.getMessage());
		}
	}

	// Insert Statement
	public int insert(Student student) {
		PreparedStatement ps = null;
		int insertReturnValue = -1;
		String insertQuery = "call procedure_insert_student(?, ?, ?, ?, ?)";
		try {
			ps = connection.prepareStatement(insertQuery);
			ps.setString(1, student.getNo());
			ps.setString(2, student.getName());
			ps.setInt(3, student.getKor());
			ps.setInt(4, student.getEng());
			ps.setInt(5, student.getMath());
			insertReturnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("insert error " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return insertReturnValue;
	}

	// delete record
	public int delete(String deleteNumber) {
		int deleteReturnValue = -1;
		String deleteQuery = "DELETE from student where no = ?";
		try {
			ps = connection.prepareStatement(deleteQuery);
			ps.setString(1, deleteNumber);
			deleteReturnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("delete error " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("resource close error " + e.getMessage());
			}
		}
		return deleteReturnValue;
	}

	// input data from DB
	public List<Student> select() {
		String selectQuery = "select * from student";
		try {
			ps = connection.prepareStatement(selectQuery);
			rs = ps.executeQuery();
			// check to exist data
			if (!(rs != null || rs.isBeforeFirst())) {
				return list;
			}
			while (rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avr = rs.getDouble("avr");
				String grade = rs.getString("grade");
				int rate = rs.getInt("rate");
				list.add(new Student(no, name, kor, eng, math, total, avr, grade, rate));
			}

		} catch (Exception e) {
			System.out.println("select save list error " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("resource close error " + e.getMessage());
			}
		}
		return list;
	}

	public List<Student> selectSearch(String searchName, int type) {
		String selecsearchQuery = "select * from student where";
		try {
			switch (type) {
			case 1:
				selecsearchQuery += " no like ?";
				break;
			case 2:
				selecsearchQuery += " name like ?";
				break;
			default:
				System.out.println("wrongtype error");
				return list;
			}
			ps = connection.prepareStatement(selecsearchQuery);
			ps.setString(1, "%" + searchName + "%");
			rs = ps.executeQuery();
			if (!(rs != null || rs.isBeforeFirst())) {
				return list;
			}
			while (rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avr = rs.getDouble("avr");
				String grade = rs.getString("grade");
				int rate = rs.getInt("rate");
				list.add(new Student(no, name, kor, eng, math, total, avr, grade, rate));
			}

		} catch (Exception e) {
			System.out.println("selectSearch error " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("resource close error " + e.getMessage());
			}
		}
		return list;
	}

	// update statement
	public int update(Student student) {
		int updateReturnValue = -1;
		String updateQuery = "UPDATE student set kor = ?, eng = ?, math = ?, "
				+ " total = ?,  avr = ?,  grade =? where no = ? ";
		try {
			ps = connection.prepareStatement(updateQuery);
			ps.setInt(1, student.getKor());
			ps.setInt(2, student.getEng());
			ps.setInt(3, student.getMath());
			ps.setInt(4, student.getTotal());
			ps.setDouble(5, student.getAvr());
			ps.setString(6, student.getGrade());
			ps.setString(7, student.getNo());
			updateReturnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("update error" + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return updateReturnValue;
	}

	// sort
	public List<Student> selectOrderBy(int sortElement) {
		String selectOrderByQuery = "select * from student order by ";
		try {
			switch (sortElement) {
			case 1:
				selectOrderByQuery += "no asc ";
				break;
			case 2:
				selectOrderByQuery += "name asc ";
				break;
			case 3:
				selectOrderByQuery += "total desc ";
				break;
			default:
				System.out.println("query error");
			}
			ps = connection.prepareStatement(selectOrderByQuery);
			rs = ps.executeQuery();
			// check to exist data
			if (!(rs != null || rs.isBeforeFirst())) {
				return list;
			}
			int rank = 0;
			while (rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avr = rs.getDouble("avr");
				String grade = rs.getString("grade");
				int rate = rs.getInt("rate");
				if (sortElement == 3) {
					rate = ++rank;
				}
				list.add(new Student(no, name, kor, eng, math, total, avr, grade, rate));
			}

		} catch (Exception e) {
			System.out.println("sort error " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("resource close error " + e.getMessage());
			}
		}
		return list;

	}

	// state
	public List<Student> selectMaxMin(int type) {
		String selectMaxMinQuery = "select * from student where total = ";
		;
		try {
			switch (type) {
			case 1:
				selectMaxMinQuery += "(select max(total) from student)";
				break;
			case 2:
				selectMaxMinQuery += "(select min(total) from student)";
				break;
			default:
				System.out.println("state query error");
			}
			ps = connection.prepareStatement(selectMaxMinQuery);
			rs = ps.executeQuery();
			// check to exist data
			if (!(rs != null || rs.isBeforeFirst())) {
				return list;
			}

			while (rs.next()) {
				String no = rs.getString("no");
				String name = rs.getString("name");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avr = rs.getDouble("avr");
				String grade = rs.getString("grade");
				int rate = rs.getInt("rate");
				list.add(new Student(no, name, kor, eng, math, total, avr, grade, rate));
			}

		} catch (Exception e) {
			System.out.println("state error " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("resource close error " + e.getMessage());
			}
		}
		return list;
	}

	// Close Connection
	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println("resource close error " + e.getMessage());
		}
	}

}
