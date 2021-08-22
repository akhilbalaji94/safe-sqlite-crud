package inputvalidation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class InputValidator {
	private Connection connection = null;
	private Statement statement;
	private PreparedStatement verifynameprepstatement;
	private PreparedStatement verifyteleprepstatement;
	private PreparedStatement selectallprepstatement;
	private PreparedStatement insertprepstatement;
	private PreparedStatement deletenameprepstatement;
	private PreparedStatement deleteteleprepstatement;

	public InputValidator(String File, int TimeOut) {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + File);
			System.out.println("Connection to SQLite has been established.");

			statement = connection.createStatement();
			statement.setQueryTimeout(TimeOut);
			statement.executeUpdate("create table  if not exists teledir (person_id integer"
				+ " primary key, person text not null, telephone text not null)");

			verifynameprepstatement = connection.prepareStatement("select person,"
					+ "telephone from teledir where person=?");
			verifynameprepstatement.setQueryTimeout(TimeOut);

			verifyteleprepstatement = connection.prepareStatement("select person,"
					+ "telephone from teledir where telephone=?");
			verifyteleprepstatement.setQueryTimeout(TimeOut);

			selectallprepstatement = connection.prepareStatement("select person,"
					+ "telephone from teledir");
			selectallprepstatement.setQueryTimeout(TimeOut);

			insertprepstatement = connection.prepareStatement("insert into teledir(person, "
					+ "telephone) values (?,?)");
			insertprepstatement.setQueryTimeout(TimeOut);

			deletenameprepstatement = connection.prepareStatement("delete from teledir ");

		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

	public void executeUpdate(String s) {

		try {
			statement.executeUpdate(s);

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	public boolean executeVerifyname(String name) {

		try {
			verifynameprepstatement.setString(1, name);
			ResultSet rs = verifynameprepstatement.executeQuery();
			if(rs.next()) 
				return true;
			else
				return false;

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return false;

	}

	public boolean executeVerifytele(String telephone) {

		try {
			verifyteleprepstatement.setString(1, telephone);
			ResultSet rs = verifyteleprepstatement.executeQuery();
			if(rs.next()) 
				return true;
			else
				return false;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return false;

	}
	public void executeSelect() {

		try {
			ResultSet rs = selectallprepstatement.executeQuery();
			while (rs.next()) {
				// read the result set
				ResultSetMetaData rsmd = rs.getMetaData();
				int colCount = rsmd.getColumnCount();

				// System.out.println("colCount:" + colCount);
				String sep = "";
				for (int i = 1; i < colCount + 1; ++i) {
					System.out.print(sep + rs.getString(i));
					sep = ",";
				}
				System.out.println();

			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	public void executeInsert(String name, String telephone) {

		try {
			insertprepstatement.setString(1, name);
			insertprepstatement.setString(2, telephone);
			insertprepstatement.executeUpdate();

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

	public void close() {
		try {
			if (connection != null)
				connection.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	private boolean telephoneMatch(String number) {
		//		System.out.println(number.length());
		String regex = "((\\d{3} \\d{1} \\d{3} \\d{3} \\d{4})|\\d{5}.?\\d*|(\\d{3} \\d{3} \\d{3} \\d{4})|(\\d{3}-\\d{4})|(\\+\\d{0,2} ?\\(\\d{2,3}\\) ?\\d{3}-\\d{4})|(\\d?\\(\\d{3}\\)\\d{3}-\\d{4})|(\\+\\d{2} \\(\\d{2}\\) ))";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(number);

		if (matcher.matches()) {
			System.out.println("Phone Number Valid");
			return true;
		} else {
			System.out.println("Telephone Number provided in invalid format");
			return false;
		}
	}

	private boolean nameMatch(String name) {
		//		System.out.println(name.length());
		String regex = "([a-zA-Z]?\\'?([a-zA-Z]*\\, [a-zA-Z]* [a-zA-Z]*\\.?)|([a-zA-Z]+ [a-zA-Z][a-zA-Z]+)|([a-zA-Z]*, [a-zA-Z]*)|([a-zA-Z]*)|([a-zA-Z]* [a-zA-Z]'[a-zA-Z]*-[a-zA-Z]*))";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(name);

		if (matcher.matches()) {
			System.out.println("Name is Valid");
			return true;
		} else {
			System.out.println("Name provided in invalid format");
			return false;
		}
	}

	public static void main(String[] args) {
		InputValidator t = new InputValidator("sqlitejava.db",30);
		switch(args[0]){
		case "ADD":
			if(args.length == 1)
				System.out.println("Usage: ADD <Person> <Telephone #>");
			else{
				if(t.nameMatch(args[1]) && t.telephoneMatch(args[2])){
					t.executeInsert(args[1],args[2]);
				}
				else{
					System.err.println("Invalid input is provided");
					System.exit(1);
				}
			}
			break;
		case "DEL":
			if(args.length == 1)
				System.out.println("Usage: DEL <Person> or DEL <Telephone #>");
			else{
				if(t.telephoneMatch(args[1]) && t.executeVerifytele(args[1])){
					t.executeUpdate("delete from teledir where"
							+ " telephone=" +"'"+args[1] +"'");
				}
				else if(t.nameMatch(args[1]) && t.executeVerifyname(args[1])){
					t.executeUpdate("delete from teledir where"
							+ " person=" +"'"+args[1] +"'");
				}
				else{
					System.err.println("Invalid input is provided");
					System.exit(1);
				}
			}
			break;

		case "LIST":
			t.executeSelect();
			break;
		}

		t.close();
		System.exit(0);

	}

}
