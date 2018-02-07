package sicassembler;

import java.util.ArrayList;
import javax.script.ScriptException;

public class ObjectCode {

    private static ArrayList<ListFile> b = new ArrayList<ListFile>();
    private static OpTable b22 = new OpTable();

    public static ArrayList<ListFile> objectcode() throws ScriptException {
        b = AssignAdresses.assign();
        System.out.println(b.size());
        for (int i = 1; i < b.size(); i++) {
            String ba = "";
            int x = 0;
            String test = b.get(i).getComment();
            if (test.equalsIgnoreCase("No Comment") == false) {
                continue;
            }
            String b2 = b.get(i).getOperation();
            if(b2.equalsIgnoreCase("ltorg")||b2.equalsIgnoreCase("org"))
                continue;
            
            if (!b2.equalsIgnoreCase("resw") && !b2.equalsIgnoreCase("resb") && !b2.equalsIgnoreCase("end") && !b2.equalsIgnoreCase("word") && !b2.equalsIgnoreCase("byte")&& !b2.equalsIgnoreCase("equ")&& !b2.equalsIgnoreCase("org")) {
                String b3 = b22.getOP(b2);
                String ll = "";
                if (b3.equalsIgnoreCase("invalid")) {
                    b.get(i).setErr(true);

                    continue;
                }
                if (b.get(i).getComment().equals("No Comment") == false) {
                    continue;
                }
                String b4 = b.get(i).getOperand();
                if (b4.indexOf(',') != -1) {
                    x = 1;
                }
                if (b.get(i).getOperand().startsWith("0")) {
                    String z = b.get(i).getOperand();
                    z = z.replaceFirst("0", "");
                    ba = z;
                } else {
                    String buffer = b.get(i).getOperand();
                    if (buffer.contains(",")) {
                        int l = buffer.indexOf(",");
                        buffer = buffer.substring(0, l);

                    }
                    boolean flag = false;

                    for (ListFile l : b) {
                        if (l.getLabel().equals(buffer)) {
                            ba = l.getAddress();
                            flag = true;
                        }

                    }
                    if (flag == false) {
                        b.get(i).setOperandError(true);
                    }

                }
                if (ba.equals("")) {
                    continue;
                }

                long dec = Long.parseLong(ba, 16);
                String bin = String.format("%16s", Long.toBinaryString(dec)).replace(' ', '0');
                bin = "" + x + "" + bin.substring(1);
                long bin2 = Long.parseLong(bin, 2);
                String hex = java.lang.Long.toHexString(bin2);
                if(hex.length()<4)
        {int m= hex.length();
         
        for(int j=0;j<4-m;j++)
            hex="0"+hex;
            
        }
                String objcode = b3 + hex;
                b.get(i).setObjcode(objcode.toUpperCase());
            } else if (b2.equalsIgnoreCase("byte")) {
                String s = b.get(i).getOperand();
                if (s.startsWith("c'") || s.startsWith("C'")) {
                    s = s.replace("c'", "");
                    s = s.replace("C'", "");
                    s = s.replace("'", "");
                    String ascii = "";
                    for (int t = 0; t < s.length(); t++) {
                        String z = Integer.toHexString(s.charAt(t));
                        ascii = ascii + z;

                    }
                    b.get(i).setObjcode(ascii.toUpperCase());

                } else if (s.startsWith("x'") || s.startsWith("X'")) {
                    s = s.replace("x'", "");
                    s = s.replace("X'", "");
                    s = s.replace("'", "");
                    b.get(i).setObjcode(s.toUpperCase());
                }
            } else if (b2.equalsIgnoreCase("word")) {
                String s = b.get(i).getOperand();
                int g = Integer.parseInt(s);
                String bearing = "";

                String z = Integer.toHexString(g);
                for (int k = 0; k < 6 - z.length(); k++) {
                    bearing = bearing + "0";
                }
                bearing = bearing + z;
                b.get(i).setObjcode(bearing.toUpperCase());

            }
            

        }

        return b;
    }
    private static boolean isNumber(String s)
    {//System.out.println(s);
        boolean check = true;
        
        try {

        Integer.parseInt(s);

    }catch (NumberFormatException e) {
        check = false;
    }
    return check;
    }
}
