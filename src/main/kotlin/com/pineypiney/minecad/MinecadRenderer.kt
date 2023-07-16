package com.pineypiney.minecad

import com.pineypiney.game_engine.WindowI
import com.pineypiney.game_engine.objects.ObjectCollection
import com.pineypiney.game_engine.rendering.GameRenderer
import com.pineypiney.game_engine.util.GLFunc
import com.pineypiney.minecad.block.Block

class MinecadRenderer: GameRenderer<MinecadLogic>() {
    override val window: MinecadWindow = MinecadWindow.INSTANCE

    override fun init() {
        GLFunc.depthTest = true
    }

    override fun render(window: WindowI, game: MinecadLogic, tickDelta: Double) {
        clear()
        game.block.render(game.camera.getView(), game.camera.getProjection(), tickDelta)
    }

    override fun updateAspectRatio(window: WindowI, objects: ObjectCollection) {
        GLFunc.viewportO = window.size
    }

    override fun delete() {
    }
}