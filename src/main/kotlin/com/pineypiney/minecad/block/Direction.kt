package com.pineypiney.minecad.block

import glm_.vec3.Vec3

enum class Direction(val vector: Vec3, val getComponent: (Vec3) -> Float) {
    UP(Vec3(0, 1, 0), Vec3::y),
    DOWN(Vec3(0, -1, 0), Vec3::y),
    FORWARD(Vec3(0, 0, 1), Vec3::z),
    BACKWARD(Vec3(0, 0, -1), Vec3::z),
    LEFT(Vec3(-1, 0, 0), Vec3::x),
    RIGHT(Vec3(1, 0, 0), Vec3::x);

    val sign: Float get() = getComponent(vector)

    fun right(): Direction{
        return when(this){
            UP -> RIGHT
            DOWN -> RIGHT
            FORWARD -> RIGHT
            BACKWARD -> LEFT
            LEFT -> FORWARD
            RIGHT -> BACKWARD
        }
    }

    fun up(): Direction{
        return when(this){
            UP -> BACKWARD
            DOWN -> FORWARD
            FORWARD -> UP
            BACKWARD -> UP
            LEFT -> UP
            RIGHT -> UP
        }
    }

    fun opp(): Direction{
        return when(this){
            UP -> DOWN
            DOWN -> UP
            FORWARD -> BACKWARD
            BACKWARD -> FORWARD
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }
}