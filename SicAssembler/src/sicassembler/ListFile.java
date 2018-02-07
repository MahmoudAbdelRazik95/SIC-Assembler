package sicassembler;

public class ListFile {

    private String address;
    private String objcode = "      ";
    private String label;
    private String operation;
    private String operand;
    private boolean Err;
    private String Comment = "No Comment";
    private boolean operandError;
    private String comm = "";
    private boolean orgErr= false;
    private boolean equErr= false;
    private boolean evalErr = false;
    
    public void setComm(String comm) {
        this.comm = comm;
    }

    public boolean isEvalErr() {
        return evalErr;
    }

    public void setEvalErr(boolean evalErr) {
        this.evalErr = evalErr;
    }
    

    public boolean isErr() {
        return Err;
    }

    public void setErr(boolean Err) {
        this.Err = Err;
    }

    public String getComment() {
        return Comment;
    }

    public boolean isOrgErr() {
        return orgErr;
    }

    public void setOrgErr(boolean orgErr) {
        this.orgErr = orgErr;
    }

    
    public void setComment(String Comment) {
        this.Comment = Comment;
    }
    

    public boolean isOperandError() {
        return operandError;
    }

    public void setOperandError(boolean operandError) {
        this.operandError = operandError;
    }

    public boolean isLabelError() {
        return labelError;
    }

    public void setLabelError(boolean labelError) {
        this.labelError = labelError;
    }
    private boolean labelError;

    public ListFile() {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        int m= address.length();
            for(int i =0;i<4-m;i++)
                address="0"+address;
        this.address = address.toUpperCase();
    }

    public String getObjcode() {
        return objcode;
    }

    public void setObjcode(String objcode) {
        this.objcode = objcode;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    public boolean isEquErr() {
        return equErr;
    }

    public void setEquErr(boolean equErr) {
        this.equErr = equErr;
    }
    

    @Override
    public String toString() {
        if (Comment.equals("No Comment")) {

            String space1 = "";
            String space2 = "";
            String space3 = "";
            
            for (int i = 1; i <= 9 - label.length(); i++) {
                space1 = space1 + " ";
            }
            for (int i = 1; i <= 8 - operation.length(); i++) {
                space2 = space2 + " ";
            }
            for (int i = 1; i <= 15 - operand.length(); i++) {
                space3 = space3 + " ";
            }
            String space34 = "";
            for (int i = 1; i <= 6 - objcode.length(); i++) {
                space34 = space34 + " ";
            }
            String lollipop = "";
            if (objcode.length() > 6) {
                int l = (int) (objcode.length() / 6.0 - 1);
                int k = 1;

                lollipop = (address + "  " + objcode.substring(0, 6) + "  " + label + space1 + operation + space2 + operand + space3 + comm + "\n");
                while (k <= l) {
                    lollipop = lollipop + ("      " + objcode.substring(k * 6, (k * 6) + 6) + "\n");

                    k++;

                }
                if (k * 6 < objcode.length()) {
                    lollipop = lollipop + ("      " + objcode.substring(k * 6));
                } else {
                    lollipop = lollipop.substring(0, lollipop.length() - 1);
                }

            } else {
                lollipop = address + "  " + objcode + space34 + "  " + label + space1 + operation + space2 + operand + space3 + comm;
            }

            if (isErr()) {
                lollipop = lollipop + "\n **** illegal format in operation field                 \n"
                        + " **** unrecognized operation code                       \n"
                        + " **** missing or misplaced operand in instruction";
            }
            if (isLabelError()) {
                lollipop = lollipop + "\n **** Duplicate Label ";
            }
            if (isOperandError()) {
                lollipop = lollipop + "\n **** Unrecognized Operand ";
            }
            if (isOrgErr()){
                lollipop = lollipop + "\n **** Undefined Label ";
            }
            if (isEvalErr()){
                lollipop = lollipop + "\n **** Undefined operation ";
            }
            return lollipop;

        } else {
            return this.Comment;
        }

    }
}
