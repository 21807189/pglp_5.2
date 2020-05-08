package mou;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import DAO.*;
import mou.*;


public class PersonnelDAOteste {
    private static FabriqueDAO fabrique;
    private static String nomDossierFabriqueAvant;
    private static String nomDossierFabriqueTest;
    private static DAO<Personnel> daoP;

    @BeforeClass
    public static void initDAO() {
        fabrique = FabriqueDAO.getFabriqueDAO(FabriqueDAO.TypeDAO.mouwafak);
        nomDossierFabriqueAvant = ((FabriqueDAOFile) fabrique).getDossierDB();
        int i = 0;
        do {
            ((FabriqueDAOFile) fabrique).setDossierDB("databaseFileForTestUniquely" + i + "\\");
            nomDossierFabriqueTest =  ((FabriqueDAOFile) fabrique).getDossierDB();
            i++;
        } while (new File(nomDossierFabriqueTest).exists());

        daoP = fabrique.getPersonnelDAO();
    }

    @AfterClass
    public static void termineDAO() {
        ((FabriqueDAOFile) fabrique).setDossierDB(nomDossierFabriqueAvant);
        deleteAllDossier(new File(nomDossierFabriqueTest));
    }

    private static boolean deleteAllDossier(final File dossier) {
        if (dossier.isDirectory()) {
            File[] sousDossiers = dossier.listFiles();
            if (sousDossiers != null) {
                for (File f : sousDossiers) {
                    deleteAllDossier(f);
                }
            }
        }
        return dossier.delete();
    }

    @Test
    public void createTest()
            throws FileNotFoundException, ClassNotFoundException, IOException {
        Personnel p = new Personnel.Builder(1, "hou", "mou",
                LocalDate.of(1900,01,01),
                new telephone(1, "0700000001", "portable")).build();
        daoP.create(p);

        String nomFichier =nomDossierFabriqueTest + "Personnel\\" + p.getId() + ".ser";
        File f = new File(nomFichier);
        assertTrue(f.exists());
        Personnel observed = deserialize(nomFichier);
        assertEquals(p, observed);
        assertEquals(p.getId(), observed.getId());
    }

    @Test
    public void findTest() {
        Personnel p = new Personnel.Builder(2, "hou", "mou",
                LocalDate.of(1900,01,01),
                new telephone(1, "0700000001", "portable")).build();
        daoP.create(p);

        Personnel observed = daoP.find(p.getId() + "");
        assertEquals(p.getId(), observed.getId());
        assertEquals(p, observed);
    }

    @Test
    public void updateTest()
            throws FileNotFoundException, ClassNotFoundException, IOException {
        Personnel p = new Personnel.Builder(3, "hou", "mou",
                LocalDate.of(2000, 01, 05),
                new telephone(1, "0700000001", "portable")).build();
        daoP.create(p);

        Personnel updateP = new Personnel.Builder(3, "houss", "mouwaf",
                LocalDate.of(2000, 01, 05),
                new telephone(1, "0700000002", "portable")).build();
        daoP.update(updateP);

        String nomFichier = nomDossierFabriqueTest + "Personnel\\"
                + updateP.getId() + ".ser";
        File f = new File(nomFichier);
        assertTrue(f.exists());
        Personnel observed = deserialize(nomFichier);
        assertEquals(updateP.getId(), observed.getId());
        assertEquals(updateP, observed);
    }

   
    @Test
    public void deleteTest() {
        Personnel p = new Personnel.Builder(4, "hou", "mou",
                LocalDate.of(2000, 01, 05),
                new telephone(1, "0700000001", "portable")).build();
        daoP.create(p);

        daoP.delete(p);
        String nomFichier = nomDossierFabriqueTest + "Personnel\\" + p.getId() + ".ser";
        File f = new File(nomFichier);
        assertFalse(f.exists());
    }

}
