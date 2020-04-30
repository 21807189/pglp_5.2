package mou;

import java.time.LocalDate;


public final class Main {
	
	private Main() {
	}
	
    public static void main(final String[] args) {
    	  Personnel p1 = new Personnel.Builder("mou", "hou",
                  LocalDate.of(2000, 01, 05),
                  new telephone("07000000", "portable"))
                  .build();
          Personnel p2 = new Personnel.Builder("omu", "ohu",
                  LocalDate.of(2000, 01, 05),
                  new telephone("07000001", "portable"))
                  .build();
          Personnel p3 = new Personnel.Builder("muo", "huo",
                  LocalDate.of(2000, 01, 05),
                  new telephone("07000002", "portable"))
                  .build();
          Personnel p4 = new Personnel.Builder("uom", "uoh",
                  LocalDate.of(2000, 01, 05),
                  new telephone("07000003", "portable"))
                  .build();
          Personnel p5 = new Personnel.Builder("mouu", "houu",
                  LocalDate.of(2000, 01, 05),
                  new telephone("07000004", "portable"))
                  .build();
          Personnel p6 = new Personnel.Builder("mouwa", "houss",
                  LocalDate.of(2000, 01, 05),
                  new telephone("07000000", "portable"))
                  .build();

          Groupe g1 = new Groupe("G1");
          g1.add(p1);
          g1.add(p2);
          
          Groupe g2 = new Groupe("g1");
          g2.add(p3);
          g2.add(p4);
          g2.add(p5);
          g2.add(g1);
          
          Groupe g3 = new Groupe("G3");
          g3.add(p6);
          g3.add(g1);

          Annuaire a = new Annuaire(g3);
         // System.out.println(a.hierachie());
          System.out.println(a.groupe());
      }
    }

