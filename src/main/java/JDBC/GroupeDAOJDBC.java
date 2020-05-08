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

import mou.*;
import DAO.*;

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

            IterateurComposant ite = obj.iterateur();
            while (ite.hasNext()) {
                Composant c = ite.next();
                if (c instanceof Personnel) {
                    Personnel p = (Personnel) c;
                    selectPersonnel.setInt(1, p.getId());
                    try (ResultSet rs = selectPersonnel.executeQuery()) {
                        if (!rs.next()) {
                            PersonnelDAOJDBC.insert(p, conn);
                        } else {
                            System.err.println("Personnel " + p.getId()
                            + " existe déjà.");
                        }
                    }
                    insertAppartenir.setInt(1, p.getId());
                    insertAppartenir.execute();
                } else if (c instanceof Groupe) {
                    Groupe g = (Groupe) c;
                    selectGroupe.setInt(1, g.getId());
                    try (ResultSet rs = selectGroupe.executeQuery()) {
                        if (!rs.next()) {
                            GroupeDAOJDBC.insert(g, conn);
                        } else {
                            System.err.println("Groupe " + g.getId()
                            + " existe déjà.");
                        }
                    }
                    insertContenir.setInt(1, g.getId());
                    insertContenir.execute();
                }
            }
        }
    }

    @Override
    public Groupe create(final Groupe obj) {
        try {
            createTables(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        Groupe created = null;
        try {
            connection.setAutoCommit(false);
            insert(obj, connection);
            connection.commit();
            created = obj;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            try {
                connection.rollback();
                System.err.println("L'insertion du Groupe " + obj.getId() + " a échouée.");
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

        return obj;
    }

    static Groupe read(final int idGroupe, final Connection conn)
            throws SQLException {
        Groupe g = null;
        try (PreparedStatement selectGroupe =
                conn.prepareStatement("Select * from Groupe WHERE id = ?")) {
            selectGroupe.setInt(1, idGroupe);
            try (ResultSet rs = selectGroupe.executeQuery()) {
                if (rs.next()) {
                    g = new Groupe(idGroupe, rs.getString("nom"));
                } else {
                    return null;
                }
            }
        }
        try (PreparedStatement selectAppartenir = conn.prepareStatement(
                "Select * from Appartenir WHERE id_groupe = ?")) {
            selectAppartenir.setInt(1, idGroupe);
            try (ResultSet rs = selectAppartenir.executeQuery()) {
                while (rs.next()) {
                    Personnel p = PersonnelDAOJDBC
                            .read(rs.getInt("id_personnel"), conn);
                    if (p != null) {
                        g.add(p);
                    }
                }
            }
        }
        try (PreparedStatement selectContenir = conn.prepareStatement(
                "Select * from contenir WHERE id_groupe_contenant = ?")) {
            selectContenir.setInt(1, idGroupe);
            try (ResultSet rs = selectContenir.executeQuery()) {
                while (rs.next()) {
                    Groupe gFils = GroupeDAOJDBC
                            .read(rs.getInt("id_groupe_contenu"), conn);
                    if (gFils != null) {
                        g.add(gFils);
                    }
                }
            }
        }
        return g;
    }
    
    @Override
    public Groupe find(final String id) {
        try {
            return GroupeDAOJDBC.read(Integer.parseInt(id), connection);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    static boolean modify(final Groupe obj, final Connection conn,
            final boolean insert) throws SQLException {
        try (PreparedStatement selectGroupe =
                conn.prepareStatement("SELECT * FROM groupe WHERE id = ?")) {
            selectGroupe.setInt(1, obj.getId());
            try (ResultSet rs = selectGroupe.executeQuery()) {
                if (!rs.next()) {
                    if (insert) {
                        try (PreparedStatement insertGroupe =
                                conn.prepareStatement(
                                        "insert into groupe values (?, ?)")) {
                            insertGroupe.setInt(1, obj.getId());
                            insertGroupe.setString(2, obj.getNom());
                            insertGroupe.execute();
                        }
                    } else {
                        return false;
                    }
                }
            }
        }
        try (PreparedStatement updateGroupe = conn
                .prepareStatement("UPDATE groupe SET nom = ? WHERE id = ?")) {
            updateGroupe.setString(1, obj.getNom());
            updateGroupe.setInt(2, obj.getId());
            updateGroupe.execute();
        }

        try (PreparedStatement deleteAppartenir = conn.prepareStatement(
                "DELETE from appartenir WHERE id_groupe = ?")) {
            deleteAppartenir.setInt(1, obj.getId());
            deleteAppartenir.execute();
        }
        try (PreparedStatement deleteContenir = conn.prepareStatement(
                "DELETE from contenir WHERE id_groupe_contenu = ? "
                        + "OR id_groupe_contenant = ?")) {
            deleteContenir.setInt(1, obj.getId());
            deleteContenir.setInt(2, obj.getId());
            deleteContenir.execute();
        }

        IterateurComposant ite = obj.iterateur();
        try (PreparedStatement insertAppartenir =
                conn.prepareStatement("insert into appartenir values (?, ?)");
                PreparedStatement insertContenir = conn.prepareStatement(
                        "insert into contenir values (?, ?)");) {
            insertAppartenir.setInt(2, obj.getId());
            insertContenir.setInt(2, obj.getId());
            while (ite.hasNext()) {
                Composant c = ite.next();
                if (c instanceof Personnel) {
                    Personnel p = (Personnel) c;
                    PersonnelDAOJDBC.modify(p, conn, true);
                    insertAppartenir.setInt(1, p.getId());
                    insertAppartenir.execute();
                } else if (c instanceof Groupe) {
                    Groupe g = (Groupe) c;
                    GroupeDAOJDBC.modify(g, conn, true);
                    insertContenir.setInt(1, g.getId());
                    insertContenir.execute();
                }
            }
        }
        return true;
    }

    @Override
    public Groupe update(final Groupe obj) {
        Groupe updated = null;
        try {
            connection.setAutoCommit(false);
            if (modify(obj, connection, false)) {
                updated = obj;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            try {
                connection.rollback();
                System.err.println("La mise à jour du Groupe " + obj.getId() + " a échouée.");
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
    public void delete(final Groupe obj) {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement deleteAppartenir = connection.prepareStatement(
                    "DELETE from appartenir WHERE id_groupe = ?")) {
                deleteAppartenir.setInt(1, obj.getId());
                deleteAppartenir.execute();
                System.out.println(
                        "Groupe " + obj.getId() + " supprimé.");
            } 
            try (PreparedStatement deleteContenir = connection.prepareStatement(
                    "DELETE from contenir WHERE id_groupe_contenu = ?"
                            + " OR id_groupe_contenant = ?")) {
                deleteContenir.setInt(1, obj.getId());
                deleteContenir.setInt(2, obj.getId());
                deleteContenir.execute();
                System.out.println(
                        "Groupe "
                                + obj.getId() + " supprimé.");
            }
            if (tableExists("Annuaire", connection)) {
                try (PreparedStatement deleteAnnuaire =
                        connection.prepareStatement(
                                "DELETE FROM Annuaire "
                                        + "WHERE racine_groupe = ?")) {
                    deleteAnnuaire.setInt(1, obj.getId());
                    deleteAnnuaire.execute();
                }
            }
            System.out.println(
                    "Annuaire " + obj.getId() + " supprimé.");

            try (PreparedStatement deleteGroupe = connection
                    .prepareStatement("DELETE from groupe WHERE id = ?")) {
                deleteGroupe.setInt(1, obj.getId());
                deleteGroupe.execute();
            }
            connection.commit();
            System.out.println("Groupe " + obj.getId() + " supprimé.");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            try {
                connection.rollback();
                System.err.println("La suppression du Groupe " + obj.getId() + " a échouée.");
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
    }

}
