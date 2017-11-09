import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Hashtable;
import java.util.List;
public class hashtagcounter {

	public static void main(String[] args) throws IOException {
		fibonacci H = new fibonacci ();
		Node newMax = null;
		int datafield;
		String fileName = args[0];
		String fileName1="output_file.txt";

		// This will reference one line at a time
		String[] hashtags = new String[20000];
		String line = null;
		int freq[]= new int[20000];
		int query=0;	
		Hashtable<String,Node> hm=new Hashtable<String,Node>(); 
		try {
			// FileReader reads text files 
			FileReader fileReader = 
					new FileReader(fileName);
			//Wrapping FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			//FileWriter writes text files
			FileWriter fileWriter = new FileWriter(fileName1);
			//Wrapping FileWriter in BufferedWriter.
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			int i=0;
			//Processing the input and saving the hashtags and their respective frequencies in 2 seperate arrays
			while((line = bufferedReader.readLine()) != null && !line.startsWith("STOP")) {
				if(line.startsWith("#")){
					String[] var1s = line.split("\\s+");
					hashtags[i]=var1s[0].substring(1);
					freq[i]=Integer.parseInt(var1s[1]);
					i=i+1;
				} 
				else{
					query = Integer.parseInt(line); 
					for(int k=0;k<i;k++){
						if (hm.containsKey(hashtags[k])){ //checks if the hashtag is already present in the hashmap or not
							Node f=hm.get(hashtags[k]);  
							int newfreq=f.datafield+freq[k];
							H.increaseKey(f,newfreq); // if already present, increase the key (frequency here) in the fibonacci heap
							hm.remove(hashtags[k]);
							hm.put(hashtags[k],f);
						}
						else {
							Node n= new Node(hashtags[k],freq[k]);
							H.insert(n,freq[k]); // if not present, just insert the new node into the fibonacci heap
							hm.put(hashtags[k], n); //includes the new entry into the hashmap
						}

					}
					i=0;
					Arrays.fill(hashtags, null);
					Arrays.fill(freq, 0);
					 List<Node> removedNodes = new ArrayList<Node>(query);

					for(int s=0;s < query; s++) { // perform removeMax operation whenever a query occurs
						newMax = H.removeMax();
						removedNodes.add(newMax);	
						hm.remove(newMax.getHashtag()); // removes the hashtag from the hashmap
						if(s<query-1){
						//System.out.print(newMax.getHashtag()+","); //print hashtags of the maximum frequency nodes into the console
						bufferedWriter.write(newMax.getHashtag()+",");  //writes hashtags of the maximum frequency nodes into the output text file
					}
						else{
							//System.out.print(newMax.getHashtag()); 
							bufferedWriter.write(newMax.getHashtag()); 
						}
					}
					bufferedWriter.newLine();
						
					for(int s=0;s < query; s++) {
						Node n = new Node(removedNodes.get(s).getHashtag(),removedNodes.get(s).getData());
						H.insert(n, n.getData());
						hm.remove(n.getHashtag());
						hm.put(n.getHashtag(), n);
					}
					//System.out.println("\n");					
									}
			
			}
			bufferedReader.close();
		bufferedWriter.close();
		}//catch block
		catch(FileNotFoundException ex) {
			System.out.println(
					"Unable to open file '" + 
							fileName + "'");                
		}
		catch(IOException ex) {
			System.out.println(
					"Error reading or writing file '" 
							+ fileName + "'");                  
			// Or we could just do this: 
			// ex.printStackTrace();
		}
	}
}


	



