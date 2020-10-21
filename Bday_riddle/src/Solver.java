import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Solver {
    List<Edge> allEdges;
    int[][] matrix; // matrix holding all possible color compositions
    int num_of_nobs;

    public Solver(int num_of_nobs){
        this.allEdges = new ArrayList<>();
        for(int i = 0; i < num_of_nobs; i++){
            for(int j = i; j < num_of_nobs; j++){
                if(i != j){
                    allEdges.add(new Edge(i, j, 0));
                }
            }
        }
        this.num_of_nobs = num_of_nobs;
        this.matrix = generateMatrix(allEdges.size());
    }

    // generate all possible color compositions
    // 0 = red, 1 = blue
    // each row is one coloration of the edges
    private int[][] generateMatrix(int n){
        int rows = (int) Math.pow(2,n);
        int[][] matrix = new int[rows][n];
        int interval = rows;
        for(int i = 0; i < n; i++){
            interval = interval/2;
            boolean red=true;
            int index = 0;
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
                    index = 1;
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

    // changes color of an edge
    private void changeOneColor(int index, int color){
        allEdges.get(index).setColor(color);
    }

    // get all edges of a node with specified color
    private List<Edge> getEdgesOfColorForNode(int id, int color){
        List<Edge> result = new ArrayList<>();
        for(Edge edge: allEdges){
            if((edge.getId_1() == id || edge.getId_2() == id) && edge.getColor() == color){
                result.add(edge);
            }
        }
        return result;
    }

    // get an edge connecting two nodes
    private Edge getEdge(int id1, int id2){
        for(Edge edge: allEdges){
            if((edge.getId_1() == id1 && edge.getId_2() == id2) || (edge.getId_1() == id2 && edge.getId_2() == id1)){
                return edge;
            }
        }
        return null;
    }

    // returns whether at least one node is new
    private boolean validEdge(List<Integer> nobs, Edge edge){
        return !nobs.contains(edge.id_1) || !nobs.contains(edge.id_2);
    }

    // returns id of the new nob
    private int getNewNob(List<Integer> nobs, Edge edge){
        if(!nobs.contains(edge.id_1)) {
            return edge.id_1;
        }
        else{
            return edge.id_2;
        }
    }



    // tests if a 4 colored cycle can be found
    // can also return solution as path if needed
    private boolean testForSolution(){
        // list of edges in solution
        List<Edge> path = new ArrayList<>();
        // list of nobs in solution
        List<Integer> nobs = new ArrayList<>();
        int nob1, nob2, nob3, nob4;
        int color;
        for(Edge edge_1 : allEdges){
            nob1 = edge_1.id_1;
            path.add(edge_1);
            nobs.add(nob1);
            color = edge_1.color;
            if(getEdgesOfColorForNode(nob1, color).size() > 1){
                nob2 = edge_1.id_2;
                nobs.add(nob2);
                for(Edge edge_2 : getEdgesOfColorForNode(nob2, color)){
                    if(!path.contains(edge_2) && validEdge(nobs, edge_2)){
                        nob3 = getNewNob(nobs, edge_2);
                        path.add(edge_2);
                        nobs.add(nob3);
                        for(Edge edge_3 : getEdgesOfColorForNode(nob3, color)){
                            if(!path.contains(edge_3) && validEdge(nobs, edge_3)){
                                path.add(edge_3);
                                nob4 = getNewNob(nobs, edge_3);
                                if(Objects.requireNonNull(getEdge(nob4, nob1)).color == color){
                                    path.add(getEdge(nob4, nob1));
                                    return true;
                                }
                                path.remove(edge_3);
                            }
                        }
                        nobs.remove(2);
                        path.remove(edge_2);
                    }
                }
                nobs.remove(1);
            }
            path.remove(edge_1);
            nobs.remove(0);
        }
        return false;
    }

    // tests if each coloration has a solution
    private boolean solve(){
        int compositions = (int) Math.pow(2, allEdges.size());
        for(int i = 0; i < compositions; i++){
            for(int j = 0; j < allEdges.size(); j++){
                changeOneColor(j, matrix[i][j]);
            }
            if(!testForSolution()){
                return false;
            }
        }
        return true;
    }

    // ramps up number of nobs until solved
    public static void main(String[] args) {
        int i = 4;
        while(true){
            Solver solver = new Solver(i);
            if(solver.solve()){
                System.out.println("YAY solution is: " +i);
                return;
            }
            i++;
        }
    }

}



