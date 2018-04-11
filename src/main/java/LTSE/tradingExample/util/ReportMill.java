/*
 * Copyright (c) 2014. SQAD LLC
 * All Rights Reserved
 *
 * $Rev:: 1972              $:  Revision of last commit
 * $Author:: linwu          $:  Author of last commit
 * $Date:: 2018-02-15 15:18#$:  Date of last commit
 */

package LTSE.tradingExample.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import com.opencsv.CSVWriter;

/**
 * 
 * @author lwu
 *
 */
public class ReportMill implements AutoCloseable {

    private final String outputDir;
    private File outputFile;
    private OutputStreamWriter bufferedWriter;
    private FileWriter fileWriter;
    private CSVWriter writer;

    public ReportMill (String outputDir) {
        this.outputDir = outputDir;
    }

    @Override
    public void close () throws Exception {
        // Close from outside in
        if (writer != null) {
            writer.flush ();
            writer.close ();
        }

        if (bufferedWriter != null) bufferedWriter.close ();
        if (fileWriter != null) fileWriter.close ();
    }

    public void open (String fileName) throws IOException {
        
    	   outputFile = new File (outputDir, fileName);
           if (outputFile.exists ()) {
               if (! outputFile.delete ()) {
                   throw new IOException (String.format ("Unable to remove existing file %s", outputFile.getAbsolutePath ()));
               }
           }

           outputFile.getParentFile ().mkdirs ();
           if (! outputFile.createNewFile ()) {
               throw new IOException (String.format ("Unable to create file %s", outputFile.getAbsolutePath ()));
           }
           bufferedWriter = new OutputStreamWriter (new FileOutputStream(outputFile), StandardCharsets.UTF_8);
           writer = new CSVWriter (bufferedWriter);
       }
      
  

    public void write (String... fields) {
        writer.writeNext (fields);
    }

    public String getOutputDir () {
        return outputDir;
    }

    public File getOutputFile () {
        return outputFile;
    }

  
   
}
