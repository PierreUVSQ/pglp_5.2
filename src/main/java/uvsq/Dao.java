package uvsq;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class Dao<T> {

  Connection connect = DriverManager.getConnection("jdbc:derby:test;create=true");

  protected Dao() throws SQLException {
  }

  public abstract T create(T obj);

  public abstract T find(String id);

  public abstract void delete(String file);
}
