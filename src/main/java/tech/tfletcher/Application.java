package tech.tfletcher;

import tech.tfletcher.engine.GameEngine;
import tech.tfletcher.engine.IGameLogic;
import tech.tfletcher.engine.TestGame;

class Application {
    private static final int W_WIDTH = 600;
    private static final int W_HEIGHT = 600;

    private static final String W_NAME = "GLFW Window";

    public static void main(String[] args){
        try{
            IGameLogic gameLogic = new TestGame();
            GameEngine gameEngine = new GameEngine(W_NAME, W_WIDTH, W_HEIGHT, gameLogic);
            gameEngine.start();

        }catch(Exception e){
            e.printStackTrace();
            System.exit(-1);
        }


    }
}