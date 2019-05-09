package process_Vul;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

public class ReadSlices {
    String filename;
    String sliceroot;
    List<List<String>> slices=new ArrayList<>();

    public ReadSlices( String sliceroot,String filename) throws IOException {
        this.filename = filename;
        this.sliceroot = sliceroot;

    }


    public void readLine() throws IOException {

        BufferedReader bufferedReader=new BufferedReader(new FileReader(sliceroot+"\\"+filename));
        String s="";
        List<String> slice=new ArrayList<>();
        while((s=bufferedReader.readLine())!=null){
            slice.add(s);
            if(s.startsWith("-----")){
                List<String> list=new ArrayList<>();
                list.addAll(slice);
                list.remove(0);
                list.remove(list.size()-1);
                slices.add(list);
                slice.clear();

            }
        };
        bufferedReader.close();
    }


    public static void main(String[] args) throws IOException {
        ReadSlices readSlices=new ReadSlices("C:\\Users\\troye sivan\\Desktop\\VulDeePecker-master\\CWE-119\\CGD\\slices","CWE134_Uncontrolled_Format_String__char_console_vfprintf_08.c");
        readSlices.readLine();

    }

}
