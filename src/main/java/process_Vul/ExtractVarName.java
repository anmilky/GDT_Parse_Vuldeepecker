package process_Vul;

import org.eclipse.cdt.core.dom.ast.IASTComment;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.parser.*;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTTranslationUnit;
import org.eclipse.core.runtime.CoreException;
import process_C.FuncVisitor;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.NoSuchFileException;
import java.util.*;

public class ExtractVarName {

    String RawFileRoot;
    String filename;
    String cwetype;
    String filepath;
    ArrayList<String> funcname;
    ArrayList<String>varname;
    public ExtractVarName(String RawFileRoot, String cwetype, String filename) {
        this.RawFileRoot = RawFileRoot;
        this.cwetype = cwetype;
        this.filename = filename;
    }

    public File findRAWfile(){
        File file=new File(RawFileRoot+"\\"+cwetype);
        String filehead=filename.split("\\.")[0].toLowerCase();
        File[] vulners= file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if(pathname.getName().toLowerCase().startsWith(filehead)){
                    return true;
                }
                return false;
            }
        });
        if(vulners.length>0){
            return vulners[0];
        }else {
//            File[] files=file.listFiles(new FileFilter() {
//                @Override
//                public boolean accept(File pathname) {
//                    if(pathname.getName().endsWith("txt"))
//                        return false;
//                    else
//                        return true;
//                }
//            });
//            if(files.length>0){
//                return files[0];
//            }else {
                return null;
//            }
        }

    }


    public void process() throws CoreException, NoSuchFileException {
        filepath=RawFileRoot+"\\"+cwetype+"\\"+filename;
        File file=findRAWfile();
        if(file==null){
            System.out.println(filepath);
            throw new NoSuchFileException(filepath);
        }
        FileContent fileContent = FileContent.createForExternalFileLocation(file.getAbsolutePath());
        Map definedSymbols = new HashMap();
        String[] includePaths = new String[0];
        IScannerInfo info = new ScannerInfo(definedSymbols, includePaths);
        IParserLogService log = new DefaultLogService();

        IncludeFileContentProvider emptyIncludes = IncludeFileContentProvider.getEmptyFilesProvider();

        int opts = 8;
        IASTTranslationUnit translationUnit = GPPLanguage.getDefault().getASTTranslationUnit(fileContent, info, emptyIncludes, null, opts, log);
        VarVistor Visitor = new VarVistor(filename, cwetype, (CPPASTTranslationUnit) translationUnit);
        Visitor.extractVarFunc();
        this.funcname=Visitor.funcname_userdefine;
        this.varname=Visitor.varname;
    }

    public static void main(String[] args) throws CoreException, NoSuchFileException {
        ExtractVarName extractVarName=new ExtractVarName("C:\\Users\\troye sivan\\Desktop\\SARD_reorganize_C",
                "CWE15","CWE15_External_Control_of_System_or_Configuration_Setting__w32_01.c"
        );
        extractVarName.process();
//        extractVarName.varname.forEach(System.out::println);
//        extractVarName.funcname.forEach(System.out::println);

    }


}
