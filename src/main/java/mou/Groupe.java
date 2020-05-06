package mou;

import java.io.Serializable;
import java.util.ArrayList;

public class Groupe implements Composant, IterableComposant, Serializable {
   
    private final int id;   
    private String nom;
    private ArrayList<Composant> composantFils;

    public Groupe(final int i, final String n) {
        this.id = i;
        this.nom = n;
        composantFils = new ArrayList<Composant>();
    }

   
    public void add(final Composant c) {
        composantFils.add(c);
    }

   
    public Composant get(final int index) {
        return composantFils.get(index);
    }

   
    public String getNom() {
        return this.nom;
    }

   
    public int getId() {
        return id;
    }

   
    public Composant remove(final int index) {
        return composantFils.remove(index);
    }

  
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((composantFils == null) ? 0 : composantFils.hashCode());
        result = prime * result + ((nom == null) ? 0 : nom.hashCode());
        return result;
    }

    public int size() {
        return composantFils.size();
    }

    public ArrayList<String> hierarchie() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(this.toString());
        IterateurComposant ite = this.iterateur();
        Composant c;
        while (ite.hasNext()) {
            c = ite.next();
            for (String s : c.hierarchie()) {
                if (s.substring(0, 1).equals("\t")) {
                    s = "\t" + s;
                } else {
                    s = "\t|-   " + s;
                }
                list.add(s);
            }
        }
        return list;
    }

    public String toString() {
        return "Groupe " + this.nom + " (" + this.composantFils.size() + ")";
    }

    public IterateurComposant iterateur() {
        IterateurComposant ite = new IterateurComposant(this.composantFils);
        return ite;
    }

}
