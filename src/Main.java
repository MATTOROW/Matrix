
public class Main {
    public static void main(String[] args) {
//        Matrix a = new Matrix();
//        Matrix b = new Matrix();
//        a.input();
//        b.input();
//        Matrix c = a.multiplicationMatrix(b);
//        c.printMatrix();
//        Matrix d = a.sumMatrix(b);
//        d.printMatrix();
        Matrix e = new Matrix();
        e.input();
        System.out.println(e);
        Matrix f = new Matrix();
        f.input();
        e.solve(f).printMatrix();
    }
}