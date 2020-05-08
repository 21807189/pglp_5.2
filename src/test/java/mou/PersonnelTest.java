package mou;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

public class PersonnelTest {

    @Test
    public void constructeurTestA() {
        Personnel p = new Personnel.Builder(1,"hou", "mou",
                LocalDate.of(1900,01,01),
                new telephone(1,"0700000001", "portable")).build();
        assertEquals("hou", p.getNom());
        assertEquals("mou", p.getPrenom());
        assertEquals(LocalDate.of(1900,01,01), p.getDateNaissance());

        assertEquals(1, p.getNumeros().size());
        assertEquals(new telephone(1,"0700000001", "portable"),
                p.getNumeros().get(0));
    }
/**
 * *teste de constructeur personnel avec 2 num portable et fixe
 */
    
    @Test
    public void constructeurTestB() {
        Personnel p = new Personnel.Builder(1,"hou", "mou",
                LocalDate.of(1900,01,01),
                new telephone(1,"0700000002", "portable"))
                        .addNumero(new telephone(1,"0900000001", "fixe"))
                        .build();
        assertEquals("hou", p.getNom());
        assertEquals("mou", p.getPrenom());
        assertEquals(LocalDate.of(1900,01,01), p.getDateNaissance());
       

        assertEquals(2, p.getNumeros().size());
        assertEquals(new telephone(1,"0900000001", "fixe"), p.getNumeros().get(1));
        assertEquals(new telephone(1,"0700000002", "portable"),
                p.getNumeros().get(0));
    }

    /**
     * meme test A avec une erreur volentairement 
     * erreur du nom.
     * ce qui renvoi erreur!!
     */
    @Test
    public void constructeurTestC() {
        Personnel p = new Personnel.Builder(10,"houssein", "mou",
                LocalDate.of(1900,01,01),
                new telephone(10,"0700000001", "portable")).build();
        assertEquals("hou", p.getNom());
        assertEquals("mou", p.getPrenom());
        assertEquals(LocalDate.of(1900,01,01), p.getDateNaissance());

        assertEquals(1, p.getNumeros().size());
        assertEquals(new telephone(10,"0700000001", "portable"),
                p.getNumeros().get(0));
    }
    
/**
 * meme teste b avec erreur volentairement
 * erreur de numtel
 * ce qui renvoi erreur de ce teste mais pas de B
 */
    @Test
    public void constructeurTestD() {
        Personnel p = new Personnel.Builder(2,"hou", "mou",
                LocalDate.of(1900,01,01),
                new telephone(2,"0700000023", "portable"))
                        .addNumero(new telephone(2,"0900000001", "fixe"))
                        .build();
        assertEquals("hou", p.getNom());
        assertEquals("mou", p.getPrenom());
        assertEquals(LocalDate.of(1900,01,01), p.getDateNaissance());
       

        assertEquals(2, p.getNumeros().size());
        
        assertEquals(new telephone(2,"0900000001", "fixe"), p.getNumeros().get(1));
        
        assertEquals(new telephone(2,"0700000002", "portable"),
                p.getNumeros().get(0));
    }

   
}
