package com.pineypiney.minecad

import com.pineypiney.game_engine.GameEngine
import com.pineypiney.game_engine.WindowI
import com.pineypiney.game_engine.resources.FileResourcesLoader

class MinecadEngine: GameEngine<MinecadLogic>(FileResourcesLoader("src/main/resources")) {
    override var TARGET_FPS: Int = 1000
    override val TARGET_UPS: Int = 20
    override val window: MinecadWindow = MinecadWindow.INSTANCE

    override val activeScreen: MinecadLogic = MinecadLogic(this)

    companion object{
        val INSTANCE = MinecadEngine()
    }
}