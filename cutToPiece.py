import os

# os.chdir(r"C:\Users\troye sivan\Desktop\VulDeePecker-master\CWE-399\CGD")
# with open("cwe399_cgd.txt","r",encoding="utf-8") as f:
#     with open("cwe399_cwetype.txt","a",encoding="utf-8") as f2:
#         count=0
#         flag=False
#         for line in f.readlines():
#             if line.startswith("------"):
#                 flag=True
#             elif flag==True:
#                 f2.write(line+"\n")
#                 flag=False
import re



def write(lines):
    if lines.__len__() > 0:
        filename = lines[0]
        filename = filename.strip();
        filename1 = filename.split("/")[1]
        cvedir=filename.split("/")[0].split(" ")[1]
        filename = filename1.split(" ")[0]
        if not os.path.exists("slices/"+cvedir):
            os.makedirs("slices/"+cvedir)
        with open("slices/"+cvedir+"/"+ filename, "a", encoding="utf-8") as f:
            [f.write(item) for item in lines]




os.chdir(r"C:\Users\troye sivan\Desktop\VulDeePecker-master\CWE-119\CGD")
type = []
with open("cwe119_cgd.txt", "r", encoding="utf-8") as f:
    flag = False
    filename = ""
    lines = []
    for line in f.readlines():
        lines.append(line);
        if line.startswith("--------"):
            if(len(lines)>0):
                 write(lines)
            lines = []
