/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acoalgorithm;

/**
 *
 * @author Vaehnor
 */
public class Launcher {
    public static void main(String[] args) {
        AntColonyOptimization aco = new AntColonyOptimization(0, 1, 5, 0.5, 500, 0.8, 0.01, 1000, 10);
        aco.solve();
    }
}
