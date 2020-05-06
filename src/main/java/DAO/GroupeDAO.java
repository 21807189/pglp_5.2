package DAO;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mou.Groupe;

public class GroupeDAO extends DAO<Groupe> {
    
    private String dossier;

    public GroupeDAO(final String dossierDB) {
        dossier = dossierDB + "Groupe\\";
    }

    @Override
    public Groupe create(final Groupe obj) {
        String chemin = dossier;
        if (!new File(chemin).exists()) {
            if (!new File(chemin).mkdirs()) {
                return null;
            }
        }
        chemin += obj.getId() + ".ser";
        File f = new File(chemin);
        if (f.exists()) {
            return null;
        }
        try {
            serialize(obj, chemin);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return obj;
    }

    @Override
    public Groupe find(final String id) {
        String nomFichier = dossier + id + ".ser";
        try {
            Groupe g = deserialize(nomFichier);
            if (!id.equals(g.getId() + "")) {
                return null;
            } else {
                return g;
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Groupe update(final Groupe obj) {
        String nomFichier = dossier + obj.getId() + ".ser";
        File f;
        f = new File(nomFichier);
        if (!f.exists()) {
            System.err.println("Le groupe n'existe pas, impossible de modifier.");
            return null;
        }
        Groupe g;
        try {
            g = deserialize(nomFichier);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
        if (obj.getId() != g.getId()) {
            return null;
        }

        try {
            serialize(obj, nomFichier);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return obj;
    }

    @Override
    public void delete(final Groupe obj) {
        String nomFichier = dossier + obj.getId() + ".ser";
        File f;
        f = new File(nomFichier);
        if (!f.exists()) {
            System.err.println("Impossible de supprimer, le groupe n'existe pas");
            return;
        }
        Groupe g;
        try {
            g = deserialize(nomFichier);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return;
        }
        if (obj.getId() != g.getId()) {
            return;
        }
        if (!obj.equals(g)) {
            return;
        }
        if (f.delete()) {
            System.out.println("Groupe supprimé.");
        } else {
            System.err.println("erreur, groupe non supprimé.");
        }
    }

   
    private void serialize(final Groupe g, final String nomFichier)
            throws FileNotFoundException, IOException {
        try (ObjectOutputStream out =
                new ObjectOutputStream(new BufferedOutputStream(
                        new FileOutputStream(new File(nomFichier))))) {
            out.writeObject(g);
        }
    }

    private Groupe deserialize(final String nomFichier)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        try (ObjectInputStream in =
                new ObjectInputStream(new BufferedInputStream(
                        new FileInputStream(new File(nomFichier))))) {
            Object o = in.readObject();
            if (o instanceof Groupe) {
                return (Groupe) o;
            }
        }
        return null;
    }
}
