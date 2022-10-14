package mirae;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
	public static Scanner scanner = new Scanner(System.in);
	public static final int INPUT = 1, UPDATE = 2, DELETE = 3, SEARCH = 4, PRINT = 5, SORT = 6, STATE = 7, EXIT = 8;
	private List<Student> list = new ArrayList<>();
	public static void main(String[] args) {

		boolean stop = false;
		System.out.println("�޴�����(1~8)");
		while (!stop) {
			switch (chooseMenu()) {
			case INPUT:
				inputData();
				break;
			case UPDATE:
				updateData();
				break;
			case DELETE:
				deleteData();
				break;
			case SEARCH:
				searchData();
				break;
			case PRINT:
				printData();
				break;
			case SORT:
				sortData();
				break;
			case STATE:
				statsData();
				break;
			case EXIT:
				stop = true;
				break;
			default:
				System.out.println("1~8 �� �����Ͻÿ�.");
				break;
			}
		}
	}

	private static void statsData() {
		List<Student> list = new ArrayList<>();
		try {
			System.out.println("1.�ְ����� 2.�������� >>");
			int type = scanner.nextInt();
			boolean value = checkPattern(String.valueOf(type), 5);
			if (!value)
				return;
			DBConnection dbCxn = new DBConnection();
			dbCxn.connect();
			list = dbCxn.selectMaxMin(type);
			if (list.size() <= 0) {
				System.out.println("�˻��� ������ �����ϴ�.");
				return;
			}
			for (Student student : list) {
				System.out.println(student);
			}
			dbCxn.close();
		} catch (InputMismatchException e) {
			System.out.println(e.getMessage());
			return;
		} catch (Exception e) {
			System.out.println("highest lowest error " + e.getMessage());
		} finally {
			scanner.nextLine();
		}
	}

	public static void sortData() {
		List<Student> list = new ArrayList<>();
		try {
			DBConnection dbCxn = new DBConnection();
			dbCxn.connect();
			System.out.println("���ı��� ���� 1:no, 2:name, 3:total>>");
			int sortElement = scanner.nextInt();
			boolean value = checkPattern(String.valueOf(sortElement), 4);
			if (!value)
				return;

			list = dbCxn.selectOrderBy(sortElement);

			if (list.size() <= 0) {
				System.out.println("doesn't exist");
				return;
			}
			for (Student student : list) {
				System.out.println(student);
			}
			dbCxn.close();
		} catch (Exception e) {
			System.out.println("database sort error " + e.getMessage());
		}
		return;

	}

	public static void updateData() {
		List<Student> list = new ArrayList<>();
		try {
			System.out.println("������ ������ �й� �Է�>>");
			String updateNumber = scanner.nextLine();
			boolean value = checkPattern(updateNumber, 1);
			if (!value) {
				return;
			}
			DBConnection dbCxn = new DBConnection();
			dbCxn.connect();
			list = dbCxn.selectSearch(updateNumber, 1);
			if (list.size() <= 0) {
				System.out.println("�˻��� ������ �����ϴ�.");
				return;
			}
			Student tempStudent = list.get(0);
			System.out.print("���� " + tempStudent.getKor() + ">>");
			int kor = scanner.nextInt();
			value = checkPattern(String.valueOf(kor), 3);
			if (!value)
				return;
			tempStudent.setKor(kor);

			System.out.print("���� " + tempStudent.getEng() + ">>");
			int eng = scanner.nextInt();
			value = checkPattern(String.valueOf(eng), 3);
			if (!value)
				return;
			tempStudent.setEng(eng);

			System.out.print("���� " + tempStudent.getMath() + ">>");
			int math = scanner.nextInt();
			value = checkPattern(String.valueOf(math), 3);
			if (!value)
				return;
			tempStudent.setMath(math);

			tempStudent.calTotal();
			tempStudent.calAvr();
			tempStudent.calGrade();

			int returnUpdateValue = dbCxn.update(tempStudent);
			if (returnUpdateValue == -1) {
				System.out.println("update error");
				return;
			}

			for (Student student : list) {
				System.out.println(student);
			}
			System.out.println(list.toString() + "\n�����Ϸ�");

			dbCxn.close();
		} catch (InputMismatchException e) {
			System.out.println(e.getMessage());
			return;
		} catch (Exception e) {
			System.out.println("update error " + e.getMessage());
		} finally {
			scanner.nextLine();
		}
	}

	public static void searchData() {
		List<Student> list = new ArrayList<>();
		try {
			System.out.println("�˻��� �̸� �Է�>>");
			String searchName = scanner.nextLine();
			boolean value = checkPattern(searchName, 2);
			if (!value) {
				return;
			}
			DBConnection dbCxn = new DBConnection();
			dbCxn.connect();
			list = dbCxn.selectSearch(searchName, 2);
			if (list.size() <= 0) {
				System.out.println("�˻��� ������ �����ϴ�.");
				return;
			}
			for (Student student : list) {
				System.out.println(student);
			}
			dbCxn.close();
		} catch (InputMismatchException e) {
			System.out.println(e.getMessage());
			return;
		} catch (Exception e) {
			System.out.println("search error " + e.getMessage());
		} finally {
			scanner.nextLine();
		}
	}

	public static void printData() {
		List<Student> list = new ArrayList<>();
		try {
			DBConnection dbCxn = new DBConnection();
			dbCxn.connect();
			list = dbCxn.select();
			if (list.size() <= 0) {
				System.out.println("doesn't exist");
				return;
			}
			for (Student student : list) {
				System.out.println(student);
			}
			dbCxn.close();
		} catch (Exception e) {
			System.out.println("print error " + e.getMessage());
		}
		return;
	}

	public static void deleteData() {
		try {

			System.out.print("������ �л������� �й��� �Է�");
			String deleteNumber = scanner.nextLine();
			boolean value = checkPattern(deleteNumber, 1);
			if (!value) {
				return;
			}

			DBConnection dbCxn = new DBConnection();
			dbCxn.connect();
			int id = dbCxn.delete(deleteNumber);
			if (id != -1) {
				System.out.println("id" + id + " delete success");
			}
			if (id == 0) {
				System.out.println("������ ��ȣ�� �������� �ʽ��ϴ�.");
			} 
			dbCxn.close();

		} catch (InputMismatchException e) {
			System.out.println(e.getMessage());
			return;
		} catch (Exception e) {
			System.out.println("delete error " + e.getMessage());
		} finally {
			scanner.nextLine();
		}
	}

	public static void inputData() {
		scanner.nextLine();
		String pattern = null;
		boolean regex = false;

		try {
			System.out.println("�г�(1~3:01, 02, 03)��(1~9:01~09)��ȣ(1~30:01~60)>>");
			String no = scanner.nextLine();
			boolean value = checkPattern(no, 1);
			if (!value) {
				return;
			}

			System.out.println("�̸��Է�>>");
			String name = scanner.nextLine();
			value = checkPattern(name, 2);
			if (!value) {
				return;
			}

			System.out.println("���������Է�>>");
			int kor = scanner.nextInt();
			value = checkPattern(String.valueOf(kor), 3);
			if (!value) {
				return;
			}

			System.out.println("���������Է�>>");
			int eng = scanner.nextInt();
			value = checkPattern(String.valueOf(eng), 3);
			if (!value) {
				return;
			}
			System.out.println("���������Է�>>");
			int math = scanner.nextInt();
			value = checkPattern(String.valueOf(math), 3);
			if (!value) {
				return;
			}

			Student student = new Student(no, name, kor, eng, math);
//		
			DBConnection dbCxn = new DBConnection();
			dbCxn.connect();
			int id = dbCxn.insert(student);
			if (id != -1) {
				System.out.println("id" + id + " insert success");
			} else {
				System.out.println("id" + id + " insert fail");
			}
			dbCxn.close();

		} catch (InputMismatchException e) {
			System.out.println(e.getStackTrace());
			return;
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		} finally {
			scanner.nextLine();
		}
	}

	public static int chooseMenu() {
		int number = -1;
		try {
			System.out.println("1.�Է� | 2. ���� | 3. ���� | 4. �˻� | 5. ��� | 6. ���� | 7. ��� | 8. ����");
			number = scanner.nextInt();
			String pattern = "^[1-8]*$";
			boolean regex = Pattern.matches(pattern, String.valueOf(number));
		} catch (InputMismatchException e) {
			System.out.println(e.getMessage());
			number = -1;
		} finally {
			scanner.nextLine();
		}
		return number;
	}

	public static boolean checkPattern(String data, int patternType) {
		String pattern = null;
		boolean regex = false;
		String message = null;
		switch (patternType) {
		case 1:
			pattern = "^0[1-3]0[1-9][0-6][0-9]$";
			message = "�й��� ��Ȯ�ϰ� �Է��ϼ���";
			break;
		case 2:
			pattern = "^[��-�R]{2,4}$";
			message = "�̸��� ��Ȯ�ϰ� �Է��ϼ���";
			break;
		case 3:
			pattern = "^[0-9]{1,3}$";
			message = "������ ��Ȯ�ϰ� �Է��ϼ���";
			break;
		case 4:
			pattern = "^[1-3]$";
			message = "��Ȯ�ϰ� �Է��ϼ���";
			break;
		case 5:
			pattern = "^[1-2]$";
			message = "��Ȯ�ϰ� �Է��ϼ���";
			break;
		}

		regex = Pattern.matches(pattern, String.valueOf(data));
		if (patternType == 3) {
			if (Integer.parseInt(data) < 0 || Integer.parseInt(data) > 100) {
				System.out.println(message);
				return false;
			}
		} else {
			if (!regex) {
				System.out.println(message);
				return false;
			}
		}
		return regex;
	}
}

