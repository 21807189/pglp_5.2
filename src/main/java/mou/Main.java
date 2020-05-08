package mou;

import java.time.LocalDate;

import DAO.*;


public final class Main {
	
	private Main() {
	}
	
    public static void main(final String[] args) {
    	  Personnel p1 = new Personnel.Builder(1,"mou", "hou",
                  LocalDate.of(2000, 01, 05),
                  new telephone(1,"07000000", "portable"))
                  .build();
          Personnel p2 = new Personnel.Builder(2,"omu", "ohu",
                  LocalDate.of(2000, 01, 05),
                  new telephone(2,"07000001", "portable"))
                  .build();
          Personnel p3 = new Personnel.Builder(3,"muo", "huo",
                  LocalDate.of(2000, 01, 05),
                  new telephone(3,"07000002", "portable"))
                  .build();
          Personnel p4 = new Personnel.Builder(4,"uom", "uoh",
                  LocalDate.of(2000, 01, 05),
                  new telephone(4,"07000003", "portable"))
                  .build();
          Personnel p5 = new Personnel.Builder(5,"mouu", "houu",
                  LocalDate.of(2000, 01, 05),
                  new telephone(5,"07000004", "portable"))
                  .build();
          Personnel p6 = new Personnel.Builder(6,"mouwa", "houss",
                  LocalDate.of(2000, 01, 05),
                  new telephone(6,"07000000", "portable"))
                  .build();

          Groupe g1 = new Groupe(1,"G1");
          g1.add(p1);
          g1.add(p2);
          
          Groupe g2 = new Groupe(2,"g1");
          g2.add(p3);
          g2.add(p4);
          g2.add(p5);
          g2.add(g1);
          
          Groupe g3 = new Groupe(3,"G3");
          g3.add(p6);
          g3.add(g1);

          Annuaire a = new Annuaire(1,g3);
          System.out.println(a.hierachie());
          System.out.println(a.groupe());
      }
    
    public void runJDBCTelephone() {
        DAO<telephone> dao = FabriqueDAO.getFabriqueDAO(FabriqueDAO.TypeDAO.mouwafak).getTelephoneDAO();
        telephone t1 = new telephone(1, "0700000001", "portable");
        System.out.println("Telephone : " + t1.toString());
        dao.create(t1);

        telephone t2 = dao.find("1");
        System.out.println("Tel recupere : " + t2.toString());

        t1 = new telephone(1, "0700000002", "portable");
        System.out.println("Modifié : " + t1.toString());
        dao.update(t1);

        t2 = dao.find("1");
        System.out.println("Tel modifié recuperé : " + t2.toString());

        dao.delete(t2);

        t2 = dao.find("1");
        System.out.println(t2 == null);
    }

    public void runJDBCPersonnel() {
        DAO<Personnel> dao = FabriqueDAO.getFabriqueDAO(FabriqueDAO.TypeDAO.mouwafak).getPersonnelDAO();
        Personnel p =
                new Personnel.Builder(1, "mou", "hou", LocalDate.of(2000, 01, 05),
                        new telephone(2, "0700000001", "portable"))
                                .addNumero(new telephone(3, "0900000001", "fixe"))
                                .build();
        System.out.println("Personnel : " + p.toString());
        dao.create(p);

        Personnel p2 = dao.find("1");
        System.out.println("Personnel récupéré : " + p2.toString());

        Personnel p3 = new Personnel.Builder(1, "mouw", "hous",
                LocalDate.of(2008, 01, 05), new telephone(2, "0700000001", "poable"))
                        .addNumero(new telephone(5, "0900000001", "fixe")).build();
        System.out.println("Personnel modifié : " + p3.toString());
        dao.update(p3);

        Personnel p4 = dao.find("1");
        System.out.println("Personnel modifié récupéré : " + p4.toString());

        dao.delete(p4);

        p2 = dao.find("1");
        System.out.println("supprimé ? ");
        System.out.println(p2 == null);
    }

    public void runJDBCGroupe() {
        Personnel p =
                new Personnel.Builder(1, "mou", "hou", LocalDate.of(2000, 01, 05),
                        new telephone(1, "0700000001", "portable")).build();
        Personnel p2 =
                new Personnel.Builder(2, "mouw", "hous", LocalDate.of(2000, 01, 05),
                        new telephone(2, "0700000002", "portable"))
                                .addNumero(new telephone(3, "0900000002", "fixe"))
                                .build();
        Groupe g = new Groupe(1, "G1");
        g.add(p);
        g.add(p2);
        Groupe gm = new Groupe(2, "GG");
        Personnel p11 =
                new Personnel.Builder(11, "1", "1", LocalDate.of(2000, 01, 05),
                        new telephone(11, "06.0876..", "portable")).build();
        gm.add(p11);
        g.add(gm);
        System.out.println("Groupe : " + g.hierarchie());
        DAO<Groupe> dao = FabriqueDAO.getFabriqueDAO(FabriqueDAO.TypeDAO.mouwafak).getGroupeDAO();
        dao.create(g);

        Groupe g1 = dao.find("1");
        System.out.println("Groupe récupéré : " + g1.hierarchie());

        Personnel p3 =
                new Personnel.Builder(3, "1", "1", LocalDate.of(2000, 01, 05),
                        new telephone(1, "0656.", "portable")).build();
        Personnel p4 =
                new Personnel.Builder(2, "1", "2", LocalDate.of(2000, 01, 05),
                        new telephone(2, "055..", "portable"))
                                .addNumero(new telephone(3, "04...", "pole"))
                                .build();
        Groupe g2 = new Groupe(1, "G1modif");
        g2.add(p3);
        g2.add(p4);
        g2.add(gm);
        System.out.println("Groupe modif : " + g2.hierarchie());

        dao.update(g2);
        Groupe g3 = dao.find("1");
        System.out.println("Groupe modif récupéré : " + g3.hierarchie());

        dao.delete(gm);
        FabriqueDAO.getFabriqueDAO(FabriqueDAO.TypeDAO.mouwafak).getPersonnelDAO().delete(p3);
        FabriqueDAO.getFabriqueDAO(FabriqueDAO.TypeDAO.mouwafak).getTelephoneDAO().delete(new telephone(2, "num", "info"));
        g2 = dao.find("1");
       
        System.out.println("Groupe supp récupéré : " + g2.hierarchie());
    }

    public void runJDBCAnnuaire() {
        Personnel p =
                new Personnel.Builder(1, "sof", "mah", LocalDate.of(2000, 01, 05),
                        new telephone(1, "0700000003", "portable")).build();
        Personnel p2 =
                new Personnel.Builder(2, "sfo", "mha", LocalDate.of(2000, 01, 05),
                        new telephone(2, "0700000004", "portable")).build();
        Personnel p3 =
                new Personnel.Builder(3, "ofs", "ahm", LocalDate.of(2000, 01, 05),
                        new telephone(3, "0700000005", "portable")).build();
        Personnel p4 =
                new Personnel.Builder(4, "sofr", "maha", LocalDate.of(2000, 01, 05),
                        new telephone(4, "0700000006", "portable")).build();
        Personnel p5 =
                new Personnel.Builder(5, "sofron", "maham", LocalDate.of(2000, 01, 05),
                        new telephone(5, "0700000007", "portable")).build();
        Personnel p6 =
                new Personnel.Builder(6, "sofrone", "mahamo", LocalDate.of(2000, 01, 05),
                        new telephone(6, "0700000008", "portable")).build();

        Groupe g = new Groupe(1, "G1");
        g.add(p);
        g.add(p2);
        Groupe g2 = new Groupe(2, "G2");
        g2.add(p3);
        g2.add(p4);
        g2.add(g);
        g2.add(p6);
        Groupe g3 = new Groupe(3, "G3");
        g3.add(p5);
        g3.add(g2);

        Annuaire a = new Annuaire(1, g3);
        System.out.println("**** Annuaire : " + a.hierachie());

        DAO<Annuaire> dao = FabriqueDAO.getFabriqueDAO(FabriqueDAO.TypeDAO.mouwafak).getAnnuaireDAO();
        dao.create(a);

        Annuaire a2 = dao.find("1");
        System.out.println("**** Annuaire récupéré : " + a2.hierachie());

        Groupe g4 = new Groupe(4, "G4");
        g2.add(p);
        g4.add(g2);
        Annuaire a3 = new Annuaire(1, g4);
        System.out.println("****Annuaire modif : " + a3.hierachie());

        dao.update(a3);
        Annuaire a4 = dao.find("1");
        System.out.println("**** Annuaire modif récupéré : " + a4.hierachie());

        dao.delete(a4);

        a2 = dao.find("1");
        System.out.println("Delete ? ");
        System.out.println(a2 == null);
    }

    
}

    

