package uvsq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Unit test for simple App. */
public class AppTest {
  /** Rigorous Test :-) */
  public Connection connect = null;
  public Statement stmt = null;
  @Test
  public void shouldAnswerWithTrue() {
    assertTrue(true);
  }

  @Before
  public void init() {

    Connection connect = null;

    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      connect = DriverManager.getConnection("jdbc:derby:test;create=true");
      stmt = connect.createStatement();
      String drop = "DROP TABLE Telephone";
      stmt.execute(drop);
      drop = "DROP TABLE Personnel";
      stmt.execute(drop);

      String createTable = "CREATE TABLE Personnel(nom varchar(50) PRIMARY KEY NOT NULL, prenom  varchar(50), fonction varchar(50),naissance DATE)";
      stmt.execute(createTable);
      createTable = "CREATE TABLE Telephone (nom varchar(50), tel int, PRIMARY KEY(nom, tel), FOREIGN KEY(nom) REFERENCES Personnel(nom))";
      stmt.execute(createTable);
      createTable = "CREATE TABLE Groupe( nom varchar(50) PRIMARY KEY NOT NULL)";
      stmt.execute(createTable);
      connect.close();
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
      try {
        connect.close();
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
    }


  }

  /**
   * On crée un annuaire complet et on l'enregistre. On le recharge et compare les noms des
   * différents parcours
   */
  @Test
  public void testAnnuaireComplet() {

    Annuaire a = Annuaire.getInstance();
    String tel = new String("00000000");
    Groupe gg = new Groupe("PDG");
    Groupe g2 = new Groupe("VICE_PRESIDENT");
    List<String> tmp = new ArrayList<>();
    tmp.add(tel);
    tmp.add("12345678");
    Personnel p1 =
        new Personnel.Builder("Smith", "John", "ComputerScienist").updatePhoneList(tmp).build();
    Personnel p2 = new Personnel.Builder("pg", "lp", "class").updatePhoneList(tmp).build();

    gg.ajoutMembre(p1);
    gg.ajoutMembre(p2);
    g2.ajoutMembre(p1);
    a.addEquipe(p1);
    a.addEquipe(new Groupe("Groupe1"));
    a.addEquipe(new Groupe("Groupe2"));
    a.addEquipe(gg);
    a.addEquipe(new Groupe("Groupe3"));
    a.addEquipe(new Groupe("Groupe4"));
    a.addEquipe(g2);

    System.out.println("Created");
    try (ObjectOutputStream out =
        new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("data")))) {
      out.writeObject(Annuaire.getInstance());
    } catch (IOException ioe) {

    }
    System.out.println("registered");

    try (ObjectInputStream in =
        new ObjectInputStream(new BufferedInputStream(new FileInputStream("data")))) {
      Annuaire test = (Annuaire) in.readObject();
      for (Equipe e : test) {

        e.printNom();
      }
      Iterator jsp1 = test.iterator();
      Iterator jsp2 = a.iterator();
      Equipe e1;
      Equipe e2;
      while (jsp1.hasNext() && jsp2.hasNext()) {

        e1 = (Equipe) jsp1.next();
        e2 = (Equipe) jsp2.next();
        assertEquals(e1.getNom(), e2.getNom());
      }

    } catch (ClassNotFoundException | IOException e) {

    }

    System.out.println("Start");
  }

  @Test
  public void annuaireDAOtest() {

    Dao ad = null;
    try {
      ad = DaoFactory.getAnnuaireDao();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    Annuaire a = Annuaire.getInstance();
    String tel = new String("00000000");
    Groupe gg = new Groupe("PDG");
    Groupe g2 = new Groupe("VICE_PRESIDENT");
    List<String> tmp = new ArrayList<>();
    tmp.add(tel);
    tmp.add("12345678");
    Personnel p1 =
        new Personnel.Builder("Smith", "John", "ComputerScienist").updatePhoneList(tmp).build();
    Personnel p2 = new Personnel.Builder("pg", "lp", "class").updatePhoneList(tmp).build();

    gg.ajoutMembre(p1);
    gg.ajoutMembre(p2);
    g2.ajoutMembre(p1);
    a.addEquipe(p1);
    a.addEquipe(new Groupe("Groupe1"));
    a.addEquipe(new Groupe("Groupe2"));
    a.addEquipe(gg);
    a.addEquipe(g2);
    ad.create(a);

    Annuaire test = (Annuaire) ad.find("annuaire");

    Iterator jsp1 = test.iterator();
    Iterator jsp2 = a.iterator();
    Equipe e1;
    Equipe e2;
    while (jsp1.hasNext() && jsp2.hasNext()) {

      e1 = (Equipe) jsp1.next();
      e2 = (Equipe) jsp2.next();
      assertEquals(e1.getNom(), e2.getNom());
    }
  }

  @Test
  public void testGroupeDAO() {

    int cpt = GroupeDao.compteur;//compteur d'adresse
    Groupe g = new Groupe("toto");
    List<String> tmp = new ArrayList<>();
    tmp.add("0000000");
    tmp.add("12345678");
    Personnel p1 =
        new Personnel.Builder("Smith", "John", "ComputerScienist").updatePhoneList(tmp).build();
    Personnel p2 = new Personnel.Builder("pg", "lp", "class").updatePhoneList(tmp).build();
    Dao ag = null;
    try {
      ag = DaoFactory.getGroupeDao();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    ag.create(g);
    Groupe test = (Groupe) ag.find("groupe");
    assertEquals("toto", test.getNom());
  }

  @Test
  public void testPersonnelDAO() {

    List<String> tmp = new ArrayList<>();
    tmp.add("0000000");
    tmp.add("12345678");
    Personnel p1 =
        new Personnel.Builder("Smith", "John", "ComputerScienist").updatePhoneList(tmp).build();
    // Dao ap = null;
    try (Dao ap = new PersonnelDao()) {

      ap.create(p1);
     Personnel test = (Personnel) ap.find("Smith");
      assertEquals("Smith", test.getNom());
      for (String e : test.getTel()) {
        System.out.println(e);
      }

    } catch (Exception e) {
      e.printStackTrace();
      try {
        connect.close();
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
  }
}
