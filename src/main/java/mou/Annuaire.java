package mou;

import java.io.Serializable;
import java.util.ArrayList;


public class Annuaire implements Serializable {
   
    private Composant racine;

    public Annuaire(final Composant r) {
        this.racine = r;
    }

    public String hierachie() {
        String s = "";
        ArrayList<String> liste = racine.hierarchie();
        for (String str : liste) {
            s = s.concat(str + "\n");
        }
        return s;
    }

    public String groupe() {
        ArrayList<Composant> mou = new ArrayList<Composant>();
        ArrayList<Composant> hou = new ArrayList<Composant>();
        hou.add(racine);
        Composant c;
        String s = "";
        while (!mou.isEmpty() || !hou.isEmpty()) {
            if (mou.isEmpty()) {
                mou.addAll(hou);
                hou.clear();
                s = s.concat("---\n");
            }
            c = mou.remove(0);
            s = s.concat(c.toString() + "\n");
            if (c instanceof IterableComposant) {
                IterateurComposant ite  = ((IterableComposant) c).iterateur();
                while (ite.hasNext()) {
                	hou.add(ite.next());
                }
            }
        }
        return s;
    }

	public static Annuaire getInstance() {
		// TODO Auto-generated method stub
		return null;
	}

}
