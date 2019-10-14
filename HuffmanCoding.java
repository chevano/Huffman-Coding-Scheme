/*
**		----------------------------------------------------------------------------------------------------------------------
**									Notes:
**		----------------------------------------------------------------------------------------------------------------------
**		In order to obtain any delimiter Scanner.nextLine had to be used. 
**		Any other read method from scanner would have resulted into a token that is not a delimiter.
**		Even though Scanner.nextLine included delimiters with Ascii representation ranging from 32 and onwards
**		delimiters that ranged from 0 - 31 were neglected. For the purpose of this project it was crucial to capture
**		delimiters such as the line feed(10), and carriage return(13).
**		
**		To capture the above mention delimiter, the approach taken was to explicitly include these missing delimiters
**		while reading the file line by line.
*/

import java.util.*;
import java.io.*;


public class HuffmanCoding{
	
	public static class TreeNode{
		String chStr;
		int prob;
		String code;
		TreeNode left;
		TreeNode right;
		TreeNode next;
		
		public TreeNode(){
			chStr = "dummy";
			prob  = -999;
			code  = "";
			left  = null;
			right = null;
			next  = null;
		}

		public TreeNode(String chStr, int prob, String code, TreeNode left, TreeNode right, TreeNode next){
			this.chStr = chStr;
			this.prob  = prob;
			this.code  = code;
			this.left  = left;
			this.right = right;
			this.next  = next;
		}
	}
	
	public static class linkedList{
		static TreeNode listHead;

		public linkedList(){
			listHead = new TreeNode();
		}

		public static TreeNode findSpot(TreeNode newNode){
			TreeNode spot = listHead;
			while(spot.next != null){
				if(spot.next != null && spot.next.prob < newNode.prob)
					spot = spot.next;
				else
					return spot;
			}
			return spot;
		}

		public static void insert(TreeNode spot,TreeNode newNode){
			newNode.next = spot.next;
			spot.next = newNode;
		}

		public static void insertNewNode(TreeNode listHead,TreeNode newNode){
			TreeNode spot = findSpot(newNode);
			insert(spot,newNode);
		}


		public static String printList(){
			String line = "listHead --> ";
			TreeNode ptr = listHead;
			while(ptr.next != null){
				line += "(\"" + ptr.chStr + "\", "+ Integer.toString(ptr.prob);
				line += ", \"" + ptr.next.chStr + "\") --> ";
				ptr = ptr.next;
			}
			line += "(\"" + ptr.chStr + "\", ";
			line += Integer.toString(ptr.prob)+ ", NULL) --> NULL\n";
			
			return line;
		}
	}

	public static class BinaryTree {
		static TreeNode Root;
		static int[] charCountAry = new int[256];    // To store the characters count
		static String[] charCode  = new String[256]; // To store the Huffman code table
		
		public BinaryTree(){
			Root = new TreeNode();
		}

		public BinaryTree(TreeNode listHead){
			Root = listHead;
		}
		
		public static void computeCharCounts(String fileName)throws IOException{

			Scanner inFile = new Scanner(new FileReader(fileName));			
			int index = 0;
			for(int i = 0; i < charCountAry.length; i++)
				charCountAry[i] = 0;		//setting each character count to zero
			
			while (inFile.hasNext()){
                		String line = inFile.nextLine();
				for(int i = 0; i < line.length(); i++){
					index = (int) line.charAt(i);
					if(index >= 0 && index <= 255)
						charCountAry[index] += 1;	// calculating the frequency of each character
				}
               		}
			inFile.close();
		}
			

		public static void printCountAry(String fileName)throws IOException{
			BufferedWriter DebugFile = new BufferedWriter(new FileWriter(fileName,true));
		
			for(int i = 0; i < charCountAry.length; i++){
				if(charCountAry[i] > 0){
						DebugFile.write((char)i + "\t" + charCountAry[i] + "\r\n"); // (Chararacter,Character Count)
						DebugFile.flush();	
				}
			}
			DebugFile.write("\r\n"); // Moving to a new line while making sure that a carriage return is present
			DebugFile.close();
		}

		public static linkedList constructHuffmanLList(String fileName)throws IOException{
			BufferedWriter DebugFile = new BufferedWriter(new FileWriter(fileName,true)); 
			String str;
			int num;
			linkedList L = new linkedList();

			for(int i = 0; i < charCountAry.length; i++){
				if(charCountAry[i] > 0){
					str = Character.toString((char)i);
					num = charCountAry[i];
					TreeNode temp = new TreeNode(str,num,"",null, null, null);
					L.insertNewNode(L.findSpot(temp),temp);
					DebugFile.write(L.printList());
					DebugFile.flush();
				}
				
			}

			Root = L.listHead;
			DebugFile.write("\r\n");
			DebugFile.close();
			return L;		
		}

		public static void constructHuffmanBinTree(linkedList L, String fileName)throws IOException{
			BufferedWriter DebugFile = new BufferedWriter(new FileWriter(fileName,true)); 
			String new_chStr;
			int new_prob = 0;

			while(L.listHead.next.next != null){
			// Concatenating characters
				new_chStr = L.listHead.next.chStr;
				new_chStr += L.listHead.next.next.chStr;

			// Summing probabilities up
				new_prob = L.listHead.next.prob;
				new_prob += L.listHead.next.next.prob;

			// Creating new node
				TreeNode new_node = new TreeNode(new_chStr, new_prob, "", null, null, null);
			// Pointing the new node's left and right child to its original nodes
				new_node.left = L.listHead.next;
				new_node.right = L.listHead.next.next;

			// Inserting new node
				L.insertNewNode(L.findSpot(new_node), new_node);

			// Shifting the linked list head to the next next one
				L.listHead.next = L.listHead.next.next.next;

				DebugFile.write(L.printList());
				DebugFile.flush();
			}

			// Renewing the Root
			Root = L.listHead.next;
			DebugFile.close();
		}
		
		public static void preOrderTraversal(TreeNode Root, String fileName)throws IOException{
			if(Root == null)
				return;// do nothing
			else{
				String line = "(\"" + Root.chStr + "\", " + Integer.toString(Root.prob) + ") ";
				print_append(line, fileName);

				preOrderTraversal(Root.left,fileName);
				preOrderTraversal(Root.right,fileName);
			}
		}

		public static void postOrderTraversal(TreeNode Root, String fileName)throws IOException{
			if(Root == null)
				return;// do nothing
			else{
				postOrderTraversal(Root.left,fileName);
				postOrderTraversal(Root.right,fileName);

				String line = "(\"" + Root.chStr + "\", " + Integer.toString(Root.prob) + ") ";
				print_append(line, fileName);
			}
		}

		public static void inOrderTraversal(TreeNode Root, String fileName)throws IOException{
			if(Root == null)
				return;// do nothing
			else{
				inOrderTraversal(Root.left,fileName);

				String line = "(\"" + Root.chStr + "\", " + Integer.toString(Root.prob) + ") ";
				print_append(line, fileName);

				inOrderTraversal(Root.right,fileName);
			}
		}

		public static boolean isLeaf(TreeNode node){
			if(node.left == null && node.right == null)
				return true;
			else
				return false;
		}

		public static void constructCharCode(TreeNode T, String code){
			if(isLeaf(T)){
				T.code = code;
				char indexChar = (char)T.chStr.charAt(0);
				charCode[(int)indexChar] = code;
			}

			else{
				if(T.left != null)
					constructCharCode(T.left, code + "0");
				if(T.right != null)
					constructCharCode(T.right, code + "1");
			}
		}

		public static void print_append(String line, String fileName)throws IOException{
			//append at the end of the file
			BufferedWriter DebugFile = new BufferedWriter(new FileWriter(fileName,true));

			DebugFile.write(line);
			DebugFile.close();
		}
			
		
		public static void Encode(String inputFile,String outputFile) throws IOException{
			char charIn;
			int index;
			String code;

			Scanner inFile = new Scanner(new FileReader(inputFile));
			BufferedWriter outFile = new BufferedWriter(new FileWriter(outputFile));

			String firstLine = inFile.nextLine();

			for(int i = 0; i < firstLine.length(); i++){
					charIn = firstLine.charAt(i);
					index = (int)charIn;

					if(index >= 0 && index <= 255){ // Ascii range
						code = charCode[index];
						outFile.write(code);
						outFile.flush();
					}
			}

			while(inFile.hasNextLine()){
				String line = inFile.nextLine();
				

                		for(int i = 0; i < line.length(); i++){ 
				//Go to a new line
					if(i == 0){
						outFile.write("\r\n");
						outFile.flush();
					}
					charIn = line.charAt(i);
					index = (int)charIn;

					if(index >= 0 && index <= 255){ // Ascii range
						code = charCode[index];
						outFile.write(code);
						outFile.flush();
					}
				}
			}

			inFile.close();
			outFile.close();
		}

		public static void Decode(String inputFile, String outputFile)throws IOException{
			Scanner inFile = new Scanner(new FileReader(inputFile));
			BufferedWriter outFile = new BufferedWriter(new FileWriter(outputFile));

			char oneBit;
			String word;
			TreeNode Spot = Root;
			
			String firstLine = inFile.nextLine();
			for(int i = 0; i < firstLine.length(); i++){

					oneBit = firstLine.charAt(i);
					if(oneBit == '0'){
						Spot = Spot.left;
						if(isLeaf(Spot)){
							outFile.write(Spot.chStr);
							Spot = Root;
						}
					}
					if(oneBit == '1'){
						Spot = Spot.right;
						if(isLeaf(Spot)){
							outFile.write(Spot.chStr);
							Spot = Root;
						}
					}
			}

			while(inFile.hasNextLine()){
				String line = inFile.nextLine();
				for(int i = 0; i < line.length(); i++){
				//Go to a new line
					if(i == 0){
						outFile.write("\r\n");
						outFile.flush();
					}
					oneBit = line.charAt(i);
					if(oneBit == '0'){
						Spot = Spot.left;
						if(isLeaf(Spot)){
							outFile.write(Spot.chStr);
							Spot = Root;
						}
					}
					if(oneBit == '1'){
						Spot = Spot.right;
						if(isLeaf(Spot)){
							outFile.write(Spot.chStr);
							Spot = Root;
						}
					}
				}
			}

			if(Spot != Root){
				System.out.println("Error! The compress file contains invalid character!\n");
				System.exit(0);// Exit Program
			}

			inFile.close();
			outFile.close();
		}
		public static void userInterface()throws IOException{
			Scanner input = new Scanner(System.in);
			String nameOrg; // Input filename (original File)
			String nameCompress; // compress filename
			String nameDeCompress; //Decompress filename
			char yesNo;

			System.out.println("Hello there, would you like to compress a file?");
			System.out.println("Please type N for no, or anything else to begin");
			yesNo = input.next().charAt(0);

			if(yesNo == 'N' || yesNo == 'n')
				System.exit(0); // exit the program immediately
			else{
				while(yesNo != 'N' || yesNo != 'n'){
					System.out.println("Please enter the name of the input file to be compressed");
					nameOrg = input.next();
		
					nameCompress = nameOrg + "_Compressed";
					nameDeCompress = nameOrg + "_DeCompress";

					Encode(nameOrg,nameCompress);
					Decode(nameCompress,nameDeCompress);

					System.out.println("Hello there, would you like to compress another file?");
					System.out.println("Please type N for no, or anything else to begin");
					yesNo = input.next().charAt(0);

					if(yesNo == 'N' || yesNo == 'n')
						System.exit(0); // exit the program immediately
				}
			}
		}		
	}

	public static void main(String[] args){
		try{
			
			linkedList L;
			BinaryTree BT = new BinaryTree();

			String fake_code = "";
			String nameInFile = args[0];
			String nameDebugFile = nameInFile + "_DeBug";

			BT.computeCharCounts(nameInFile);
			BT.printCountAry(nameDebugFile);

			L = BT.constructHuffmanLList(nameDebugFile);

			BT.constructHuffmanBinTree(L,nameDebugFile);
			BT.constructCharCode(BT.Root,fake_code);
			
			BT.print_append("\n\nPre-Order Traversal:\n\n",nameDebugFile);
			BT.preOrderTraversal(BT.Root,nameDebugFile);

			BT.print_append("\n\nIn-Order Traversal:\n\n",nameDebugFile);
			BT.inOrderTraversal(BT.Root,nameDebugFile);

			BT.print_append("\n\nPost-Order Traversal:\n\n",nameDebugFile);
			BT.postOrderTraversal(BT.Root,nameDebugFile);

			BT.userInterface();
			
		}
		catch(IOException| IllegalArgumentException | NoSuchElementException e){}
	}

}