package rpbot.derbydatabase;

import java.sql.Date;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import rpbot.sheetdata.Item;
import rpbot.sheetdata.SheetData;

/**
 *
 * @author ivanskodje
 */
public class DerbyDatabase implements Database
{

    // private static final String DB_URL = "jdbc:derby:database;create=true";
    // private static final String DB_URL = "jdbc:derby:/home/ivanskodje/.netbeans-derby/TimeHoursDB;create=true;user=derby;password=derby";
    // Derby Vars
    private static String database_url;
    private static Connection connection = null;
    private static Statement statement = null;
    private static PreparedStatement preparedStatement = null;

    private String username = null;
    private String password = null;
    private String repository = null;

    /*
	In order to work a Derby database, we need a repository, user and password.
     */
    public DerbyDatabase(String repository, String username, String password)
    {
        // Put database url together
        database_url = "jdbc:derby:" + repository + ";create=true;user=" + username + ";password=" + password;

        // Store username and password
        // TODO: Check if we should and how to store strings securely
        this.repository = repository;
        this.username = username;
        this.password = password;
    }

    /* 
	Establishes connection with Derby DB
     */
    @Override
    public void connect()
    {
        try
        {
            // Instance driver and setup connection
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            connection = DriverManager.getConnection(database_url, username, password);

            // TODO: Allow user on Repo creation to decide whether or not a password will be used
            // Enable user authorization
            // setAuthorization(true)
            // Disable user authorization
            setAuthorization(false);

            // Setup database tables (if they dont exist)
            setupTables();
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException ex)
        {
            System.err.print("Unable to setup connection with database.\n" + ex.getMessage());
        }
    }

    /*
	Setup database tables
     */
    private void setupTables()
    {
        setupItems();
    }

    /*
        Setup the Sheet Table
     */
    private void setupItems()
    {
        String TABLE_NAME = "ITEMS";
        String QUERY = "CREATE TABLE " + TABLE_NAME + "("
                + "id INTEGER PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n"
                + "userId VARCHAR(4096) NOT NULL,\n"
                + "keyValue VARCHAR(4096) NOT NULL,\n"
                + "value VARCHAR(4096),\n"
                + "dateCreated DATE default CURRENT_DATE"
                + ")";
        try
        {
            statement = connection.createStatement();
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet tables = databaseMetaData.getTables(null, null, TABLE_NAME.toUpperCase(), null);

            // If table does not exist, create it
            if (!tables.next())
            {
                statement.execute(QUERY);
            }
        }
        catch (SQLException ex)
        {
            System.err.print("Unable to setup the item table.\n" + ex.getMessage());
        }
    }

    /*
	Execute Query and return a ResultSet
     */
    @Override
    public ResultSet executeQuery(String query)
    {
        ResultSet resultSet = null;

        try
        {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        }
        catch (SQLException ex)
        {
            System.err.println("Unable to execute query.\n" + ex.getMessage());
        }

        return resultSet;
    }

    /*
	Execute an action and return a boolean result
     */
    @Override
    public boolean executeAction(String query)
    {
        try
        {
            statement = connection.createStatement();
            statement.execute(query);
            return true;
        }
        catch (SQLException ex)
        {
            System.err.println("Unable to execute query.\n" + ex.getMessage());
            return false;
        }
    }
   
    
    /*
	Shared method for enabling or disabling user authorization for the database
     */
    @Override
    public void setAuthorization(Boolean is_enabled)
    {
        try
        {
            // Create statement
            statement = connection.createStatement();

            // True: Enable, False: Disable - requireAuthentication
            statement.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.connection.requireAuthentication', '" + is_enabled + "')");

            // True: Add, False: Remove - password requirements for user
            String use_pass = is_enabled ? "'" + password + "'" : "null";
            statement.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.user." + username + "', " + use_pass + ")");

        }
        catch (SQLException ex)
        {
            System.err.println("DatabaseHandler.java -> setAuthorization(" + is_enabled + ") \n" + ex.getMessage());
        }
    }

    @Override
    public boolean insertItem(Item item)
    {
        try
        {
            String query = "INSERT INTO ITEMS (userId, keyValue, value) values (?, ?, ?)";
            preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, item.getUserId());
            preparedStatement.setString(2, item.getKey());
            preparedStatement.setString(3, item.getValue());
            preparedStatement.executeUpdate();
            
            System.out.println("SUCCESSFULLY INSERTED ITEM INTO DB!");
            return true;
        }
        catch (SQLException ex)
        {
            String sqlState = ex.getSQLState();

            // Duplicate Name
            // Error Docu: https://db.apache.org/derby/docs/10.1/ref/rrefexcept71493.html
            if (sqlState.equals("23505"))
            {
                System.err.println( "Item already exist.");
            }
            else
            {
                System.err.println("Unable to execute query.\n" + ex.getMessage());
            }
        }
        
        return false;
    }
    
    
    @Override
    public boolean updateItem(Item item)
    {
        if(deleteItem(item))
        {
            return insertItem(item);
        }

        return false;
    }
    
    
    @Override
    public boolean deleteItem(Item item)
    {
        try
        {
            String query = "DELETE FROM ITEMS WHERE userId = ? AND keyValue = ?";
            preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, item.getUserId());
            preparedStatement.setString(2, item.getKey());
            preparedStatement.executeUpdate();
            return true;
        }
        catch (SQLException ex)
        {
            String sqlState = ex.getSQLState();

            // Duplicate Name
            // Error Docu: https://db.apache.org/derby/docs/10.1/ref/rrefexcept71493.html
            if (sqlState.equals("23505"))
            {
                System.err.println( "Item already exist.");
            }
            else
            {
                System.err.println("Unable to execute DELETE query.\n" + ex.getMessage());
            }
           
        }
        return false;
    }
}
