package com.pineypiney.minecad

import com.pineypiney.game_engine.rendering.cameras.PerspectiveCamera
import glm_.d
import glm_.glm
import glm_.pow
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.cos

class MinecadCamera: PerspectiveCamera(MinecadWindow.INSTANCE) {

    private var lookingAt: Vec3 = Vec3()
    private var lookDist = 5f

    fun spin(cursorDelta: Vec2){
        look(cursorDelta * 20, false)
        cameraPos = lookingAt - (cameraFront * lookDist)
    }

    fun pan(cursorDelta: Vec2){
        val move = cameraUp * cursorDelta.y + cameraRight * cursorDelta.x
        translate(move)
        lookingAt plusAssign move
    }

    fun look(cursorDelta: Vec2, setLook: Boolean = true){
        cameraYaw += cursorDelta.x
        increasePitch(cursorDelta.y)
        updateCameraVectors()
        if(setLook) lookingAt = cameraPos + (cameraFront * lookDist)
    }

    fun zoom(scroll: Float){
        lookDist = (lookDist * 2f.pow(-scroll)).coerceIn(0.2f, 10f)
        cameraPos = lookingAt - (cameraFront * lookDist)
    }

    private fun increasePitch(p: Float){
        cameraPitch = (cameraPitch + p).coerceIn(-90.0, 90.0)
    }

    companion object{
        fun vectorToEuler(vector: Vec3): Pair<Double, Double>{
            val pitch = asin(vector.y.d)
            val yaw = asin(vector.x / cos(pitch))
            return Math.toDegrees(pitch) to Math.toDegrees(yaw)
        }
    }
}