package com.ssi.util;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.WriteAbortedException;

import org.apache.log4j.Logger;

public class DataUtil {
	
	private static Logger LOG = Logger.getLogger(DataUtil.class);
	
	public static <T> T loadDataFromFile(File dataFile){
        if(dataFile.isDirectory())dataFile.delete();
        if(!dataFile.exists()){
            try {
				dataFile.createNewFile();
			} catch (IOException e1) {
				dataFile.delete();
				try {
					dataFile.createNewFile();
				} catch (IOException e2) {
					LOG.error("Failed to re-create data file: ", e2);
					return null;
				}
			}
            LOG.debug("No data file exists, creating new file: ["+dataFile.getAbsolutePath()+"]");
        }
        T data = null;
        ObjectInputStream in = null;
        try {
            LOG.debug("Reading data file ["+dataFile.getAbsolutePath()+"] ... ");
			in = new ObjectInputStream(new FileInputStream(dataFile));
            if(in != null){
                data = (T) in.readObject();
                LOG.debug(">> Success!");
            }
            return data;
        } catch (EOFException e) {
            LOG.debug(">> Canceled.");
            LOG.error("File is empty: ", e);
        } catch (ClassNotFoundException e) {
            LOG.debug(">> Canceled.");
            LOG.error("File format issue: ", e);
        } catch (NotSerializableException e){
            LOG.debug(">> Canceled.");
            LOG.error("Cannot load data: ", e);
        } catch (WriteAbortedException e){
            LOG.debug(">> Canceled.");
            LOG.error("Cannot load data: ", e);
        } catch (IOException e) {
        	 LOG.debug(">> Canceled.");
             LOG.error("Cannot load data: ", e);
		}finally{
            if(in != null){
                try {
					in.close();
				} catch (IOException e) {
					// ignore;
				}
            }
        }
        return null;
	}
	
    public static <T> void saveDataToFile(File dataFile, T data) {
        if(dataFile == null) return;
        ObjectOutputStream out = null;
        try {
            LOG.debug("Saving data for ["+dataFile.getAbsolutePath()+"] ...");
            out = new ObjectOutputStream(new FileOutputStream(dataFile));
            out.flush();
            out.writeObject(data);
            LOG.debug(">> Saved.");
        } catch (FileNotFoundException e1) {
            LOG.debug(">> Canceled.");
            LOG.error("File cannot open: ", e1);
        } catch (IOException e1) {
            LOG.debug(">> Canceled.");
            LOG.error("IOException: ", e1);
        }finally{
        	if(out != null){
                try {
                    out.flush();
                    out.close();
                } catch (IOException e1) {
                    LOG.error("IOException: ", e1);
                }
        	}
        }
    }
	
}
