package com.pineypiney.minecad

import com.pineypiney.game_engine.LibrarySetUp


fun main() {
    LibrarySetUp.initLibraries()
    MinecadWindow.INSTANCE.init()
    MinecadEngine.INSTANCE.run()
}