package ec.gob.mdt.ciudadano.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by francisco on 20/09/16.
 */
public class Codex {
    /**
     *
     * @param cadena
     * @param opcion
     * @return cadena codificada/decodificada
     */

    public static String codificador(String cadena,boolean opcion){
        String access="";
        List<String> simbol = new ArrayList();
        simbol.add("63");simbol.add("43");simbol.add("23");simbol.add("03");
        simbol.add("82");simbol.add("62");simbol.add("42");simbol.add("22");
        simbol.add("02");simbol.add("81");simbol.add("61");simbol.add("41");
        simbol.add("21");simbol.add("01");simbol.add("11");simbol.add("31");
        simbol.add("51");simbol.add("71");simbol.add("91");simbol.add("12");
        simbol.add("32");simbol.add("52");simbol.add("72");simbol.add("92");
        simbol.add("13");simbol.add("33");simbol.add("53");simbol.add("74");
        simbol.add("94");simbol.add("15");simbol.add("35");simbol.add("55");
        simbol.add("75");simbol.add("95");simbol.add("16");simbol.add("36");
        simbol.add("56");simbol.add("76");simbol.add("96");simbol.add("17");
        simbol.add("37");simbol.add("27");simbol.add("07");simbol.add("86");
        simbol.add("66");simbol.add("46");simbol.add("26");simbol.add("06");
        simbol.add("85");simbol.add("65");simbol.add("45");simbol.add("25");
        simbol.add("05");simbol.add("84");simbol.add("~[");simbol.add("$;");
        simbol.add(".-");simbol.add(",]");simbol.add("||");simbol.add("({");
        simbol.add("/}");simbol.add("*~");simbol.add(")%");simbol.add("{~");
        simbol.add("!)");simbol.add(":$");simbol.add("%.");simbol.add("~,");
        simbol.add("}/");simbol.add("{*");simbol.add("[(");simbol.add("]!");
        simbol.add(";:");simbol.add("-~");simbol.add("~~");simbol.add("..");
        simbol.add("--");simbol.add(",,");simbol.add("]]");simbol.add("((");
        simbol.add("{{");
        List<String> letras  = new ArrayList();
        letras.add("a");letras.add("b");letras.add("c");letras.add("d");letras.add("e");
        letras.add("f");letras.add("g");letras.add("h");letras.add("i");letras.add("j");
        letras.add("k");letras.add("l");letras.add("m");letras.add("n");letras.add("ñ");
        letras.add("o");letras.add("p");letras.add("q");letras.add("r");letras.add("s");
        letras.add("t");letras.add("u");letras.add("v");letras.add("w");letras.add("x");
        letras.add("y");letras.add("z");letras.add("A");letras.add("B");letras.add("C");
        letras.add("D");letras.add("E");letras.add("F");letras.add("G");letras.add("H");
        letras.add("I");letras.add("J");letras.add("K");letras.add("L");letras.add("M");
        letras.add("N");letras.add("Ñ");letras.add("O");letras.add("P");letras.add("Q");
        letras.add("R");letras.add("S");letras.add("T");letras.add("U");letras.add("V");
        letras.add("W");letras.add("X");letras.add("Y");letras.add("Z");letras.add("1");
        letras.add("2");letras.add("3");letras.add("4");letras.add("5");letras.add("6");
        letras.add("7");letras.add("8");letras.add("9");letras.add("0");letras.add("á");
        letras.add("é");letras.add("í");letras.add("ó");letras.add("ú");letras.add("Á");
        letras.add("É");letras.add("Í");letras.add("Ó");letras.add("Ú");letras.add(" ");
        letras.add("Ä");letras.add("Ë");letras.add("Ï");letras.add("Ö");letras.add("Ü");
        letras.add("_");
        List<String> cadenaArray = Arrays.asList(cadena.split(""));
        if(opcion)
        {
            for(int i=1;i<cadenaArray.size();i++)
            {
                access+=simbol.get(letras.indexOf(cadenaArray.get(i)));
            }
        }
        else if((cadenaArray.size()/2)==Math.round(cadenaArray.size()/2))
        {
            for(int i=1;i<cadenaArray.size();i+=2)
            {
                access+=letras.get(simbol.indexOf(cadenaArray.get(i)+cadenaArray.get(i+1)));
            }
        }
        return access;
    }
}
