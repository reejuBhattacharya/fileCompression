import java.util.LinkedList;
import java.util.Queue;

public class Decoder {
    HuffmanNode tree;
    String serializedTree;
    String encodedText;

    Decoder(String _serializedTree, String _encodedText) {
        this.serializedTree = _serializedTree;
        this.encodedText = _encodedText;
    }    

    private void deserializeTree() {
        String[] tokens = serializedTree.split("\\|");
        HuffmanNode root = parseNode(tokens[0]);
        Queue<HuffmanNode> q = new LinkedList<>();
        q.add(root);
        for(int i=1; i<tokens.length; i++) {
            HuffmanNode left = parseNode(tokens[i++]);
            HuffmanNode right = parseNode(tokens[i]);
            HuffmanNode curr = q.poll();
            curr.setLeaves(left, right);
            if(left != null) q.add(left);
            if(right != null) q.add(right);
        }
        tree = root;
    }

    public String decodeText() {
        deserializeTree();
        int i = 0;
        HuffmanNode temp = tree;
        StringBuilder str = new StringBuilder();
        while(i<encodedText.length()) {
            char c = encodedText.charAt(i);
            if(c=='0')
                temp = temp.getLeftLeaf();
            else 
                temp = temp.getRightLeaf();
            if(temp.getData() != '-') {
                str.append(temp.getData());
                temp = tree;
            }
            i++;
        }
        return str.toString();
    }


    private HuffmanNode parseNode(String str) {
        if(str.equals("*"))
            return null;
        StringBuilder value = new StringBuilder("");
        int i = 0;
        while(i<str.length() && Character.isDigit(str.charAt(i))) {
            value.append(str.charAt(i));
            i++;
        }
        char data = str.charAt(i);
        return new HuffmanNode(Integer.parseInt(value.toString()), data); 
    }

}
