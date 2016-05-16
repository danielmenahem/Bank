import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class BankDAL implements BankDALInterface  {
	
	public static final String DB_URL = "jdbc:mysql://afekabankinstance.ccegtq5u6m6w.eu-west-1.rds.amazonaws.com:3306/bankdb";
	public static final String DB_USERNAME = "bankuser";
	public static final String DB_PASSWORD = "afeka2016";

	/**
	 * Get a connection to database
	 * @return Connection object
	*/
	public static Connection getConnection() throws RuntimeException {
	    try {
	        Class.forName("com.mysql.jdbc.Driver");
	        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
	    } catch (ClassNotFoundException ex) {
	        throw new RuntimeException("Error connecting to the database", ex);
	    } catch (SQLException ex) {
	        throw new RuntimeException("Error connecting to the database", ex);
	    }
	}

	@Override
	public int create(Object object) throws Exception {
	    Connection connection = BankDAL.getConnection();
	    PreparedStatement statement = insertStatement(connection, object);
	    
	    int affectedRows = statement.executeUpdate();
	    if (affectedRows == 0) {
	    	throw new SQLException("writing object failed, no rows affected.");
	    }

	    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	    	if (generatedKeys.next()) {
	    		int id = generatedKeys.getInt(1);
	    		return id;
	    	} else {
	    		throw new SQLException("writing object failed, no ID obtained.");
	    	}
	    }
	}
	
	@Override
	public Object read(Class<?> c, int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Object> readAll(Class<?> c) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int update(Object object) throws Exception {
		// TODO Auto-generated method stub
		return -1;
	}
	
	@Override
	public void delete(Object object) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	private static String dbTableNameForClass(Class<?> c) {
		return c.getSimpleName();
	}
	
	private static PreparedStatement insertStatement(Connection connection, Object object) 
			throws SQLException, UnsupportedClassException {
		// TODO complete

		String tableName = dbTableNameForClass(object.getClass());

		if (tableName == null) {
			throw new UnsupportedClassException("Invalid object. No such DB table.");
		}

    	String statement = "INSERT INTO " + tableName + " ";

    	if (object instanceof CustomerDetails) {
    		statement = statement + "(name) VALUES (?)";
		}

    	PreparedStatement ps = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

    	if (object instanceof CustomerDetails) {
    		CustomerDetails c = (CustomerDetails)object;
        	ps.setString(1, c.getCustomerName());
		}

    	return ps;
	}
}
