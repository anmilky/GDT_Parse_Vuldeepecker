package process_Vul;

import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTName;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ChangeVAR {
    ArrayList<String> funcname_userdefine;
    ArrayList<String> varname;
    List<List<String>> slices = new ArrayList<>();
    String newSliceRoot;
    String filename;
    String filepath;

    public void setVarsFunc(ArrayList<String> varname, ArrayList<String> funcname_userdefine) {
        this.varname = varname;
        this.funcname_userdefine = funcname_userdefine;
    }

    public void setSlices(List<List<String>> slices) {
        this.slices = slices;
    }

    public ChangeVAR(String newSliceRoot, String filename) {
        this.newSliceRoot = newSliceRoot;
        this.filename=filename;

        File file=new File(this.newSliceRoot);
        if(!file.exists()){
            file.mkdirs();
        }
        filepath=newSliceRoot+"\\"+filename;
    }

    public static String fenciline(String s) {
        String final_s = "";
        for (char item : s.toCharArray()) {
            Boolean flag = false;
            if(item!=95) {
                if (item < 48 || item > 57) {
                    if (item != 32 && (item < 65 || item > 90 && item < 97 || item > 122))
                        flag = true;
                }
            }
            if (flag)
                final_s = final_s + " " + item + " ";
            else
                final_s = final_s + item;
        }
        final_s = final_s.replace("  ", " ");
//            System.out.println(final_s);
        return final_s;
    }

    public void writeToFile() throws IOException {

        BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(filepath,true));
        for (List<String> slice : slices) {
            for (String s : slice) {
                  bufferedWriter.write(s+"\n");

            }
            bufferedWriter.write("---------------------------------\n");
        }
        bufferedWriter.close();

    }

    public void changeVar() {

        List<List<String>> slices_new = new ArrayList<>();
        for (List<String> slice : slices) {
            List<String> slice_new = new ArrayList<>();
            for (String s : slice) {
                s = fenciline(s);
                String[] words = s.split(" ");
                for (int i = 0; i < words.length; i++) {
                    if (funcname_userdefine.contains(words[i])) {
                        int index = funcname_userdefine.indexOf(words[i]) + 1;
                        words[i] = "FUNC" + index;
                    }
                }
                for (int i = 0; i < words.length; i++) {
                    if (varname.contains(words[i])) {
                        int index = varname.indexOf(words[i]) + 1;
                        words[i] = "VAR" + index;
                    }
                }

                s = String.join(" ", words);
                slice_new.add(s);
            }
            slices_new.add(slice_new);
        }
        slices=slices_new;
    }


}
