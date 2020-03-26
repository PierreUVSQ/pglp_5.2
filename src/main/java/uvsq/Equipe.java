package uvsq;

import java.io.Serializable;

public abstract class Equipe implements Serializable {

  protected String nom;

  public abstract void printNom();

  public abstract String getNom();
}
