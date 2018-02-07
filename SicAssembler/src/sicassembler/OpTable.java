package sicassembler;

import java.util.HashMap;

public class OpTable {

    private HashMap<String, String> opTable = new HashMap<String, String>();

    public OpTable() {
        addOps();
    }

    private void addOps() {
        opTable.put("add", "18");
        opTable.put("and", "40");
        opTable.put("comp", "28");
        opTable.put("div", "24");
        opTable.put("j", "3C");
        opTable.put("jeq", "30");
        opTable.put("jgt", "34");
        opTable.put("jlt", "38");
        opTable.put("jsub", "48");
        opTable.put("lda", "00");
        opTable.put("ldch", "50");
        opTable.put("ldl", "08");
        opTable.put("ldx", "04");
        opTable.put("mul", "20");
        opTable.put("or", "44");
        opTable.put("rd", "D8");
        opTable.put("rsub", "4C");
        opTable.put("sta", "0C");
        opTable.put("stch", "54");
        opTable.put("stl", "14");
        opTable.put("stx", "10");
        opTable.put("sub", "1C");
        opTable.put("td", "E0");
        opTable.put("tix", "2C");
        opTable.put("wd", "DC");
    }

    public String getOP(String key) {
        String s;
        if (opTable.containsKey(key.toLowerCase())) {
            s = opTable.get(key.toLowerCase());

        } else {
            s = "invalid";
        }

        return s;
    }
}
