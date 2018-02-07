package sicassembler;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptException;

public class SicAssembler {

    public static void main(String[] args) {

        try {
            FileCreator F = new FileCreator();
        } catch (ScriptException ex) {
            Logger.getLogger(SicAssembler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
