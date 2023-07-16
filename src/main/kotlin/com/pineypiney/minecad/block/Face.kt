package com.pineypiney.minecad.block

import com.pineypiney.game_engine.util.maths.shapes.AxisAlignedCuboid
import com.pineypiney.game_engine.util.maths.shapes.Rect3D
import glm_.glm.abs
import glm_.vec2.Vec2
import glm_.vec3.Vec3

class Face(val center: Vec3, val normal: Direction, val extents: Vec2) {

    constructor(center: Vec3, normal: Direction, size: Vec3): this(center, normal, extentsFromSize(normal, size))

    val rv = normal.right().vector * extents.x
    val uV = normal.up().vector * extents.y

    val n = normal.getComponent
    val r = normal.right().getComponent
    val u = normal.up().getComponent

    fun getVertices(): List<Float>{
        val corners = corners()
        val d = getData(normal.vector) + floatArrayOf(0f, 0f)
        return (getData(corners[0]) + d +
                getData(corners[1]) + d +
                getData(corners[2]) + d +
                getData(corners[2]) + d +
                getData(corners[3]) + d +
                getData(corners[0]) + d).toList()
    }

    private fun corners(): Array<Vec3>{
        return arrayOf(center - rv - uV, center + uV - rv, center + uV + rv, center + rv - uV)
    }

    fun getRect(): Rect3D{
        return Rect3D(corners()[0], rv * 2, uV * 2)
    }

    fun intersects(box: AxisAlignedCuboid): Boolean{
        // Checks if face is in the cuboid in the direction of its normal,
        // if so it can be processed like 2 2D faces
        val faceDir = Vec2(normal.getComponent(box.min), normal.getComponent(box.max))
        val qn = normal.getComponent(center) < faceDir.x || normal.getComponent(center) > faceDir.y
        if(qn) return false

        val e = abs(rv) + abs(uV)
        val faceMin = center - e
        val faceMax = center + e

        val u = normal.up().getComponent
        val r = normal.right().getComponent

        val qu = u(faceMin) < u(box.max) && u(box.min) < u(faceMax)
        val qr = r(faceMin) < r(box.max) && r(box.min) < r(faceMax)

        return qu && qr
    }

    override fun toString(): String {
        return "Face[${normal.name}]"
    }

    companion object{
        fun getData(v: Vec3): FloatArray{
            return floatArrayOf(v.x, v.y, v.z)
        }

        fun extentsFromSize(direction: Direction, size: Vec3): Vec2{
            return Vec2(direction.right().getComponent(size), direction.up().getComponent(size))
        }
    }
}

