package JDBC;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import DAO.DAO;
import mou.*;


public class AnnuaireDAOJDBC extends DAO<Annuaire> {
   
    private Connection connection;

    public AnnuaireDAOJDBC() {
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
        ResultSet rs = dbmd.getTables(null, null, name.toUpperCase(), null);
        if (rs.next()) {
            return true;
        }
        return false;
    }
    
    public static void createTables(final Connection conn) throws SQLException {
        TelephoneDAOJDBC.createTable(conn);
        PersonnelDAOJDBC.createTables(conn);
        GroupeDAOJDBC.createTables(conn);
        try (Statement stmt = conn.createStatement()) {
            if (!tableExists("annuaire", conn)) {
                stmt.executeUpdate("Create table annuaire ("
                        + "id int primary key, " + "racine_personnel int, "
                        + "racine_groupe int,"
                        + "check ((racine_personnel is null and"
                        + " racine_groupe is not null) or"
                        + "(racine_personnel is not null and"
                        + " racine_groupe is null)),"
                        + "foreign key (racine_personnel)"
                        + " references personnel(id),"
                        + "foreign key (racine_groupe)"
                        + " references groupe(id))");
            }
        }
    }

    @Override
    public Annuaire create(final Annuaire obj) {
        try {
            createTables(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        Annuaire created = null;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement insertAnnuaire = connection.prepareStatement(
                    "INSERT into annuaire values " + "(?, ?, ?)")) {
                Composant racine = obj.getRacine();
                if (racine instanceof Personnel) {
                    Personnel p = (Personnel) racine;
                    try (PreparedStatement selectPersonnel =
                            connection.prepareStatement(
                                    "SELECT * FROM personnel WHERE id = ?")) {
                        selectPersonnel.setInt(1, p.getId());
                        try (ResultSet rs = selectPersonnel.executeQuery()) {
                            if (!rs.next()) {
                                PersonnelDAOJDBC.insert(p, connection);
                            } else {
                                System.err.println("Personnel " + p.getId()
                                + " existe déjà");
                            }
                        }
                    }
                    insertAnnuaire.setNull(3, Types.INTEGER);
                    insertAnnuaire.setInt(2, p.getId());
                } else if (racine instanceof Groupe) {
                    Groupe g = (Groupe) racine;
                    try (PreparedStatement selectGroupe =
                            connection.prepareStatement(
                                    "SELECT * FROM groupe WHERE id = ?")) {
                        selectGroupe.setInt(1, g.getId());
                        try (ResultSet rs = selectGroupe.executeQuery()) {
                            if (!rs.next()) {
                                GroupeDAOJDBC.insert(g, connection);
                            } else {
                                System.err.println("Groupe " + g.getId()
                                + " existe déjà.");
                            }
                        }
                    }
                }
                
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            try {
                connection.rollback();
                System.err.println("L'insertion de l'annuaire " + obj.getId() + " a échouée.");
            } catch (SQLException e1) {
                System.err.println(e1.getMessage());
            }
        }
        return created;
    }

    @Override
    public Annuaire find(final String id) {
        Annuaire a = null;
        try (PreparedStatement selectAnnuaire = connection
                .prepareStatement("SELECT * FROM annuaire WHERE id = ?")) {
            selectAnnuaire.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = selectAnnuaire.executeQuery()) {
                if (rs.next()) {
                    int idRacine = rs.getInt("racine_personnel");
                    if (!rs.wasNull()) {
                        Personnel p =
                                PersonnelDAOJDBC.read(idRacine, connection);
                        a = new Annuaire(rs.getInt("id"), p);
                    } else {
                        idRacine = rs.getInt("racine_groupe");
                        if (!rs.wasNull()) {
                            Groupe g = GroupeDAOJDBC.read(idRacine, connection);
                            a = new Annuaire(rs.getInt("id"), g);
                        }
                    }
                } else {
                    System.err.println("Aucun annuaire avec l'identifiant" + id);
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return a;
    }

    @Override
    public Annuaire update(final Annuaire obj) {
        Annuaire updated = null;
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement updateAnnuaire = connection.prepareStatement(
                    "UPDATE annuaire SET racine_groupe = ?, racine_personnel = ? "
                            + "WHERE id = ?")) {
                updateAnnuaire.setInt(3, obj.getId());
                Composant racine = obj.getRacine();
                if (racine instanceof Personnel) {
                    Personnel p = (Personnel) racine;
                    PersonnelDAOJDBC.modify(p, connection, true);
                    updateAnnuaire.setNull(1, Types.INTEGER);
                    updateAnnuaire.setInt(2, p.getId());
                } else if (racine instanceof Groupe) {
                    Groupe g = (Groupe) racine;
                    GroupeDAOJDBC.modify(g, connection, true);
                    updateAnnuaire.setNull(2, Types.INTEGER);
                    updateAnnuaire.setInt(1, g.getId());
                }
                updateAnnuaire.execute();
                connection.commit();
                updated = obj;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            try {
                connection.rollback();
                System.err.println("La mise à jour de l'annuaire" + obj.getId() + " a échouée.");
            } catch (SQLException e1) {
                System.err.println(e1.getMessage());
            }
        } 
        return updated;
    }

    @Override
    public void delete(final Annuaire obj) {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement delete = connection
                    .prepareStatement("DELETE FROM annuaire WHERE id = ?")) {
                delete.setInt(1, obj.getId());
                delete.execute();
                connection.commit();
                System.out.println("Annuaire supprimé.");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            try {
                connection.rollback();
                System.err.println("La suppression de l'annuaire " + obj.getId() + " a échouée.");
            } catch (SQLException e1) {
                System.err.println(e1.getMessage());
            }
        } 
    }
}
