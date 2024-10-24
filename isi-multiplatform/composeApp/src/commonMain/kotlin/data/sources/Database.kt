package data.sources

import com.guuri11.isi.Settings

expect class Database() {
    fun getSettings(): Settings
    fun modifySettings(settings: Settings)
}