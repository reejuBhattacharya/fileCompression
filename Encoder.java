import java.util.HashMap;

public class Encoder {
    private String text;
    private HuffmanNode tree;

    Encoder(String _text, HuffmanNode _tree) {
        this.text = _text;
        this.tree = _tree;
    }

    public HashMap<Character, String> getCodes() {
        HashMap<Character, String> map = new HashMap<>();
        getCodesHelper(tree, map, new StringBuilder(""));
        return map;
    }

    private void getCodesHelper(HuffmanNode node, HashMap<Character, String> map, StringBuilder str) {
        if(node==null)
            return;
        
        if(node.getData()!='-') {
            map.put(node.getData(), str.toString());
            return;
        } else {
            str.append("0");
            getCodesHelper(node.getLeftLeaf(), map, str);
            str.deleteCharAt(str.length()-1);
            str.append("1");
            getCodesHelper(node.getRightLeaf(), map, str);
            str.deleteCharAt(str.length()-1);
        }
    }

    public String encodeText() {
        StringBuilder str = new StringBuilder("");
        HashMap<Character, String> codes = getCodes();
        for(int i=0; i<text.length(); i++) {
            char c = text.charAt(i);
            str.append(codes.get(c));
        }
        return str.toString();
    }
}
