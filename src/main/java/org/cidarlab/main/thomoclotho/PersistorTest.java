/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho;

import org.clothocad.core.datums.ObjBase;
import org.clothocad.core.datums.ObjectId;
import org.clothocad.core.persistence.Persistor;
import org.clothocad.model.Part;
import org.clothocad.model.Person;
import org.clothocad.model.Sequence;

/**
 *
 * @author mardian
 */
public class PersistorTest extends AuthorizedShiroTest {

    private static Persistor persistor;

    public PersistorTest() {
        persistor = injector.getInstance(Persistor.class);
    }

    public void runTest() {

        Person anonymous = new Person("Anonymous");
        Person vinay = new Person("Vinay S. Mahajan");
        Person roxanne = new Person("Roxanne Shank");
        Person randy = new Person("Randy Rettberg");
        Person reshma = new Person("Reshma Shetty");

        /*
	* Sequences
         */
        // Elowitz RBS sequence
        Sequence seqB0034 = new Sequence("B0034 Sequence", "aaagaggagaaa", vinay);

        // Terminator sequences
        Sequence seqB0010 = new Sequence("B0010 Sequence", "ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctc", randy);
        Sequence seqB0012 = new Sequence("B0012 Sequence", "tcacactggctcaccttcgggtgggcctttctgcgtttata", reshma);

        // Lumazine Synthase gene sequences
        Sequence seqR0010 = new Sequence("R0010 Sequence", "caatacgcaaaccgcctctccccgcgcgttggccgattcattaatgcagctggcacgacaggtttcccgactggaaag"
                + "cgggcagtgagcgcaacgcaattaatgtgagttagctcactcattaggcaccccaggctttacactttatgcttccggctcgtatgttgtgtggaattgtgagcgga"
                + "taacaatttcacaca", anonymous);
        Sequence seqK249002 = new Sequence("K249002 Sequence", "atgcagatttatgaaggcaaactgaccgcggaaggcctgcgctttggcattgtggcgagccgctttaaccatgcgc"
                + "tggtggatcgcctggtggaaggcgcgattgattgcattgtgcgccatggtggtcgcgaagaagatattaccctggtgcgcgtgccgggcagctgggaaattccggtgg"
                + "cggcgggcgaactggcgcgcaaagaagatattgatgcggtgattgcgattggcgtgctgattgaaggcgcggaaccgcattttgattatattgcgagcgaagtgagca"
                + "aaggcctggcgaacctgagcctggaactgcgcaaaccgattacctttggcgtgattaccgcggatgaactggaagaagcgattgaacgcgcgggcaccaaacatggca"
                + "acaaaggctgggaagcggcgctgagcgcgattgaaatggcgaacctgtttaaaagcctgcgctag", roxanne);

        // Elowitz RBS part
        Part partB0034 = new Part("BBa_B0034", seqB0034, vinay);

        // Terminator parts
        Part partB0010 = new Part("BBa_B0010", seqB0010, randy);
        Part partB0012 = new Part("BBa_B0012", seqB0012, reshma);

        // Lumazine Synthase gene parts
        Part partR0010 = new Part("BBa_R0010", seqR0010, anonymous);
        Part partK249002 = new Part("BBa_K249002", seqK249002, roxanne);
        
        ObjectId oid = persistor.save(partK249002);
        
        System.out.println(persistor.get(ObjBase.class, oid));

    }

}
