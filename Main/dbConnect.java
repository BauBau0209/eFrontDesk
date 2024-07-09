
package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnect {
    public static Connection Connect(){        
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/efrontdesk", "root", "020904");
            return con;
        } catch (SQLException e){
            System.out.print(e);
            return null;
        }
    }
}
