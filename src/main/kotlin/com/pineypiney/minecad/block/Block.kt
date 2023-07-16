package com.pineypiney.minecad.block

import com.pineypiney.game_engine.GameLogicI
import com.pineypiney.game_engine.Timer
import com.pineypiney.game_engine.objects.game_objects.objects_3D.InteractableGameObject3D
import com.pineypiney.game_engine.resources.shaders.ShaderLoader
import com.pineypiney.game_engine.util.ResourceKey
import com.pineypiney.game_engine.util.maths.shapes.AxisAlignedCuboid
import com.pineypiney.game_engine.util.maths.up
import com.pineypiney.game_engine.util.raycasting.Ray
import com.pineypiney.minecad.MinecadEngine
import com.pineypiney.minecad.MinecadWindow
import glm_.b
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec3.Vec3

class Block: InteractableGameObject3D(blockShader) {

    val faces: MutableList<Face> = initialFaces()
    private var shape = BlockShape(faces)

    private val boundingBox = AxisAlignedCuboid(Vec3(), Vec3(1))

    val excavator = BlockExcavator(this)
    var mineSize = 0.1f

    var intersection: Vec3 = Vec3()
    var intersectionNormal: Vec3 = Vec3(1)

    var primaryTime = 0.0

    override fun setUniforms() {
        super.setUniforms()
        uniforms.setVec3Uniform("highlightColour"){Vec3(1, 1, 0)}

        uniforms.setVec3Uniform("intersection"){ intersection }
        uniforms.setVec3Uniform("intersectionNormal"){ intersectionNormal }

        uniforms.setFloatUniform("mineSize"){mineSize}
    }

    override fun render(view: Mat4, projection: Mat4, tickDelta: Double) {
        super.render(view, projection, tickDelta)
        shape.bindAndDraw()
    }

    override fun checkHover(ray: Ray, screenPos: Vec2): Boolean {
        return boundingBox.intersectedBy(ray).isNotEmpty()
    }

    fun containsPoint(point: Vec3): Boolean{
        val ray = Ray(point, up)
        val passesThrough = faces.filter { (it.normal == Direction.UP || it.normal == Direction.DOWN) }.map { it.getRect().intersectedBy(ray) }
        return (passesThrough.filter { it.isNotEmpty() && it[0].y > point.y }.size) % 2 == 1
    }

    override fun onCursorMove(game: GameLogicI, cursorPos: Vec2, cursorDelta: Vec2) {
        super.onCursorMove(game, cursorPos, cursorDelta)

        primaryTime = 0.0
        getRayIntersection()
    }

    override fun onPrimary(game: GameLogicI, action: Int, mods: Byte, cursorPos: Vec2): Int {
        super.onPrimary(game, action, mods, cursorPos)
        when(action) {
            1 -> primaryTime = Timer.frameTime
            0 -> {
                if(Timer.frameTime - primaryTime < 0.5) {
                    excavate(AxisAlignedCuboid(intersection - (intersectionNormal * mineSize), Vec3(mineSize * 2)))
                }
            }
        }
        return action
    }

    private fun excavate(box: AxisAlignedCuboid){
        val excavatedFaces = faces.filter { f -> f.intersects(box) }

        val newFaces = when(excavatedFaces.size){
            1 -> {
                excavator.excavateFaces(box)
            }
            in 2..99 -> {
                excavatedFaces.flatMap { excavator.cutOutFace(it, box) }
            }
            else -> listOf()
        }
        faces.removeAll(excavatedFaces)
        faces.addAll(newFaces)
        shape = BlockShape(faces)
    }

    fun getRayIntersection(){
        val cam = MinecadEngine.INSTANCE.activeScreen.camera
        val camPos = cam.cameraPos
        var hoveredFace: Face? = null
        var sqDistance = Float.MAX_VALUE
        var intersection = Vec3()
        val ray = cam.getRay(MinecadWindow.INSTANCE.input.mouse.lastPos)
        for(face in faces){
            val int = face.getRect().intersectedBy(ray).getOrNull(0) ?: continue
            val dist = (int - camPos).length2()
            if(dist < sqDistance){
                sqDistance = dist
                hoveredFace = face
                intersection = int
            }
        }

        if(hoveredFace != null){
            this.intersection = intersection
            intersectionNormal = hoveredFace.normal.vector
            forceUpdate = true
        }
        else{
            this.intersection = Vec3()
            intersectionNormal = Vec3(1)
            forceUpdate = false
        }
    }

    fun getBytes(): ByteArray{
        val bytes = MutableList<Byte>(80){ 32 }
        val triangles = shape.getVertices().toList().chunked(24)

        val numTriangles = triangles.size
        bytes.addAll(intByte(numTriangles))

        for(triangle in triangles){
            val normal = Vec3(triangle, 3).normalize()
            val v1 = Vec3(triangle)
            val v2 = Vec3(triangle, 8)
            val v3 = Vec3(triangle, 16)

            bytes.addAll(vertexBytes(normal) + vertexBytes(v1) + vertexBytes(v2) + vertexBytes(v3) + 0.b + 0.b)
        }

        return bytes.toByteArray()
    }

    companion object{
        val blockShader = ShaderLoader[ResourceKey("vertex/block"), ResourceKey("fragment/block")]

        fun vertexBytes(v: Vec3): List<Byte>{
            return listOf(*floatByte(v.x), *floatByte(v.y), *floatByte(v.z))
        }

        fun floatByte(f: Float): Array<Byte> = intByte(f.toRawBits())

        fun intByte(i: Int): Array<Byte>{
            return arrayOf((i and 255).b, ((i shr 8) and 255).b, ((i shr 16) and 255).b, (i shr 24).b)
        }

        fun initialFaces(): MutableList<Face>{
            return Direction.values().map { Face(it.vector * 0.5, it, Vec2(0.5)) }.toMutableList()
        }
    }
}