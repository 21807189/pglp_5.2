package mou;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public  class PersonnelAnnuaire implements Serializable {

     private String nomAnnuaire;
     private List<Personnel> listPersonnels = new ArrayList<Personnel>();

     public void printAnnuNom() 
     {
         // TODO Auto-generated method stub
         System.out.println("Annuaire de "+nomAnnuaire);
     }

     public PersonnelAnnuaire(String nom)
     {
         this.nomAnnuaire = nom;
     }

     public void addPersonnel(Personnel personnel)
     {
         listPersonnels.add(personnel);
     }

     public void removePersonnel(Personnel personnel)
     {
         listPersonnels.remove(personnel);
     }

     public void getPersonnel()
     {

     }

	public void printAnnuaireName() {
		// TODO Auto-generated method stub
		
	}

}