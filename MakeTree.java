import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class MakeTree {
    private String src;

    MakeTree(String _src) {
        this.src = _src;
    }

    public HashMap<Character, Integer> getFrequency() {
        HashMap<Character, Integer> map = new HashMap<>();
        for(char c: src.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0)+1);
        }

        return map;
    }

    public HuffmanNode getHuffmanTree() {
        HashMap<Character, Integer> freq = getFrequency();
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>((a,b) -> a.getValue()-b.getValue());
        for(char key: freq.keySet()) {
            pq.add(new HuffmanNode(freq.get(key), key));
        }
        while(pq.size()>1) {
            HuffmanNode node1 = pq.poll();
            HuffmanNode node2 = pq.poll();

            HuffmanNode new_node = new HuffmanNode(node1.getValue()+node2.getValue(), '-');
            new_node.setLeaves(node1, node2);
            pq.add(new_node);
        }
        return pq.poll();
    }

    public String getSerializedTree() {
        HuffmanNode root = getHuffmanTree();
        StringBuilder str = new StringBuilder("");
        Queue<HuffmanNode> q = new LinkedList<>();
        q.add(root);
        while(!q.isEmpty()) {
            HuffmanNode node = q.poll();
            if(node==null) {
                str.append("*|");
                continue;
            }

            str.append(node.getValue()+""+node.getData()+"|");

            q.add(node.getLeftLeaf());
            q.add(node.getRightLeaf());
        }
        
        return str.toString();
    }
}
