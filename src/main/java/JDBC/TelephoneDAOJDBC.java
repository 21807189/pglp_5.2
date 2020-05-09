package JDBC;

import java.sql.Connection;

import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.derby.shared.common.error.DerbySQLIntegrityConstraintViolationException;

import DAO.DAO;
import mou.telephone;

public class TelephoneDAOJDBC extends DAO<telephone> {
    
    private Connection connection;

    public TelephoneDAOJDBC() {
        String dbUrl = "jdbc:derby:donneesPourDB\\jdbcDB;create=true";
        try {
            connection = DriverManager.getConnection(dbUrl);
        } catch (SQLException e) {
            connection = null;
            e.printStackTrace();
        }
    }

    private static boolean tableExists(final String name, final Connection conn)
            throws SQLException {
        DatabaseMetaData dbmd = conn.getMetaData();
        try (ResultSet rs =
                dbmd.getTables(null, null, name.toUpperCase(), null)) {
            if (rs.next()) {
                return true;
            }
        }
        return false;
    }

    static void createTable(final Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            if (!tableExists("telephone", conn)) {
                stmt.execute("Create table telephone " + "(id int primary key, "
                        + "numero varchar(30) unique not null,"
                        + "information varchar(30))");
            }
        }
    }

    static void insert(final telephone obj, final Connection conn)
            throws SQLException {
        try (PreparedStatement insertTelephone = conn
                .prepareStatement("insert into telephone values (?, ?, ?)")) {
            insertTelephone.setInt(1, obj.getId());
            insertTelephone.setString(2, obj.getNumero());
            insertTelephone.setString(3, obj.getInformation());
            insertTelephone.execute();
        }
    }

    
    @Override
    public telephone create(final telephone obj) {
        try {
            createTable(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        
        telephone created = null;

        try {
            connection.setAutoCommit(false);
            insert(obj, connection);
            connection.commit();
            created = obj;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            try {
                connection.rollback();
                System.err.println("L'insertion du telephone " + obj.getId() + " a échoué.");
            } catch (SQLException e1) {
                System.err.println(e1.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return created;

    }

    static ArrayList<telephone> findAll(final ArrayList<Integer> id,
            final Connection conn) throws SQLException {

        ArrayList<telephone> telephones = new ArrayList<telephone>();

        try (PreparedStatement selectTelephone =
                conn.prepareStatement("SELECT * FROM telephone WHERE id = ?")) {
            for (int i : id) {
                try {
                	 selectTelephone.setInt(1, i);
                    try (ResultSet rs = selectTelephone.executeQuery()) {
                        if (rs.next()) {
                            telephones.add(new telephone(rs.getInt("id"),
                                    rs.getString("numero"),
                                    rs.getString("information")));
                        }
                    }
                } catch (DerbySQLIntegrityConstraintViolationException e) {
                	System.err.println(e.getMessage());
                }
            }
        }
        return telephones;
    }

    @Override
    public telephone find(final String id) {
        try (PreparedStatement selectTelephone = connection
                .prepareStatement("SELECT * FROM telephone WHERE id = ?")) {
            selectTelephone.setInt(1, Integer.parseInt(id));
        	
            try (ResultSet rs = selectTelephone.executeQuery()) {
                if (rs != null && rs.next()) {
                    telephone t = new telephone(rs.getInt("id"),
                            rs.getString("numero"),
                            rs.getString("information"));
                    return t;
                } else {
                    System.err.println("Aucun telephone avec id: " + id);
                }
            }
        } catch (SQLException e1) {
            System.err.println(e1.getMessage());
        }
        return null;
    }
    static void updateAll(final List<telephone> list, final Connection conn)
            throws SQLException {

        try (PreparedStatement selectTelephone =
                conn.prepareStatement("SELECT * FROM telephone WHERE id = ?")) {
            try (PreparedStatement updateTelephone =
                    conn.prepareStatement(
                            "Update telephone SET numero = ?,"
                                    + "information = ? "
                                    + "WHERE id = ?")) {
                for (telephone t : list) {
                    selectTelephone.setInt(1, t.getId());
                    try (ResultSet rs = selectTelephone.executeQuery()) {
                        if (!rs.next()) {
                            insert(t, conn);
                        } else {
                            updateTelephone.setString(1, t.getNumero());
                            updateTelephone.setString(2, t.getInformation());
                            updateTelephone.setInt(3, t.getId());
                            updateTelephone.execute();
                        }
                    }
               }
         }
            
        }
    }

    
    @Override
    public telephone update(final telephone obj) {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        telephone updated = null;
        try (PreparedStatement updateTelephone =
                connection.prepareStatement("Update telephone SET numero = ?,"
                        + "information = ? WHERE id = ?")) {
            updateTelephone.setString(1, obj.getNumero());
            updateTelephone.setString(2, obj.getInformation());
            updateTelephone.setInt(3, obj.getId());
            updateTelephone.execute();
            connection.commit();
            updated = obj;
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            try {
                connection.rollback();
                System.err.println("La mise à jour du telephone " + obj.getId() + " à échouée.");
            } catch (SQLException e1) {
                System.err.println(e1.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return updated;
    }

    @Override
    public void delete(final telephone obj) {
        try {
            connection.setAutoCommit(false);
            if (tableExists("possede", connection)) {
                try (PreparedStatement deletePossede =
                        connection.prepareStatement(
                                "DELETE from possede where id_telephone = ?")) {
                    deletePossede.setInt(1, obj.getId());
                    deletePossede.execute();
                    System.out.println("telephone " + obj.getId()
                    + " supprimé.");
                }
            }
            try (PreparedStatement deleteTelephone = connection
                    .prepareStatement("Delete from telephone WHERE id = ?")) {
                deleteTelephone.setInt(1, obj.getId());
                deleteTelephone.execute();
            }
            connection.commit();
            
            System.out.println("Telephone " + obj.getId() + " supprimé.");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            try {
                connection.rollback();
                System.err.println("suppression du telephone " + obj.getId() + " a échouée.");
            } catch (SQLException e1) {
                System.err.println(e1.getMessage());
            }
        } 
    }
}
