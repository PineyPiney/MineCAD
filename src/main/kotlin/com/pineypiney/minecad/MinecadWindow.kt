package com.pineypiney.minecad

import com.pineypiney.game_engine.Window
import com.pineypiney.game_engine.util.input.DefaultInput
import com.pineypiney.game_engine.util.input.Inputs
import glm_.vec2.Vec2i
import org.lwjgl.glfw.GLFW

class MinecadWindow: Window("MineCAD", 960, 540, false, true, hints) {
    override val input: Inputs = DefaultInput(this)

    companion object{
        val hints = defaultHints + mapOf(
            GLFW.GLFW_CONTEXT_VERSION_MAJOR to 3,
            GLFW.GLFW_CONTEXT_VERSION_MINOR to 3,
        )

        val INSTANCE = MinecadWindow()
    }
}