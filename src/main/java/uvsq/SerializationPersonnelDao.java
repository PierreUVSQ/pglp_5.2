package uvsq;

import java.io.*;

public class SerializationPersonnelDao extends Dao<Personnel> {


   // @Override
    public Personnel create(Personnel obj) {
        try(ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("personnel")))) {
            out.writeObject(obj);
        }
        catch(IOException ioe){

        }
        return obj;


    }

   // @Override
    public Personnel find(String id) {
        Personnel p = null;
        try(ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(id)))) {
            p = (Personnel) in.readObject();

        }
        catch(ClassNotFoundException | IOException e){
            e.printStackTrace();
        }


        return p;
    }

    //@Override
    public void delete(String file) {
        try {
            File f = new File(file);

            if(!f.delete()) {
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