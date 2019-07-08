package tech.tfletcher.engine;

import tech.tfletcher.engine.Utility.MouseInput;
import tech.tfletcher.engine.rendering.Window;

public interface IGameLogic{
    void init() throws Exception;

    void input(Window window, MouseInput mInput);

    void update(float interval, MouseInput mInput);

    void render(Window window);

    void cleanup();
}