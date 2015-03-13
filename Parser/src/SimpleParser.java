import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;

public class SimpleParser {
 
	//use ASTParse to parse string
	public static void parse(final File f, String str) {
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
 
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
 
		cu.accept(new ASTVisitor() {
 			
			public boolean visit(MethodInvocation node) {
				SimpleName name = node.getName();
				int line = cu .getLineNumber(node.getStartPosition());
				String methodParams = methodArgsToString(node.arguments());
				System.out.println(f.getName() + " | " + line + " | " + name + methodParams);
				
				return false;
			}
		});
 
	}
	

	// Makes the arguments list of a method look like ("arg1, arg2, ...") instead of ["arg1, arg2,..."]  
	public static String methodArgsToString(List args){
		String arguments = "(";
		if(args.size()==0){
			arguments += ")";
		}else{
			for(int i=0; i < args.size(); i++){
				if(i < args.size()-1){
					arguments += args.get(i) +", ";
				}else{
					arguments += args.get(i) + ")";
				}
			}
		}
		return arguments;
	}

	//Read the file content and returns its data
	public static String readFileToString(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
 
		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
 
		reader.close();
 
		return  fileData.toString();	
	}
 
	//loop directory to get file list to parse
	public static void ParseFilesInDir() throws IOException{
		File dirs = new File(".");
		String dirPath = dirs.getCanonicalPath() + File.separator+"source"+File.separator;
		File root = new File(dirPath);
		File[] files = root.listFiles ( );
		String filePath = null;
		for(File f : files) {
			//Get all the files but SimpleParser.java
			if(!f.getName().equals("SimpleParser.java")){
				filePath = f.getAbsolutePath();
				if(f.isFile()){
					parse(f,readFileToString(filePath));
				}
			}
		 }
	}
 
	public static void main(String[] args) throws IOException {
		ParseFilesInDir();
	}
}