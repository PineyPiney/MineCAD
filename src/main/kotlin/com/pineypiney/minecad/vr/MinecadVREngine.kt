package com.pineypiney.minecad.vr

import com.pineypiney.game_engine.resources.textures.Texture
import glm_.vec2.Vec2i
import kool.set
import org.lwjgl.opengl.GL46
import org.lwjgl.openvr.OpenVR.IVRSystem
import org.lwjgl.openvr.Texture as VRTexture
import org.lwjgl.openvr.TrackedDevicePose
import org.lwjgl.openvr.VR
import org.lwjgl.openvr.VRCompositor
import org.lwjgl.openvr.VRSystem
import java.nio.ByteBuffer
import java.nio.IntBuffer

// https://gist.github.com/VirtuosoChris/272f803966e62796b83dce2a597adcc7
class MinecadVREngine {

    val hmd: IVRSystem
    val rtWidth: Int
    val rtHeight: Int

    init {

        //if(!VRUtil.hmdIsPresent()) throw RuntimeException("No HMD system was detected")
        if(!VRUtil.runtimeInstalled()) throw RuntimeException("No runtime installed on system")

        val eb = IntBuffer.allocate(1)
        hmd = IVRSystem(VRUtil.initVR())
        VRSystem.VRSystem_GetStringTrackedDeviceProperty(VR.k_unTrackedDeviceIndex_Hmd, VR.ETrackedDeviceProperty_Prop_ManufacturerName_String, eb)
        VRSystem.VRSystem_GetStringTrackedDeviceProperty(VR.k_unTrackedDeviceIndex_Hmd, VR.ETrackedDeviceProperty_Prop_ModelNumber_String, eb)

        val s = getRenderDimensions()
        rtWidth = s.x
        rtHeight = s.y
    }

    fun submitFrames(leftTex: Texture, rightTex: Texture){

        val poses = TrackedDevicePose.Buffer(ByteBuffer.allocate(VR.k_unMaxTrackedDeviceCount * TrackedDevicePose.SIZEOF))
        VRCompositor.VRCompositor_WaitGetPoses(poses, null)

        val lt = VRTexture.create()
        val rt = VRTexture.create()

        VRCompositor.VRCompositor_Submit(VR.EVREye_Eye_Left, lt, null, 0)
        VRCompositor.VRCompositor_Submit(VR.EVREye_Eye_Right, rt, null, 0)

        VRCompositor.VRCompositor_PostPresentHandoff()
    }

    companion object{
        fun getRenderDimensions(): Vec2i{
            val (wb, hb) = IntBuffer.allocate(1) to IntBuffer.allocate(1)
            VRSystem.VRSystem_GetRecommendedRenderTargetSize(wb, hb)

            return Vec2i(wb[0], hb[0])
        }

        fun handleError(e: Int){
            throw RuntimeException(VR.VR_GetVRInitErrorAsEnglishDescription(e))
        }
    }
}