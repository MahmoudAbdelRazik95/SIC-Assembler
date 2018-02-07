/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicassembler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptException;

/**
 *
 * @author Gehad
 */
public class FileCreator {

    private static ArrayList<ListFile> l2 = new ArrayList<ListFile>();

    public FileCreator() throws ScriptException {
        l2 = ObjectCode.objectcode();
        createLIS();
        createOBJ();
    }

    private void createLIS() {

        File file = new File("LISFILE");
        FileWriter fw;
        ArrayList<ListFile> listFile = new ArrayList();
        ArrayList<ListFile> temp = new ArrayList();
        listFile.addAll(l2);
        for(ListFile l : listFile)
        {    if(!l.getComment().equalsIgnoreCase("No Comment"))
                continue;
            if(l.getLabel().startsWith("="))
            {
                l.setOperation(l.getLabel());
                l.setOperand("");
                l.setLabel("*");
            }
            if(l.getOperation().equalsIgnoreCase("ltorg"))
                l.setAddress("    ");
            if(l.getOperation().equalsIgnoreCase("rsub"))
                l.setObjcode("4C0000");
        }
        ListFile tempEnd= new ListFile();
        for(ListFile l : l2)
        {
            if(!l.getComment().equalsIgnoreCase("No Comment"))
                continue;
            if(l.getOperation().equalsIgnoreCase("end"))
                tempEnd = l;
            else if(!l.getOperation().equalsIgnoreCase("ltorg")&&!l.getOperation().equalsIgnoreCase("org")&&!l.getOperation().equalsIgnoreCase("equ"))
                temp.add(l);   
        }
        temp.add(tempEnd);
        l2=temp;
 
        try {
            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("FistBump SIC Assembler v1.1");
            bw.newLine();
            bw.newLine();
            bw.newLine();
            for (ListFile l : listFile) {
                bw.write(l.toString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(SicAssembler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void createOBJ() {
        File file = new File("DEVF2");
        FileWriter fw;

        try {
            String head = getHead();
            String text = getText();
            String end = getEnd();
            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(head);
            bw.newLine();
            bw.write(text);
            bw.newLine();
            bw.write(end);
            bw.close();

        } catch (IOException ex) {
            Logger.getLogger(SicAssembler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String hexCal(String a, String b) {
        int sa, ea;
        sa = Integer.parseInt(a, 16);
        ea = Integer.parseInt(b, 16);
        String hex = Integer.toHexString(ea - sa);
        hex = hex.toUpperCase();
        hex = String.format("%2s", hex).replace(' ', '0');
        return hex;
    }

    private String getHead() {
        int i;
        
        String hex, fi = l2.get(0).getLabel();
        
        String add = l2.get(0).getAddress();
        if (fi.length() < 6) {
            i = 6 - fi.length();
            fi = String.format("%-6s", fi);
        }
        if (add.length() < 6) {
            add = String.format("%6s", add).replace(' ', '0');
        }
        hex = hexCal(l2.get(0).getAddress(), l2.get(l2.size() - 1).getAddress());
        if (hex.length() < 6) {
            i = 6 - fi.length();
            hex = String.format("%6s", hex).replace(' ', '0');
        }
        return "H" + fi + add + hex;
    }

    private String getText() {
        int max = 10, i = 1;
        String f = "", objcode = "";
        String len, end = String.format("%6s", l2.get(1).getAddress()).replace(' ', '0'), start = String.format("%6s", l2.get(1).getAddress()).replace(' ', '0');
        String text = "";
        for (i = 1; i <= l2.size() - 1; i++) {
            if (l2.get(i).getComment().equalsIgnoreCase("No Comment") == false) {//Matshelsh Dyh 3ashan deeh betcheck el instruction dyh comment walla l2a .
                continue;
            }
            if (text == "") {
                text = "T";
            }
            if (text.charAt(text.length() - 1) == 'T') {
                text = text + start;
            }
            if (l2.get(i).getOperation().equalsIgnoreCase("end")) {
                if (l2.get(i).getOperation().equalsIgnoreCase("end") && l2.get(i - 1).getOperation().equalsIgnoreCase("resw")) {
                    text = text.substring(0, text.length() - 8);
                    continue;
                } else if (l2.get(i - 1).getOperation().equalsIgnoreCase("resb") && l2.get(i).getOperation().equalsIgnoreCase("end")) {
                    text = text.substring(0, text.length() - 8);
                    continue;
                }
                end = String.format("%6s", l2.get(i).getAddress()).replace(' ', '0');
                len = hexCal(start, end);
                start = String.format("%6s", l2.get(i).getAddress()).replace(' ', '0');
                text = text + len + objcode;
            } else if (objcode.length() == 60) {
                end = String.format("%6s", l2.get(i).getAddress()).replace(' ', '0');
                len = hexCal(start, end);
                start = String.format("%6s", l2.get(i).getAddress()).replace(' ', '0');
                text = text + len + objcode + "\r\nT";
                objcode = l2.get(i).getObjcode();
                if (l2.get(i).getOperation().equalsIgnoreCase("resw") || l2.get(i).getOperation().equalsIgnoreCase("resb")) {
                    objcode = "";
                    start = String.format("%6s", l2.get(i + 1).getAddress()).replace(' ', '0');
                }
            } else if (objcode.length() < 60) {
                if (l2.get(i).getOperation().equalsIgnoreCase("byte") || l2.get(i).getOperation().equalsIgnoreCase("word")) {
                    String x = objcode;
                    objcode = objcode + l2.get(i).getObjcode();
                    if (objcode.length() > 60) {
                        objcode = x;
                        end = String.format("%6s", l2.get(i).getAddress()).replace(' ', '0');
                        len = hexCal(start, end);
                        start = String.format("%6s", l2.get(i).getAddress()).replace(' ', '0');
                        text = text + len + objcode + "\r\nT";
                        objcode = l2.get(i).getObjcode();
                    }
                } else if (!l2.get(i).getOperation().equalsIgnoreCase("resw") && !l2.get(i).getOperation().equalsIgnoreCase("resb")) {
                    objcode = objcode + l2.get(i).getObjcode();
                    objcode = objcode.toUpperCase();
                } else if (l2.get(i).getOperation().equalsIgnoreCase("resw") || l2.get(i).getOperation().equalsIgnoreCase("resb")) {
                    if (l2.get(i).getOperation().equalsIgnoreCase("resw") || l2.get(i).getOperation().equalsIgnoreCase("resb")) {
                        f = text;
                        end = String.format("%6s", l2.get(i).getAddress()).replace(' ', '0');
                        len = hexCal(start, end);
                        end = String.format("%6s", l2.get(i + 1).getAddress()).replace(' ', '0');
                        start = String.format("%6s", l2.get(i + 1).getAddress()).replace(' ', '0');
                        text = text + len + objcode + "\r\nT";
                        objcode = "";
                        if (l2.get(i - 1).getOperation().equalsIgnoreCase("resw") || l2.get(i - 1).getOperation().equalsIgnoreCase("resb")) {
                            text = f;
                            if (l2.get(i + 1).getOperation().equalsIgnoreCase("end")) {
                                continue;
                            }
                            start = String.format("%6s", l2.get(i + 1).getAddress()).replace(' ', '0');
                            text = text.substring(0, text.length() - 6) + start;
                        }
                    } else if (l2.get(i + 1).getOperation().equalsIgnoreCase("resw") || l2.get(i + 1).getOperation().equalsIgnoreCase("resb")) {
                        continue;
                    }
                }
            }
        }
        return text;
    }

    private String getEnd() {
        String text = String.format("%6s", l2.get(0).getAddress()).replace(' ', '0');;
        text = "E" + text;
        return text;
    }

}
