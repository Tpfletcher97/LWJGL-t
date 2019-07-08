package tech.tfletcher.engine;

import tech.tfletcher.engine.Utility.MouseInput;
import tech.tfletcher.engine.Utility.Timer;
import tech.tfletcher.engine.rendering.Window;

import java.lang.Runnable;

public class GameEngine implements Runnable {
    private final IGameLogic gameLogic;
    private final Window window;

    private final Thread gameLoopThread;

    private final Timer timer;

    private final MouseInput mInput;

    public GameEngine(String windowTitle, int width, int height, IGameLogic gameLogic) throws Exception{
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(width, height, windowTitle);
        this.gameLogic = gameLogic;

        timer = new Timer();

        mInput = new MouseInput();
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
        mInput.init(window);
        timer.init();
    }

    protected void cleanup(){
        window.close();
        gameLogic.cleanup();
    }

    protected void loop(){
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / 30;

        boolean running = true;
        while(running && !window.windowShouldClose()){
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while(accumulator >= interval){
                update(interval, mInput);
                accumulator -= interval;
            }

            render();
        }

    }

    protected  void input(){
        mInput.input(window);
        gameLogic.input(window, mInput);
    }

    protected void update(float interval, MouseInput mInput){
        gameLogic.update(interval, mInput);
    }

    protected void render(){
        gameLogic.render(window);
        window.update();
    }
}
