import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if(args.length < 3) {
            System.out.println("Insufficient number of arguments");
            System.out.println("Arguments should be in the order: <flag> [sourceFile] [destFile]");
            return;
        }
        Integer flag = null;
        try {
            flag = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.out.println("flag could not be parsed"+e.getMessage());
        }

        String srcFilePath = args[1];
        String destFilePath = args[2];

        if(flag==0) {
            createCompressedFile(srcFilePath, destFilePath);
        } else if(flag==1) {
            decodeCompressedFile(srcFilePath, destFilePath);
        } else {
            System.out.println("Wrong flag");
            return;
        }

        // compression
        // createCompressedFile(srcFilePath, destFilePath);

        // decompression
        // decodeCompressedFile(destFilePath, "test1.txt");

    }

    public static void decodeCompressedFile(String srcFilePath, String destFilePath) {
        StringBuilder str = new StringBuilder("");
        try {
            FileReader reader = new FileReader(srcFilePath);
            BufferedReader br = new BufferedReader(reader);

            String line = br.readLine();
            while (line != null) {
                str.append(line);
                str.append(System.lineSeparator());
                line = br.readLine();
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder treeLenStr = new StringBuilder("");
        StringBuilder paddingLenStr = new StringBuilder("");
        int i = 0;
        for(; i<str.length(); i++) {
            if(str.charAt(i) == '|') {
                i++;
                break;
            }
            treeLenStr.append(str.charAt(i));
        }
        for(; i<str.length(); i++) {
            if(str.charAt(i) == '|') {
                i++;
                break;
            }
            paddingLenStr.append(str.charAt(i));
        }
        int treeLen = Integer.parseInt(treeLenStr.toString());
        int paddingLen = Integer.parseInt(paddingLenStr.toString()) + System.lineSeparator().length()*8;
        String tree = str.substring(i, i+treeLen);
        String comp_enc_text = str.substring(i+treeLen);
        String encoded_text = decompressor(comp_enc_text);
        encoded_text = encoded_text.substring(0, encoded_text.length()-paddingLen);
        Decoder decoder = new Decoder(tree, encoded_text);
        String decoded_text = decoder.decodeText();
        
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(destFilePath));
            writer.write(decoded_text);
            writer.close();
            System.out.println("Successfully wrote to "+destFilePath);
        } catch (IOException e) {
            System.err.println("Something went wrong while printing to file");
            System.err.println(e);
        }
    }

    public static void createCompressedFile(String srcFilePath, String destFilePath) {
        StringBuilder str = new StringBuilder("");
        try {
            FileReader reader = new FileReader(srcFilePath);
            BufferedReader br = new BufferedReader(reader);

            String line = br.readLine();
            while (line != null) {
                str.append(line);
                str.append(System.lineSeparator());
                line = br.readLine();
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String unencoded_text = str.toString();
        MakeTree tree = new MakeTree(unencoded_text);
        String serialized_tree = tree.getSerializedTree();
        Encoder encoder = new Encoder(unencoded_text, tree.getHuffmanTree());
        String encoded_text = encoder.encodeText();
        String comp_enc_text = compressor(encoded_text);
        int paddingLen = 8 - (encoded_text.length()%8);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(destFilePath));
            writer.write(serialized_tree.length() + "|");
            writer.write(paddingLen + "|");
            writer.write(serialized_tree);
            writer.write(comp_enc_text);
            writer.close();
            System.out.println("Successfully wrote to "+destFilePath);
        } catch (IOException e) {
            System.err.println("Something went wrong while printing to file");
            System.err.println(e);
        }
    }

    public static String compressor(String s) {
        StringBuilder str = new StringBuilder();
        for(int i=0; i<s.length(); i+=8) {
            String chunk = s.substring(i, Math.min(i+8, s.length()));
            int l = chunk.length();
            if(l<8) {
                for(int j=0; j<8-l; j++)   chunk += "0";
            }
            char c = (char) Integer.parseInt(chunk, 2);
            str.append(c);
        }
        return str.toString();
    }

    public static String decompressor(String s) {
        StringBuilder str = new StringBuilder("");
        for(int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            String bin = Integer.toBinaryString((int) c);
            int l = bin.length();
            for(int j=0; j<8-l; j++)
                bin = "0"+bin;
            str.append(bin);
        }
        return str.toString();
    }

}
