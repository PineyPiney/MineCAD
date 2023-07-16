package com.pineypiney.minecad.vr

import org.lwjgl.openvr.VR
import java.nio.IntBuffer

class VRUtil {


    companion object{
        fun initVR(): Long{
            val p = IntBuffer.allocate(1)
                val handle = VR.VR_InitInternal(p, VR.EVRApplicationType_VRApplication_Scene)
            if(p[0] != VR.EVRInitError_VRInitError_None) throw RuntimeException("VRInit failed")
            val l = VR.VR_GetGenericInterface("", p)
            return l
        }

        fun hmdIsPresent(): Boolean{
            return VR.VR_IsHmdPresent()
        }

        fun runtimeInstalled(): Boolean{
            return VR.VR_IsRuntimeInstalled()
        }
    }
}