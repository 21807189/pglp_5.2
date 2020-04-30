package JDBC;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public abstract class Connection_BD_DAO  <T>{
	
	  public static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	  public static final String db_URL = "jdbc:derby:MyBD; create = true"; 
	  
	  Connection connect;   
	  
	 public  Connection_BD_DAO() {
		 try {  
			 this.connect = DriverManager.getConnection(db_URL); 
			 
			 if (this.connect !=null) {
				 
				 System.out.println("Connexion établie");
			 }
		 }catch(SQLException e) {
			 System.out.println("La connexion a échouée");
	  }
		 	  
    }

public abstract T create(T obj) throws IOException, SQLException;

public abstract T find(int id) throws IOException, ClassNotFoundException, SQLException;

public abstract T update(T obj) throws IOException, SQLException;

public abstract void delete(T obj)throws SQLException;

public Object deserialize(final byte[] bytes) throws ClassNotFoundException,IOException {
	
    ByteArrayInputStream b = new ByteArrayInputStream(bytes);
    ObjectInputStream o = new ObjectInputStream(b);
    return o.readObject();
}

public byte[] serialize(final Object obj) throws IOException {
	
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }
 public Connection getConnect() {
        return connect;
    }
}
