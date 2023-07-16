package com.pineypiney.minecad

import com.pineypiney.game_engine.GameLogic
import com.pineypiney.game_engine.WindowI
import com.pineypiney.game_engine.util.Cursor
import com.pineypiney.game_engine.util.input.InputState
import com.pineypiney.minecad.block.Block
import glm_.s
import glm_.vec2.Vec2
import glm_.vec2.Vec2i
import glm_.vec3.Vec3
import org.lwjgl.glfw.GLFW
import java.io.File

class MinecadLogic(override val gameEngine: MinecadEngine): GameLogic() {


    override val camera: MinecadCamera = MinecadCamera()
    override val renderer: MinecadRenderer = MinecadRenderer()

    val DEFAULT_CURSOR = Cursor(gameEngine, "textures/mouse_default.png", Vec2i(13, 13))
    val CLICKED_CURSOR = Cursor(gameEngine, "textures/mouse_clicked.png", Vec2i(13, 13))

    val block = Block()
    val checkPoint = Vec3(0)

    var primary = false
    var secondary = false

    override fun init() {
        super.init()
        block.init()

        window.setCursor(DEFAULT_CURSOR)
    }

    override fun addObjects() {
        add(block)
    }

    override fun render(window: WindowI, tickDelta: Double) {
        renderer.render(window, this, tickDelta)
    }

    override fun onInput(state: InputState, action: Int): Int {
        super.onInput(state, action)

        return when(state.i){
            GLFW.GLFW_KEY_ESCAPE -> {
                window.shouldClose = true
                -1
            }
            GLFW.GLFW_KEY_E -> {
                export()
                -1
            }
            else -> action
        }
    }

    override fun onPrimary(window: WindowI, action: Int, mods: Byte) {
        primary = action != 0
        window.setCursor(if(primary) CLICKED_CURSOR else DEFAULT_CURSOR)
    }

    override fun onSecondary(window: WindowI, action: Int, mods: Byte) {
        secondary = action != 0
    }

    override fun onCursorMove(cursorPos: Vec2, cursorDelta: Vec2) {
        super.onCursorMove(cursorPos, cursorDelta)

        when {
            primary -> camera.spin(cursorDelta * 5)
            secondary -> camera.pan(-cursorDelta * 5)
        }
        if(primary || secondary) camera.updateCameraVectors()
    }

    override fun onScroll(scrollDelta: Vec2): Int {
        super.onScroll(scrollDelta)
        camera.zoom(scrollDelta.y * 0.3f)
        return 0
    }

    private fun export(){
        val file = File("model.stl")
        if(!file.exists()) file.createNewFile()

        file.writeBytes(block.getBytes())
    }

    override fun cleanUp() {
        super.cleanUp()

        DEFAULT_CURSOR.delete()
        CLICKED_CURSOR.delete()
    }
}