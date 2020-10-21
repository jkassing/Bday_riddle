import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solver {
    List<Edge> allEdges;
    List<Nob> allNobs;
    int[][] matrix;

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
        this.matrix = generateMatrix(allEdges.size());
    }

    public int[][] generateMatrix(int n){
        int rows = (int) Math.pow(2,n);
        int[][] matrix = new int[rows][n];
        System.out.println(rows);
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
        System.out.println(Arrays.deepToString(matrix));
        return matrix;
    }

    public boolean solve(){

        for(int i = 0; i < allEdges.size(); i++){
            changeOneColor(i);
            if(!testForSolution()){
                return false;
            }
            for(int j = 0; j < allEdges.size(); j++){
                if(j != i){
                    changeOneColor(j);
                    if(!testForSolution()){
                        return false;
                    }
                }
                resetColors();
            }
            resetColors();
        }
        return true;
    }

    public void resetColors(){
        for(Edge edge : allEdges){
            edge.setColor(0);
        }
    }

    // changes color to blue for an Egde
    public void changeOneColor(int index){
        allEdges.get(index).setColor(1);
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


    public boolean testForSolution(){
        for(Edge edge_1 : allEdges){
            int nob1 = edge_1.id_1;
            int color = edge_1.color;
            List<Edge> edgesWColor = getEdgesOfColorForNode(nob1, color);
            if(edgesWColor.size() > 1){
                int nob2 = edge_1.id_2;
                for(Edge edge_2 : getEdgesOfColorForNode(nob2, color)){
                    int nob3;
                    if(!edge_1.equals(edge_2)){
                        if(edge_2.id_1 != nob2) {
                            nob3 = edge_2.id_1;
                        }
                        else {
                            nob3 = edge_2.id_2;
                        }
                        for(Edge edge_3 : getEdgesOfColorForNode(nob3, color)){
                            int nob4;
                            if(!edge_3.equals(edge_2)){
                                if(edge_3.id_1 != nob3) {
                                    nob4 = edge_3.id_1;
                                }
                                else {
                                    nob4 = edge_3.id_2;
                                }

                                if(containsEdge(getEdgesOfColorForNode(nob4, color), nob4, nob1)){
                                    return true;
                                }
                            }

                        }

                    }
                }

            }
        }
        return false;
    }

    public static void main(String[] args) {
        int i = 4;
        while(true){
            Solver solver = new Solver(i);
            if(solver.solve()){
                System.out.println(i);
                return;
            }
            i++;
        }



    }

}



