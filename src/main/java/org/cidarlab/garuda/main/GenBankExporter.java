/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.main;

/**
 *
 * @author mardian
 */
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.ProteinSequence;

import com.google.common.base.Splitter;
import org.cidarlab.garuda.main.Part.Orientation;
import org.cidarlab.garuda.main.Part.PartType;

public class GenBankExporter {

    public final static String uniqueId = "F" + System.currentTimeMillis();

    public static final String writeGenBank(GeneticConstruct device, String project) {
        String sequence = device.getSequence();
        String lowCasSeq = sequence.toLowerCase();
        String sequenceLength = Integer.toString(sequence.length());
        String gbkFlatFile = "";
        String desc = device.getName() + " device of the project: " + project;

        int spaces = 28 - uniqueId.length() - sequenceLength.length();
        String spacer = String.format("%" + spaces + "s", "");
        gbkFlatFile += "LOCUS       " + uniqueId + spacer + sequenceLength + " bp    DNA     linear   UNK " + getDate();
        gbkFlatFile += "\n" + "DEFINITION  " + desc;
        gbkFlatFile += "\nACCESSION   " + uniqueId;
        gbkFlatFile += "\n" + "VERSION     " + ".";
        gbkFlatFile += "\nKEYWORDS    " + ".";
        gbkFlatFile += "\nSOURCE      " + ".";
        gbkFlatFile += "\n  ORGANISM  " + ".";
        gbkFlatFile += "\nFEATURES             Location/Qualifiers";
        gbkFlatFile += "\n     source          1.." + sequenceLength;
        gbkFlatFile += "\n                     /note=\"" + desc + "\"";

        List<Part> listParts = device.getPartList();
        List<Coordinate> listCoord = device.getCoordinateList();
        Iterator<Part> it1 = listParts.iterator();
        Iterator<Coordinate> it2 = listCoord.iterator();

        int iterIndex = 0;
        while (it1.hasNext() && it2.hasNext()) {
            iterIndex += 1;
            Part part = it1.next();
            Coordinate coord = it2.next();

            if (part.getPartType() == PartType.CDS) {
                if (part.getOrientation() != Orientation.REVERSE) {
                    gbkFlatFile += "\n     gene            " + coord.getStartX() + ".." + coord.getEndX();
                    gbkFlatFile += "\n                     /gene=\"" + part.getPartProperties().getName() + "\"";
                    gbkFlatFile += "\n                     /locus_tag=\"" + uniqueId + "_" + iterIndex + "\"";
                    gbkFlatFile += "\n     " + part.getPartType() + "             " + coord.getStartX() + ".." + coord.getEndX();
                } else {
                    gbkFlatFile += "\n     gene            complement(" + coord.getStartX() + ".." + coord.getEndX() + ")";
                    gbkFlatFile += "\n                     /gene=\"" + part.getPartProperties().getName() + "\"";
                    gbkFlatFile += "\n                     /locus_tag=\"" + uniqueId + "_" + iterIndex + "\"";
                    gbkFlatFile += "\n     " + part.getPartType() + "             complement(" + coord.getStartX() + ".." + coord.getEndX() + ")";
                }
                gbkFlatFile += "\n                     /gene=\"" + part.getPartProperties().getName() + "\"";
                gbkFlatFile += "\n                     /locus_tag=\"" + uniqueId + "_" + iterIndex + "\"";
                gbkFlatFile += "\n                     /product=\"gp" + part.getPartProperties().getName() + "\"";

                ProteinSequence protein = new DNASequence(part.getPartProperties().getSequence()).getRNASequence().getProteinSequence();
                //System.out.println("type: "+part.getPartType()+"  -  "+part.getPartProperties().getSequence() + " - "+protein.getSequenceAsString() + " " + protein.getLength());

                if (protein.getSequenceAsString().contains("*")) {
                    gbkFlatFile += "\n                     /partial";
                    gbkFlatFile += "\n                     /note=\"partial gene\"";
                } else {
                    //generate /translation="<ProteinSequence>" annotation
                    gbkFlatFile += getProteinToAnnotation(protein);
                }

            } else if (part.getPartType() == PartType.PROMOTER) {
                if (part.getOrientation() != Orientation.REVERSE) {
                    gbkFlatFile += "\n     promoter        " + coord.getStartX() + ".." + coord.getEndX();
                    gbkFlatFile += "\n                     /gene=\"" + part.getPartProperties().getName() + "\"";
                    gbkFlatFile += "\n                     /locus_tag=\"" + uniqueId + "_" + iterIndex + "\"";
                } else {
                    gbkFlatFile += "\n     promoter        complement(" + coord.getStartX() + ".." + coord.getEndX() + ")";
                    gbkFlatFile += "\n                     /gene=\"" + part.getPartProperties().getName() + "\"";
                    gbkFlatFile += "\n                     /locus_tag=\"" + uniqueId + "_" + iterIndex + "\"";
                }
            } else if (part.getPartType() == PartType.TERMINATOR) {
                if (part.getOrientation() != Orientation.REVERSE) {
                    gbkFlatFile += "\n     terminator      " + coord.getStartX() + ".." + coord.getEndX();
                    gbkFlatFile += "\n                     /gene=\"" + part.getPartProperties().getName() + "\"";
                    gbkFlatFile += "\n                     /locus_tag=\"" + uniqueId + "_" + iterIndex + "\"";
                } else {
                    gbkFlatFile += "\n     terminator      complement(" + coord.getStartX() + ".." + coord.getEndX() + ")";
                    gbkFlatFile += "\n                     /gene=\"" + part.getPartProperties().getName() + "\"";
                    gbkFlatFile += "\n                     /locus_tag=\"" + uniqueId + "_" + iterIndex + "\"";
                }
            } else if (part.getPartType() == PartType.RIBOZYME) {
                if (part.getOrientation() != Orientation.REVERSE) {
                    gbkFlatFile += "\n     ncRNA           " + coord.getStartX() + ".." + coord.getEndX();
                    gbkFlatFile += "\n                     /gene=\"" + part.getPartProperties().getName() + "\"";
                    gbkFlatFile += "\n                     /locus_tag=\"" + uniqueId + "_" + iterIndex + "\"";
                } else {
                    gbkFlatFile += "\n     ncRNA           complement(" + coord.getStartX() + ".." + coord.getEndX() + ")";
                    gbkFlatFile += "\n                     /gene=\"" + part.getPartProperties().getName() + "\"";
                    gbkFlatFile += "\n                     /locus_tag=\"" + uniqueId + "_" + iterIndex + "\"";
                }
            } else if (part.getPartType() == PartType.RBS) {
                if (part.getOrientation() != Orientation.REVERSE) {
                    gbkFlatFile += "\n     RBS             " + coord.getStartX() + ".." + coord.getEndX();
                    gbkFlatFile += "\n                     /gene=\"" + part.getPartProperties().getName() + "\"";
                    gbkFlatFile += "\n                     /locus_tag=\"" + uniqueId + "_" + iterIndex + "\"";
                } else {
                    gbkFlatFile += "\n     RBS             complement(" + coord.getStartX() + ".." + coord.getEndX() + ")";
                    gbkFlatFile += "\n                     /gene=\"" + part.getPartProperties().getName() + "\"";
                    gbkFlatFile += "\n                     /locus_tag=\"" + uniqueId + "_" + iterIndex + "\"";
                }
            }
        }

        gbkFlatFile += "\nORIGIN\n";
        Iterable<String> seqArray = Splitter.fixedLength(60).split(lowCasSeq);
        int linecounter = 0;
        for (Iterator<String> it = seqArray.iterator(); it.hasNext();) {
            int spc = 9 - Integer.toString(linecounter * 60).length();
            String s = String.format("%" + spc + "s", "");
            gbkFlatFile += s + Integer.toString((linecounter * 60) + 1);
            Iterable<String> chunk = Splitter.fixedLength(10).split(it.next());
            for (Iterator<String> iter = chunk.iterator(); iter.hasNext();) {
                gbkFlatFile += " " + iter.next();
            }
            gbkFlatFile += "\n";
            linecounter++;
        }
        gbkFlatFile += "//";
        //System.out.println("==========GenBank exporter===========");
        //System.out.println(gbkFlatFile);

        return gbkFlatFile;
    }

    private static String getProteinToAnnotation(ProteinSequence protein) {
        String annotation = "";
        int j = 58;
        int v = 44;
        if (protein.getLength() <= v) {
            annotation += "\n                     /translation=\"" + protein.getSequenceAsString() + "\"";
        } else {
            for (int i = 0; i <= ((protein.getLength() - v) / j); i++) {
                if (i == 0) {
                    annotation += "\n                     /translation=\"" + protein.getSequenceAsString().substring(0, v);
                    if (protein.getLength() <= v + j) {
                        annotation += "\n                     " + protein.getSequenceAsString().substring(v, protein.getLength()) + "\"";
                    } else {
                        annotation += "\n                     " + protein.getSequenceAsString().substring(v, j + v);
                    }
                } else if (protein.getLength() - (i * j + v) < 58) {
                    annotation += "\n                     " + protein.getSequenceAsString().substring(i * j + v, protein.getLength()) + "\"";
                } else {
                    annotation += "\n                     " + protein.getSequenceAsString().substring(i * j + v, i * j + j + v);
                }
            }
        }
        return annotation;
    }

    private static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        return formatter.format(new Date(System.currentTimeMillis())).toUpperCase();
    }
}
