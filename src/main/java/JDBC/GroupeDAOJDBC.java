package JDBC;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

import org.apache.derby.shared.common.error.DerbySQLIntegrityConstraintViolationException;

import DAO.DAO;
import mou.*;

public class GroupeDAOJDBC extends DAO<Groupe> {
    
    private Connection connection;

    public GroupeDAOJDBC() {
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

    public static void createTables(final Connection conn) throws SQLException {
        TelephoneDAOJDBC.createTable(conn);
        PersonnelDAOJDBC.createTables(conn);
        try (Statement stmt = conn.createStatement()) {
            if (!tableExists("groupe", conn)) {
                stmt.executeUpdate("Create table groupe ("
                        + "id int primary key, " + "nom varchar(30) not null)");
            }

            if (!tableExists("appartenir", conn)) {
                stmt.executeUpdate("Create table appartenir ("
                        + "id_personnel int not null, "
                        + "id_groupe int not null, "
                        + "primary key (id_personnel, id_groupe), "
                        + "foreign key (id_personnel)"
                        + " references personnel(id), "
                        + "foreign key (id_groupe) references groupe(id))");
            }

            if (!tableExists("contenir", conn)) {
                stmt.executeUpdate("Create table contenir ("
                        + "id_groupe_contenu int not null, "
                        + "id_groupe_contenant int not null, "
                        + "primary key (id_groupe_contenu,"
                        + " id_groupe_contenant), "
                        + "foreign key (id_groupe_contenu)"
                        + " references groupe(id), "
                        + "foreign key (id_groupe_contenant)"
                        + " references groupe(id))");
            }
        }
    }

    static void insert(final Groupe obj, final Connection conn)
            throws SQLException {
        String insert = "INSERT into groupe values (?, ?)";
        try (PreparedStatement prepare = conn.prepareStatement(insert)) {
            prepare.setInt(1, obj.getId());
            prepare.setString(2, obj.getNom());
            prepare.execute();
        }

        try (PreparedStatement selectPersonnel =
                conn.prepareStatement("SELECT * FROM personnel WHERE id = ?");
                PreparedStatement insertAppartenir = conn.prepareStatement(
                        "insert into appartenir values (?, ?)");
                PreparedStatement selectGroupe = conn
                        .prepareStatement("SELECT * FROM groupe WHERE id = ?");
                PreparedStatement insertContenir = conn.prepareStatement(
                        "insert into contenir values (?, ?)");) {
            insertAppartenir.setInt(2, obj.getId());
            insertContenir.setInt(2, obj.getId());

           
                }
            }
        
    }

   
}
