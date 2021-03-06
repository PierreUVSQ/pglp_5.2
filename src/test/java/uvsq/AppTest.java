package uvsq;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/** Unit test for simple App. */
public class AppTest {
  /** Rigorous Test :-) */
  public static Connection connect = null;

  public static Statement stmt = null;
  public static boolean first = true;
  static File index = new File("test");
  public final static String type = "SGBD";

  @BeforeClass
  public static void init() {

    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      connect = DriverManager.getConnection("jdbc:derby:test;create=true");
      stmt = connect.createStatement();
      /*
      String drop = "DROP TABLE Telephone";
      stmt.execute(drop);
      drop = "DROP TABLE Personnel";
      stmt.execute(drop);
      drop = "DROP TABLE FaitPartiePersonnel";
      stmt.execute(drop);
      drop = "DROP TABLE FaitPartieGroupe";
      stmt.execute(drop);
      drop = "DROP TABLE AnnuaireGroupe";
      stmt.execute(drop);
      drop = "DROP TABLE AnnuairePersonnel";
      stmt.execute(drop);*/

      String createTable =
          "CREATE TABLE Personnel(nom varchar(50) PRIMARY KEY NOT NULL, prenom  varchar(50), fonction varchar(50),naissance DATE)";
      stmt.execute(createTable);
      createTable =
          "CREATE TABLE Telephone (nom varchar(50), tel int, PRIMARY KEY(nom, tel), FOREIGN KEY(nom) REFERENCES Personnel(nom) ON DELETE CASCADE )";
      stmt.execute(createTable);
      createTable = "CREATE TABLE Groupe( nom varchar(50) PRIMARY KEY NOT NULL)";
      stmt.execute(createTable);
      createTable =
          "CREATE TABLE FaitPartiePersonnel(gnom varchar(50), nom varchar(50), PRIMARY KEY(gnom,nom), FOREIGN KEY (gnom) REFERENCES Groupe(nom) ON DELETE CASCADE, FOREIGN KEY (nom) REFERENCES Personnel(nom) ON DELETE CASCADE)";
      stmt.execute(createTable);
      createTable =
          "CREATE TABLE FaitPartieGroupe(gnom varchar(50), nom varchar(50), PRIMARY KEY(gnom,nom), FOREIGN KEY (gnom) REFERENCES Groupe(nom) ON DELETE CASCADE, FOREIGN KEY (nom) REFERENCES Groupe(nom) ON DELETE CASCADE)";
      stmt.execute(createTable);
      createTable =
          "CREATE TABLE AnnuaireGroupe(gnom varchar(50) PRIMARY KEY, FOREIGN KEY (gnom) REFERENCES Groupe(nom) ON DELETE CASCADE)";
      stmt.execute(createTable);
      createTable =
          "CREATE TABLE AnnuairePersonnel(nom varchar(50), PRIMARY KEY(nom), FOREIGN KEY (nom) REFERENCES Personnel(nom) ON DELETE CASCADE)";
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
    g2.ajoutMembre(new Personnel.Builder("jsp1", "lp", "class").updatePhoneList(tmp).build());
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
  }

  @Test
  public void annuaireDAOtest() {

    Dao ad = null;
      ad = Annuaire.createChoisiDaoFactory(type).createAnnuaireDao();

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
    g2.ajoutMembre(new Personnel.Builder("pgAnnuaire", "lp", "class").updatePhoneList(tmp).build());
    a.addEquipe(new Personnel.Builder("pgAnnuaire2", "lp", "class").updatePhoneList(tmp).build());
    a.addEquipe(new Groupe("Groupe1Annuaire"));
    a.addEquipe(new Groupe("Groupe2Annuaire"));
    a.addEquipe(gg);
    a.addEquipe(g2);
    ad.create(a);

    Annuaire test = (Annuaire) ad.find("annuaire");
    System.out.println("here");
    for (Equipe e : test) {
      e.printNom();
    }
  }

  @Test
  public void testGroupeDAO() {
    Groupe g = new Groupe("toto");
    List<String> tmp = new ArrayList<>();
    tmp.add("0000000");
    tmp.add("12345678");
    Personnel p1 =
        new Personnel.Builder("Smith2", "John", "ComputerScienist").updatePhoneList(tmp).build();
    Personnel p2 = new Personnel.Builder("pg2", "lp", "class").updatePhoneList(tmp).build();
    g.ajoutMembre(p1);
    g.ajoutMembre(p2);
    Groupe g2 = new Groupe("UN GROUPE");
    g2.ajoutMembre(new Personnel.Builder("pg3", "lp", "class").updatePhoneList(tmp).build());
    g.ajoutMembre(g2);
    Dao ag = null;
    ag = Annuaire.createChoisiDaoFactory(type).createGroupeDao();

    ag.create(g);
    Groupe test = (Groupe) ag.find("toto");
    for (Equipe e : test) {
      e.printNom();
    }
    assertEquals("toto", test.getNom());
  }

  @Test
  public void testPersonnelDAO() {

    List<String> tmp = new ArrayList<>();
    tmp.add("987654321");
    tmp.add("12345678");
    Personnel p1 =
        new Personnel.Builder("Smith3", "John", "ComputerScienist").updatePhoneList(tmp).build();
    // Dao ap = null;
    try (Dao ap = new PersonnelDao()) {

      ap.create(p1);
      Personnel test = (Personnel) ap.find("Smith3");
      assertEquals("Smith3", test.getNom());
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

  @Test
  public void testDeletePersonnel() {
    List<String> tmp = new ArrayList<>();
    tmp.add("0000000");
    tmp.add("12345678");
    Personnel p1 =
        new Personnel.Builder("SmithDelete", "John", "ComputerScienist")
            .updatePhoneList(tmp)
            .build();
    // Dao ap = null;
    try (Dao ap = new PersonnelDao()) {

      ap.create(p1);
      ap.delete("SmithDelete");
      assertNull((Personnel) ap.find("SmithDelete"));
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testDeleteGroupe() {
    List<String> tmp = new ArrayList<>();
    tmp.add("0000000");
    tmp.add("12345678");
    Personnel p1 =
        new Personnel.Builder("SmithDeleteG", "John", "ComputerScienist")
            .updatePhoneList(tmp)
            .build();
    Groupe g = new Groupe("GroupeDelete1");
    g.ajoutMembre(p1);
    g.ajoutMembre(new Groupe("GroupeDelete2"));
    try (Dao ag = Annuaire.createChoisiDaoFactory("type").createGroupeDao()) {

      ag.create(g);
      ag.delete("GroupeDelete1");

    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void deleteAnnuaireTest() {

    Dao ad = null;
    ad = Annuaire.createChoisiDaoFactory(type).createAnnuaireDao();

    Annuaire a = Annuaire.getInstance();
    String tel = new String("00000000");
    Groupe gg = new Groupe("PDGDelete");
    Groupe g2 = new Groupe("VICE_PRESIDENTDelete");
    List<String> tmp = new ArrayList<>();
    tmp.add(tel);
    tmp.add("12345678");
    Personnel p1 =
        new Personnel.Builder("SmithDeleteAnnuaire", "John", "ComputerScienist")
            .updatePhoneList(tmp)
            .build();
    Personnel p2 =
        new Personnel.Builder("pgDeleteAnnuaire", "lp", "class").updatePhoneList(tmp).build();

    gg.ajoutMembre(p1);
    gg.ajoutMembre(p2);
    g2.ajoutMembre(
        new Personnel.Builder("pgDeleteAnnuaire2", "lp", "class").updatePhoneList(tmp).build());
    a.addEquipe(
        new Personnel.Builder("pgDeleteAnnuaire3", "lp", "class").updatePhoneList(tmp).build());
    a.addEquipe(new Groupe("Groupe1DeleteAnnuaire"));
    a.addEquipe(new Groupe("Groupe2DeleteAnnuaire"));
    a.addEquipe(gg);
    a.addEquipe(g2);
    ad.create(a);
    ad.delete("");
   /* Annuaire res = (Annuaire) ad.find("");
    int i = 0;
    for (Equipe e : res) {
      i++;
      e.printNom();
    }
    System.out.println(i);
    assertEquals(0, i, 0);*/
  }

  @AfterClass
  public static void after() {
    try {
      FileUtils.deleteDirectory(index);
    } catch (IOException e) {
      System.out.println("Creation du test");
    }
  }
}
