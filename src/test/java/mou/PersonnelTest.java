package mou;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

public class PersonnelTest {

    @Test
    public void constructeurTestA() {
        Personnel p = new Personnel.Builder("hou", "mou",
                LocalDate.of(1900,01,01),
                new telephone("0700000001", "portable")).build();
        assertEquals("hou", p.getNom());
        assertEquals("mou", p.getPrenom());
        assertEquals(LocalDate.of(1900,01,01), p.getDateNaissance());

        assertEquals(1, p.getnumTelephone().size());
        assertEquals(new telephone("0700000001", "portable"),
                p.getnumTelephone().get(0));
    }
/**
 * *teste de constructeur personnel avec 2 num p et f
 */
    
    @Test
    public void constructeurTestB() {
        Personnel p = new Personnel.Builder("hou", "mou",
                LocalDate.of(1900,01,01),
                new telephone("0700000002", "portable"))
                        .addnumTelephone(new telephone("0900000001", "fixe"))
                        .build();
        assertEquals("hou", p.getNom());
        assertEquals("mou", p.getPrenom());
        assertEquals(LocalDate.of(1900,01,01), p.getDateNaissance());
       

        assertEquals(2, p.getnumTelephone().size());
        assertEquals(new telephone("0900000001", "fixe"), p.getnumTelephone().get(1));
        assertEquals(new telephone("0700000002", "portable"),
                p.getnumTelephone().get(0));
    }

    /**
     * meme test A avec une erreur volentairement 
     * erreur du nom.
     * ce qui renvoi erreur!!
     */
    @Test
    public void constructeurTestC() {
        Personnel p = new Personnel.Builder("houssein", "mou",
                LocalDate.of(1900,01,01),
                new telephone("0700000001", "portable")).build();
        assertEquals("hou", p.getNom());
        assertEquals("mou", p.getPrenom());
        assertEquals(LocalDate.of(1900,01,01), p.getDateNaissance());

        assertEquals(1, p.getnumTelephone().size());
        assertEquals(new telephone("0700000001", "portable"),
                p.getnumTelephone().get(0));
    }
    
/**
 * meme teste b avec erreur volentairement
 * erreur de numtel
 * ce qui renvoi erreur de ce teste mais pas de B
 */
    @Test
    public void constructeurTestD() {
        Personnel p = new Personnel.Builder("hou", "mou",
                LocalDate.of(1900,01,01),
                new telephone("0700000023", "portable"))
                        .addnumTelephone(new telephone("0900000001", "fixe"))
                        .build();
        assertEquals("hou", p.getNom());
        assertEquals("mou", p.getPrenom());
        assertEquals(LocalDate.of(1900,01,01), p.getDateNaissance());
       

        assertEquals(2, p.getnumTelephone().size());
        assertEquals(new telephone("0900000001", "fixe"), p.getnumTelephone().get(1));
        assertEquals(new telephone("0700000002", "portable"),
                p.getnumTelephone().get(0));
    }

   
}
