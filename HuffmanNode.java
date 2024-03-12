class HuffmanNode {
    private char data = '-';
    private int value;
    private HuffmanNode left = null;
    private HuffmanNode right = null;

    HuffmanNode(int _value) {
        this.value = _value;
    }

    HuffmanNode(int _value, char _data) {
        this.value = _value;
        this.data = _data;
    }

    public void setLeaves(HuffmanNode _left, HuffmanNode _right) {
        this.left = _left;
        this.right = _right;
    }

    public void setData(char _data) {
        this.data = _data;
    }

    public int getValue() { return this.value; }

    public char getData() { return this.data; }

    public HuffmanNode getLeftLeaf() { return this.left; }

    public HuffmanNode getRightLeaf() { return this.right; }

}