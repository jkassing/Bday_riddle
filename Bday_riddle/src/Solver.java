import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solver {
    List<Edge> allEdges;
    List<Nob> allNobs;
    int[][] matrix;
    int num_of_nobs;

    public Solver(int num_of_nobs){
        this.allNobs = new ArrayList<>();
        this.allEdges = new ArrayList<>();
        for(int i = 0; i < num_of_nobs; i++){
            Nob nob = new Nob(i, num_of_nobs);
            allNobs.add(nob);
            for(int j = i; j < num_of_nobs; j++){
                if(i != j){
                    allEdges.add(new Edge(i, j, 0));
                }
            }
        }
        this.num_of_nobs = num_of_nobs;
        this.matrix = generateMatrix(allEdges.size());
    }

    public int[][] generateMatrix(int n){
        int rows = (int) Math.pow(2,n);
        int[][] matrix = new int[rows][n];
        //System.out.println(rows);
        int interval = rows;
        int index = 0;
        boolean red = true;
        for(int i = 0; i < n; i++){
            interval = interval/2;
            index = 0;
            for(int j = 0; j < rows; j++){
                if(index < interval){
                    index++;
                    if(red){
                        matrix[j][i] = 0;
                    }
                    else{
                        matrix[j][i] = 1;
                    }
                }
                else{
                    index = 0;
                    red = !red;
                    if(red){
                        matrix[j][i] = 0;
                    }
                    else{
                        matrix[j][i] = 1;
                    }
                }
            }
        }
        //System.out.println(Arrays.deepToString(matrix));
        return matrix;
    }

    public boolean solve(){
        int compositions = (int) Math.pow(2, num_of_nobs);
        for(int i = 0; i < compositions; i++){
            for(int j = 0; j < num_of_nobs; j++){
                changeOneColor(j, matrix[i][j]);
            }
            if(!testForSolution().isEmpty()){
                return false;
            }
        }
        return true;
    }


    // changes color to blue for an Egde
    public void changeOneColor(int index, int color){
        allEdges.get(index).setColor(color);
    }

    public List<Edge> getEdgesOfColorForNode(int id, int color){
        List<Edge> result = new ArrayList<>();
        for(Edge edge: allEdges){
            if((edge.getId_1() == id || edge.getId_2() == id) && edge.getColor() == color){
                result.add(edge);
            }
        }
        return result;
    }

    public boolean containsEdge(List<Edge> edges, int id1, int id2){
        for(Edge edge : edges){
            if((edge.getId_1() == id1 && edge.getId_2() == id2) || (edge.getId_1() == id2 && edge.getId_2() == id1)){
                return true;
            }
        }
        return false;
    }

    public Edge getEdge(int id1, int id2){
        for(Edge edge: allEdges){
            if((edge.getId_1() == id1 && edge.getId_2() == id2) || (edge.getId_1() == id2 && edge.getId_2() == id1)){
                return edge;
            }
        }
        return null;
    }

    public boolean validEdge(List<Integer> nobs, Edge edge){
        return !nobs.contains(edge.id_1) || !nobs.contains(edge.id_2);
    }


    public List<Edge> testForSolution(){
        List<Edge> path = new ArrayList<>();
        List<Integer> nobs = new ArrayList<>();
        for(Edge edge_1 : allEdges){
            path.add(edge_1);
            int nob1 = edge_1.id_1;
            nobs.add(nob1);
            int color = edge_1.color;
            List<Edge> edgesWColor = getEdgesOfColorForNode(nob1, color);
            if(edgesWColor.size() > 1){
                int nob2 = edge_1.id_2;
                nobs.add(nob2);
                for(Edge edge_2 : getEdgesOfColorForNode(nob2, color)){
                    int nob3;
                    if(!path.contains(edge_2) && validEdge(nobs, edge_2)){
                        path.add(edge_2);
                        if(edge_2.id_1 != nob2) {
                            nob3 = edge_2.id_1;
                        }
                        else{
                            nob3 = edge_2.id_2;
                        }
                        nobs.add(nob3);
                        for(Edge edge_3 : getEdgesOfColorForNode(nob3, color)){
                            int nob4;
                            if(!path.contains(edge_3) && validEdge(nobs, edge_3)){
                                path.add(edge_3);
                                if(edge_3.id_1 != nob3) {
                                    nob4 = edge_3.id_1;
                                }
                                else {
                                    nob4 = edge_3.id_2;
                                }
                                if(getEdge(nob4, nob1).color == color){
                                    path.add(getEdge(nob4, nob1));
                                    return path;
                                }
                                path.remove(edge_3);
                            }

                        }
                        path.remove(edge_2);

                    }
                }
                path.remove(edge_1);
            }
        }
        return path;
    }

    public static void main(String[] args) {
        int i = 4;
        while(true){
            System.out.println(i);
            Solver solver = new Solver(i);
            if(solver.solve()){
                System.out.println("YAY");
                System.out.println(i);
                return;
            }
            i++;
        }



    }

}



