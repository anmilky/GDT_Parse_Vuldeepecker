package process_Vul;

import org.eclipse.core.runtime.CoreException;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BatchProcess {


    public static void main(String[] args) throws IOException, CoreException {
        String sliceroot = "C:\\Users\\troye sivan\\Desktop\\VulDeePecker-master\\CWE-399\\CGD\\slices";
        String rawFileRoot = "C:\\Users\\troye sivan\\Desktop\\VulDeePecker-master\\CWE-399\\source_files";
        String newSliceRoot = "C:\\Users\\troye sivan\\Desktop\\VulDeePecker-master\\CWE-399\\CGD\\slices_changeVar";

        int finish = 0;
        File file = new File(sliceroot);
//        for (File f : file.listFiles()) {
        for(File cve:file.listFiles()){
            String cvename=cve.getName();
            for(File f:cve.listFiles()) {

                String filename = f.getName();
//                String cwetype = f.getName().split("_")[0];
                ReadSlices readSlices = new ReadSlices(sliceroot+"\\"+cvename, filename);
                readSlices.readLine();

                ExtractVarName extractVarName = new ExtractVarName(rawFileRoot, cvename, filename);
                extractVarName.process();

                ChangeVAR changeVAR = new ChangeVAR(newSliceRoot+"\\"+cvename, filename);
                changeVAR.setSlices(readSlices.slices);
                changeVAR.setVarsFunc(extractVarName.varname, extractVarName.funcname);
                changeVAR.changeVar();
//                changeVAR.writeToFile();
                finish++;
                System.out.printf("\r%d | %d", file.listFiles().length, finish);
                System.out.flush();
                break;
            }

        }
    }
}
