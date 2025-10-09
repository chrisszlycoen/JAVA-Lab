package Activity;

// 9. Overloading a constructor
// Create a class Box and give it properties width, height, depth of type integers.
// Create a class BoxWeight which has an additional property weight of the box.
// Create a main App class that helps to Create Object of the two classes.

class Box {
    int width;
    int height;
    int depth;
    public Box(){};
    public Box (int w, int h, int d){
        this.width = w;
        this.height = h;
        this.depth = d;
    }
}

class BoxWeight extends Box{
    float weight;
    public BoxWeight(){};
    public BoxWeight(int width, int height, int depth, float weight){
        super(width,height,depth);
        this.weight = weight;
    };
}

class App{
    public static void main(String[] args) {
    Box b1 = new Box(12,23,34);
    BoxWeight bw = new BoxWeight(12, 23, 34, 12);
    System.out.println("Box dimensions: " + b1.width + "x" + b1.height + "x" + b1.depth);
    System.out.println("BoxWeight dimensions: " + bw.width + "x" + bw.height + "x" + bw.depth + ", Weight: " + bw.weight);
    }
}