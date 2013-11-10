////////////////////////////////////////////////////////////////////////////////
// The following FIT Protocol software provided may be used with FIT protocol
// devices only and remains the copyrighted property of Dynastream Innovations Inc.
// The software is being provided on an "as-is" basis and as an accommodation,
// and therefore all warranties, representations, or guarantees of any kind
// (whether express, implied or statutory) including, without limitation,
// warranties of merchantability, non-infringement, or fitness for a particular
// purpose, are specifically disclaimed.
//
// Copyright 2013 Dynastream Innovations Inc.
////////////////////////////////////////////////////////////////////////////////
// ****WARNING****  This file is auto-generated!  Do NOT edit this file.
// Profile Version = 8.20Release
// Tag = $Name: AKW8_200 $
////////////////////////////////////////////////////////////////////////////////


package com.garmin.fit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * Encodes message objects into a FIT binary file.
 * 
 */
public class FileEncoder implements MesgListener, MesgDefinitionListener {
   private File file;
   private FileOutputStream out;
   private MesgDefinition lastMesgDefinition[] = new MesgDefinition[Fit.MAX_LOCAL_MESGS];

   public FileEncoder() {
   }

   public FileEncoder(File file) {
      open(file);
   }

   /**
    * Opens file for writing. If the file already exists it will be overwritten.
    * 
    * @param file
    *           file to write
    */
   public void open(File file) {
      
	  file.delete();            
      this.file = file;

      writeFileHeader();
      try {
         out = new FileOutputStream(this.file, true); // Open output stream to write messages.
      } catch (java.io.IOException e) {
         throw new FitRuntimeException(e);
      }
   }

   /**
    * Writes the file header. 
    */
   private void writeFileHeader() {
      if (file == null)
         throw new FitRuntimeException("File not open.");

      try {
         RandomAccessFile raf = new RandomAccessFile(file, "rw");
         long dataSize = file.length() - Fit.FILE_HDR_SIZE;
         int nextByte = 0;
         int crc = 0;

         if (dataSize < 0)
            dataSize = 0;

         for (int i = 0; i < (Fit.FILE_HDR_SIZE - 2); i++) {
            switch (i) {
               case 0:  nextByte = Fit.FILE_HDR_SIZE;                break;
               case 1:  nextByte = Fit.PROTOCOL_VERSION;             break;
               case 2:  nextByte = (Fit.PROFILE_VERSION & 0xFF);     break;
               case 3:  nextByte = (Fit.PROFILE_VERSION >> 8);       break;
               case 4:  nextByte = (int) (dataSize & 0xFF);          break;
               case 5:  nextByte = (int) ((dataSize >> 8) & 0xFF);   break;
               case 6:  nextByte = (int) ((dataSize >> 16) & 0xFF);  break;
               case 7:  nextByte = (int) ((dataSize >> 24) & 0xFF);  break;
               case 8:  nextByte = '.';                              break;
               case 9:  nextByte = 'F';                              break;
               case 10: nextByte = 'I';                              break;
               case 11: nextByte = 'T';                              break;
               default:                                              break;
            }

            raf.write(nextByte);
            crc = CRC.get16(crc, (byte) nextByte);
         }

         raf.write((int) (crc & 0xFF));
         raf.write((int) ((crc >> 8) & 0xFF));
         raf.close();
      } catch (java.io.IOException e) {
         throw new FitRuntimeException(e);
      }
   }

   /**
    * MesgListener interface. 
    */
   public void onMesg(Mesg mesg) {
      write(mesg);
   }

   /**
    * MesgDefinitionListener interface. 
    */
   public void onMesgDefinition(MesgDefinition mesgDefinition) {
      write(mesgDefinition);
   }
   
   /**
    * Writes a message definition to the file.
    * 
    * @param mesgDefinition
    *           message definition object to write
    */
   public void write(MesgDefinition mesgDefinition) {
      if (file == null)
         throw new FitRuntimeException("File not open.");

      mesgDefinition.write(out);
      lastMesgDefinition[mesgDefinition.localNum] = mesgDefinition;
   }
   
   /**
    * Writes a message to the file.
    * Automatically writes message definition if required.
    * 
    * @param mesg
    *           message object to write
    */
   public void write(Mesg mesg) {
      if (file == null)
         throw new FitRuntimeException("File not open.");

      if ((lastMesgDefinition[mesg.localNum] == null) || !lastMesgDefinition[mesg.localNum].supports(mesg)) {
         write(new MesgDefinition(mesg));
      }
      
      mesg.write(out, lastMesgDefinition[mesg.localNum]);
   }
   
   /**
    * Writes a list of messages to the file.
    * 
    * @param mesgs
    *           list message objects to write
    */
   public void write(List<Mesg> mesgs) {
      for (Mesg mesg : mesgs) {
         write(mesg);
      }
   }

   /**
    * Updates the data size in the file header, writes the CRC, and closes the file.
    */
   public void close() {
      if (file == null)
         throw new FitRuntimeException("File not open.");
      
      try {
    	  out.close();
          
          // Set the data size in the file header.
          writeFileHeader();

         // Compute the CRC.
         FileInputStream in = new FileInputStream(file);
         int crc = 0;
         for (int i = 0; i < file.length(); i++) {
            crc = CRC.get16(crc, (byte) in.read());
         }
         in.close();
         
         // Write the CRC.
         out = new FileOutputStream(file, true); 
         out.write((int) (crc & 0xFF));
         out.write((int) ((crc >> 8) & 0xFF));

         out.close();
         file = null;
      } catch (java.io.IOException e) {
         throw new FitRuntimeException(e);
      }
   }
}
