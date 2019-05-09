package process_Vul;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.internal.core.dom.parser.cpp.*;
import process_C.GenSlice;
import process_C.TravelNode;

import java.io.IOException;
import java.util.*;

public class VarVistor {
    String Filepath ;
    String filename ;
    CPPASTTranslationUnit unit;
    ArrayList<String>funcname_userdefine;
    ArrayList<String>varname;
    public VarVistor(String filepath, String filename, CPPASTTranslationUnit unit) {
        Filepath = filepath;
        this.filename = filename;
        this.unit=unit;
        this.funcname_userdefine=new ArrayList<>();
        this.varname=new ArrayList<>();
        funcname_userdefine.sort(((o1, o2) -> o1.length()-o2.length()));
        varname.sort(((o1, o2) -> o1.length()-o2.length()));
    }
    public static boolean ifuserdefine(String callname) {
        String[] s = {"cin", "getenv", "getenv_s", "wgetenv", "wgetenv_s", "catgets", "gets", "getchar", "getc", "getch", "getche", "kbhit", "stdin", "getdlgtext", "getpass", "scanf", "fscanf", "vscanf", "vfscanf", "istream.get", "istream.getline", "istream.peek", "istream.read*", "istream.putback", "streambuf.sbumpc", "streambuf.sgetc", "streambuf.sgetn", "streambuf.snextc", "streambuf.sputbackc", "SendMessage", "SendMessageCallback", "SendNotifyMessage", "PostMessage", "PostThreadMessage", "recv", "recvfrom", "Receive", "ReceiveFrom", "ReceiveFromEx", "Socket.Receive*", "memcpy", "wmemcpy", "memccpy", "memmove", "wmemmove", "memset", "wmemset", "memcmp", "wmemcmp", "memchr", "wmemchr", "strncpy", "strncpy*", "lstrcpyn", "tcsncpy*", "mbsnbcpy*", "wcsncpy*", "wcsncpy", "strncat", "strncat*", "mbsncat*", "wcsncat*", "bcopy", "strcpy", "lstrcpy", "wcscpy", "tcscpy", "mbscpy", "CopyMemory", "strcat", "lstrcat", "lstrlen", "strchr", "strcmp", "strcoll", "strcspn", "strerror", "strlen", "strpbrk", "strrchr", "strspn", "strstr", "strtok", "strxfrm", "readlink", "fgets", "sscanf", "swscanf", "sscanf_s", "swscanf_s", "printf", "vprintf", "swprintf", "vsprintf", "asprintf", "vasprintf", "fprintf", "sprint", "snprintf", "snprintf*", "snwprintf*", "vsnprintf", "CString.Format", "CString.FormatV", "CString.FormatMessage", "CStringT.Format", "CStringT.FormatV", "CStringT.FormatMessage", "CStringT.FormatMessageV", "syslog", "malloc", "Winmain", "GetRawInput*", "GetComboBoxInfo", "GetWindowText", "GetKeyNameText", "Dde*", "GetFileMUI*", "GetLocaleInfo*", "GetString*", "GetCursor*", "GetScroll*", "GetDlgItem*", "GetMenuItem*"};
        ArrayList<String> funcname = new ArrayList<>();
        funcname.addAll(Arrays.asList(s));

        if (!funcname.contains(callname)) {
            return true;
        }
        return false;

    }

    public void extractVarFunc() {
        List<CPPASTIdExpression> ids = TravelNode.findall(unit,CPPASTIdExpression.class);
        ArrayList<String> vars_s=new ArrayList<>();
        ArrayList<String> funs_s=new ArrayList<>();
        List<CPPASTFunctionCallExpression> funcs= TravelNode.findall(unit,CPPASTFunctionCallExpression.class);
        List<CPPASTFunctionDeclarator> declarators= TravelNode.findall(unit,CPPASTFunctionDeclarator.class);
        for(CPPASTFunctionDeclarator declarator:declarators){
            String name=declarator.getRawSignature();
            if(!funs_s.contains(name)){
                funs_s.add(name);
            }

        }
        for(CPPASTFunctionCallExpression fun:funcs){
            String name=fun.getFunctionNameExpression().toString();
            if(!funs_s.contains(name)){
                funs_s.add(name);
            }

        }
        for(IASTNode node:ids){
           String s=node.getRawSignature();
           if(!funs_s.contains(s)&&!vars_s.contains(s)){
               vars_s.add(s);
           }
        }
        for(String s:funs_s){
            if(ifuserdefine(s)){
                funcname_userdefine.add(s);
            }
        }
        varname.addAll(vars_s);

    }

};

