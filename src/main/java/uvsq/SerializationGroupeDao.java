package uvsq;

import java.io.*;

public class SerializationGroupeDao extends Dao<Groupe> {


   //@Override
    public Groupe create(Groupe obj) {
        try(ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(obj.getNom())))) {
            out.writeObject(obj);
        }
        catch(IOException ioe){

        }
        return obj;
    }

    //@Override
    public Groupe find(String id) {
        Groupe g = null;
        try(ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(id)))) {
            g = (Groupe) in.readObject();

        }
        catch(ClassNotFoundException | IOException e){
            e.printStackTrace();
        }


        return g;
    }

    //@Override
    public void delete(String file) {

        try {
            File f = new File(file);

            if (!f.delete()) {
                System.out.println("Failure");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void close() throws Exception {

    }
}