package com.microsoft.playwright.jmeter.ui

import java.io.File
import javax.swing.filechooser.FileFilter

class PlaywrightFileFilter: FileFilter() {
    override fun accept(f: File?): Boolean {
        return f!!.name == "playwright.config.ts" || f!!.name == "playwright.config.mjs" || f!!.name == "playwright.config.js"
        // TODO : allow directories?
    }

    override fun getDescription(): String {
        return "Playwright config"
    }
}