package tech.tfletcher.engine;

import java.lang.Runnable;

public class GameEngine implements Runnable {
    private final IGameLogic gameLogic;
    private final Window window;

    private final Thread gameLoopThread;

    private final Timer timer;

    public GameEngine(String windowTitle, int width, int height, IGameLogic gameLogic) throws Exception{
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(width, height, windowTitle);
        this.gameLogic = gameLogic;

        timer = new Timer();
    }

    public void start(){
        gameLoopThread.start();
    }

    @Override
    public void run(){
        try{
            init();
            loop();
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            cleanup();
        }
    }

    public void init() throws Exception{
        window.init();
        gameLogic.init();
    }

    protected void cleanup(){
        gameLogic.cleanup();
    }

    protected void loop(){
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / 30;

        while(!window.windowShouldClose()){
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            gameLogic.input(window);

            while(accumulator >= interval){
                update(interval);
                accumulator -= interval;
            }

            render();
        }

    }

    protected void update(float interval){
        gameLogic.update(interval);
    }

    protected void render(){
        gameLogic.render(window);
    }
}
