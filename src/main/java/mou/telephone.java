package mou;

import java.io.Serializable;

public class telephone implements Serializable {
    private final int id;
    private String information;
    private String numero;

    
    public telephone(final int i, final String num, final String info) {
        this.id = i;
        this.information = info;
        this.numero = num;
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((information == null) ? 0 : information.hashCode());
        result = prime * result + ((numero == null) ? 0 : numero.hashCode());
        return result;
    } 
    
    public String getInformation() {
        return information;
    }

    public String getNumero() {
        return numero;
    }

    @Override
    public String toString() {
        return "(" + this.information + ") " + this.numero;
    }

}
