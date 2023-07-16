package com.pineypiney.minecad.block

import com.pineypiney.game_engine.util.maths.shapes.AxisAlignedCuboid
import glm_.vec2.Vec2

class BlockExcavator(val block: Block) {

    fun excavateFaces(box: AxisAlignedCuboid): List<Face>{
        val interFaces = block.faces.filter { it.intersects(box) }
        val face = interFaces.first()

        val boxN = face.n(box.size)
        val boxR = face.r(box.size)
        val boxU = face.u(box.size)

        val vr = face.normal.right().vector
        val vu = face.normal.up().vector

        val faces = cutOutFace(face, box).toTypedArray()

        val inLeftFace = Face(box.center - (vr * boxR * 0.5f), face.normal.right(), box.size * 0.5f)
        val inRightFace = Face(box.center + (vr * boxR * 0.5f), face.normal.right().opp(), box.size * 0.5f)
        val inDownFace = Face(box.center - (vu * boxU * 0.5f), face.normal.up(), box.size * 0.5f)
        val inUpFace = Face(box.center + (vu * boxU * 0.5f), face.normal.up().opp(), box.size * 0.5f)

        val bottomFace = Face(box.center - (face.normal.vector * boxN * 0.5f), face.normal, box.size * 0.5f)

        return listOf(*faces, inLeftFace, inRightFace, inDownFace, inUpFace, bottomFace)
    }

    fun cutOutFace(face: Face, box: AxisAlignedCuboid): List<Face>{
        val boxR = face.r(box.size)
        val boxU = face.u(box.size)

        val boxRelR = face.r(box.center - face.center) * face.normal.right().sign
        val boxRelU = face.u(box.center - face.center) * face.normal.up().sign

        val leftMargin = (face.extents.x + boxRelR) * 0.5f - (boxR * 0.25f)
        val rightMargin = leftMargin - boxRelR

        val downMargin = (face.extents.y + boxRelU) * 0.5f - (boxU * 0.25f)
        val upMargin = downMargin - boxRelU

        val vr = face.normal.right().vector
        val vu = face.normal.up().vector

        val faceList = mutableListOf<Face>()

        var hOffset = boxRelR
        var hWidth = box.size.x * 0.5f
        if(leftMargin < 0) {
            hOffset -= leftMargin
            hWidth += leftMargin
        }
        if(rightMargin < 0) {
            hOffset += rightMargin
            hWidth += rightMargin
        }

        if(leftMargin > 0){
            faceList.add(Face(face.center - face.rv + (vr * leftMargin), face.normal, Vec2(leftMargin, face.extents.y)))
        }
        if(rightMargin > 0){
            faceList.add(Face(face.center + face.rv - (vr * rightMargin), face.normal, Vec2(rightMargin, face.extents.y)))
        }
        if(downMargin > 0){
            faceList.add(Face(face.center - face.uV + (vu * downMargin) + (vr * hOffset), face.normal, Vec2(hWidth, downMargin)))
        }
        if(upMargin > 0){
            faceList.add(Face(face.center + face.uV - (vu * upMargin) + (vr * hOffset), face.normal, Vec2(hWidth, upMargin)))
        }

        return faceList
    }
}