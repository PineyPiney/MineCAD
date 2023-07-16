package com.pineypiney.minecad.block

import com.pineypiney.game_engine.objects.util.shapes.ArrayShape

class BlockShape(faces: Collection<Face>): ArrayShape(getVertices(faces), intArrayOf(3, 3, 2)) {

    companion object{
        fun getVertices(faces: Collection<Face>): FloatArray{
            return faces.flatMap(Face::getVertices).toFloatArray()
        }
    }
}