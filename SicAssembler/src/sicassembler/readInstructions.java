


package sicassembler;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class readInstructions {
   
     private static ArrayList<ListFile> x = new ArrayList();
     private static Scanner scan;
     private static  String a;
     private static String b;
     private static String c;
     private static String d;
     private static  ArrayList<String> Labels = new ArrayList();
     private static ArrayList<ListFile> literalPool = new ArrayList();
     private static int flag=0;
     private static int flag2=0;
     private static ArrayList<String> check = new ArrayList();
     
     public static ArrayList<ListFile> getInstructions()
     {
        openfile();
    
        return x;
     }
    
    private static void openfile(){
        
        try{
            scan = new Scanner(new  File(System.getProperty("user.dir")+"\\SRCFILE"));
            add();
            closefile();
        }
        catch(Exception e){
             JOptionPane.showMessageDialog(null, "File Not Found", "Error", JOptionPane.WARNING_MESSAGE);
             
             System.exit(0);
        }
    }
    private static void add(){
         
        while (scan.hasNext()){
            String z  = scan.nextLine();
           if(z.trim().length()==0)
               continue;
           ListFile l = new ListFile();
           if(z.startsWith("."))
           {
               l.setComment(z);
               l.setLabel("Not Valid");
               x.add(l);
               continue;
           }
            a = z.substring(0, 9);
            a=a.trim();
            b = z.substring(9, 17);
            b=b.trim();
            if(z.length()<35){
                c = z.substring(17);
            c=c.trim(); 
            }
            else
            {
                c = z.substring(17,35);
            c=c.trim();
            d=z.substring(35);
            d=d.trim();
            l.setComm(d);
            
                
            }
            
            l.setLabel(a);
            l.setOperation(b);
            l.setOperand(c);
            
            if (b.equalsIgnoreCase("ORG")){
                for(int i=0;i<Labels.size();i++){
                    if (Labels.get(i).equalsIgnoreCase(c)){
                        flag=1;
                    }
                }
               if (flag==0){
                   l.setOrgErr(true);
               }
            }
            if (b.equalsIgnoreCase("EQU")){
                for(int i=0;i<Labels.size();i++){
                    if (Labels.get(i).equalsIgnoreCase(c)){
                        flag2=1;
                    }
                }
                if (flag2==0){
                    l.setEquErr(true);
                }
            }
            
            if(Labels.contains(a)==false){
            Labels.add(a);
            if (a.equals(""))
                Labels.remove(a);
            x.add(l);
            
            }
            else 
            {
                l.setLabelError(true);
                x.add(l);
            }
            if(b.equalsIgnoreCase("LTORG"))
            {
                x.addAll(literalPool);
                literalPool.clear();
            }
            if(c.startsWith("="))
            {ListFile literal = new ListFile();
            boolean flagger = true;
                String op=c.substring(1);
                if(op.startsWith("x")||op.startsWith("X")||op.startsWith("c")||op.startsWith("C"))
                {
                     literal.setLabel(c);
                    literal.setOperation("BYTE");
                    c=c.substring(1);
                    
                    literal.setOperand(c);
                    
                    
                }
                else
                { literal.setLabel(c);
                    literal.setOperation("WORD");
                    c=c.substring(1);
                    literal.setOperand(c);
                }
               
                   if(!check.contains(c))
                   {
                       check.add(c);
                       literalPool.add(literal);
                   }
               
            }
            
        }
        x.addAll(literalPool);
    }
    private static void closefile(){

        scan.close();
    }
    
}
