package utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jem on 2017/5/8.
 */

public class DataOperation {

    public void saveStorage2SDCard(List<String> tArrayList, String fileName) {
        File fileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/stockManager/cache");
        if (!fileDir.exists()){
            fileDir.mkdirs();
        }
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            File file = new File(fileDir.getAbsolutePath()+"/"+fileName);
            if (file.exists()){
                file.delete();
            }
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);  //新建一个内容为空的文件
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(tArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (objectOutputStream != null) {
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fileOutputStream != null) {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<String> getStorageEntities(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/stockManager/cache/"+fileName);
        ObjectInputStream objectInputStream = null;
        FileInputStream fileInputStream = null;
        ArrayList<String> savedArrayList = new ArrayList<>();
        try {

            fileInputStream = new FileInputStream(file);
            objectInputStream = new ObjectInputStream(fileInputStream);
            savedArrayList = (ArrayList<String>) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return savedArrayList;
    }

}
