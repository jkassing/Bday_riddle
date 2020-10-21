public class Edge {
    int id_1;
    int id_2;
    int color;

    public Edge(int id_1, int id_2, int color){
        this.id_1 = id_1;
        this.id_2 = id_2;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public int getId_1() {
        return id_1;
    }

    public int getId_2() {
        return id_2;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
