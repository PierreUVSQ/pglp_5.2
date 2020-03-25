package uvsq;

import java.io.*;

public class PersonnelDao implements Dao<Personnel> {

  @Override
  public Personnel create(Personnel obj) {
    try (ObjectOutputStream out =
        new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("personnel")))) {
      out.writeObject(obj);
    } catch (IOException ioe) {

    }
    return obj;
  }

  @Override
  public Personnel find(String id) {
    Personnel p = null;
    try (ObjectInputStream in =
        new ObjectInputStream(new BufferedInputStream(new FileInputStream(id)))) {
      p = (Personnel) in.readObject();

    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }

    return p;
  }

  @Override
  public void delete(String file) {}
}
