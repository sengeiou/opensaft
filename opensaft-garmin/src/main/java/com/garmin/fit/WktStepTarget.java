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

public enum WktStepTarget {
   SPEED((short)0),
   HEART_RATE((short)1),
   OPEN((short)2),
   CADENCE((short)3),
   POWER((short)4),
   GRADE((short)5),
   RESISTANCE((short)6),
   INVALID((short)255);


   protected short value;




   private WktStepTarget(short value) {
     this.value = value;
   }

   public static WktStepTarget getByValue(final Short value) {
      for (final WktStepTarget type : WktStepTarget.values()) {
         if (value == type.value)
            return type;
      }

      return WktStepTarget.INVALID;
   }

   public short getValue() {
      return value;
   }


}