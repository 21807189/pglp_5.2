package mou;

import java.io.Serializable;
import java.util.ArrayList;


public class Annuaire implements Serializable {

    private final int id;
    private Composant racine;

    public Annuaire(final int i, final Composant r) {
        this.id = i;
        this.racine = r;
    }

    public String hierachie() {
        String s = "";
        ArrayList<String> list = racine.hierarchie();
        for (String str : list) {
            s = s.concat(str + "\n");
        }
        return s;
    }

    public String groupe() {
        ArrayList<Composant> aTraiter = new ArrayList<Composant>();
        ArrayList<Composant> aTraiterSuiv = new ArrayList<Composant>();
        aTraiterSuiv.add(racine);
        Composant c;
        String s = "";
        while (!aTraiter.isEmpty() || !aTraiterSuiv.isEmpty()) {
            if (aTraiter.isEmpty()) {
                aTraiter.addAll(aTraiterSuiv);
                aTraiterSuiv.clear();
                s = s.concat("---\n");
            }
            c = aTraiter.remove(0);
            s = s.concat(c.toString() + "\n");
            if (c instanceof IterableComposant) {
                IterateurComposant ite = ((IterableComposant) c).iterateur();
                while (ite.hasNext()) {
                    aTraiterSuiv.add(ite.next());
                }
            }
        }
        return s;
    }

    @Override
    public int hashCode() {
        final int prime = 25;
        int result = 1;
        result = prime * result + ((racine == null) ? 0 : racine.hashCode());
        return result;
    }

   
    public int getId() {
        return id;
    }
    
    public Composant getRacine() {
        return racine;
    }

}
